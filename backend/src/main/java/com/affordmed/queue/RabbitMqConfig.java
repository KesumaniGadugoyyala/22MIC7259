package com.affordmed.queue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import com.affordmed.config.AppProperties;

@Configuration
@ConditionalOnProperty(name = "app.queue.enabled", havingValue = "true", matchIfMissing = true)
public class RabbitMqConfig {
    @Bean
    public DirectExchange notificationExchange(AppProperties properties) {
        return new DirectExchange(properties.getQueue().getNotificationExchange(), true, false);
    }

    @Bean
    public Queue notificationQueue(AppProperties properties) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", properties.getQueue().getNotificationExchange());
        args.put("x-dead-letter-routing-key", properties.getQueue().getNotificationDlqRoutingKey());
        return new Queue(properties.getQueue().getNotificationQueue(), true, false, false, args);
    }

    @Bean
    public Queue notificationDlq(AppProperties properties) {
        return new Queue(properties.getQueue().getNotificationDlq(), true);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, DirectExchange notificationExchange, AppProperties properties) {
        return BindingBuilder.bind(notificationQueue)
            .to(notificationExchange)
            .with(properties.getQueue().getNotificationRoutingKey());
    }

    @Bean
    public Binding notificationDlqBinding(Queue notificationDlq, DirectExchange notificationExchange, AppProperties properties) {
        return BindingBuilder.bind(notificationDlq)
            .to(notificationExchange)
            .with(properties.getQueue().getNotificationDlqRoutingKey());
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
        MessageConverter converter, AppProperties properties) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        factory.setDefaultRequeueRejected(false);
        factory.setAdviceChain(retryInterceptor());
        factory.setAutoStartup(properties.getQueue().isEnabled());
        return factory;
    }

    @Bean
    public RetryOperationsInterceptor retryInterceptor() {
        return RetryInterceptorBuilder.stateless()
            .maxAttempts(3)
            .backOffOptions(1000, 2.0, 5000)
            .recoverer((args, cause) -> {
                throw new AmqpRejectAndDontRequeueException("Retries exhausted", cause);
            })
            .build();
    }
}
