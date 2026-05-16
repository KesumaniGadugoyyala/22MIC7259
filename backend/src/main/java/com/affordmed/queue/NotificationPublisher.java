package com.affordmed.queue;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import com.affordmed.config.AppProperties;

@Service
@ConditionalOnProperty(name = "app.queue.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class NotificationPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final AppProperties properties;

    public void publish(BulkNotificationMessage message) {
        rabbitTemplate.convertAndSend(
            properties.getQueue().getNotificationExchange(),
            properties.getQueue().getNotificationRoutingKey(),
            message);
    }
}
