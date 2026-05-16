package com.affordmed.queue;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.transaction.annotation.Transactional;

import com.affordmed.dto.CreateNotificationRequest;
import com.affordmed.entity.Notification;
import com.affordmed.mapper.NotificationMapper;
import com.affordmed.repository.NotificationRepository;
import com.affordmed.util.PriorityScoringUtil;
import com.affordmed.websocket.NotificationWebSocketService;

@Service
@ConditionalOnProperty(name = "app.queue.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class NotificationConsumer {
    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final NotificationWebSocketService webSocketService;

    @RabbitListener(queues = "${app.queue.notification-queue}")
    @Transactional
    public void handleBulkNotifications(BulkNotificationMessage message) {
        List<Notification> notifications = message.getNotifications().stream()
            .map(this::toNotification)
            .collect(Collectors.toList());

        repository.saveAll(notifications);

        notifications.forEach(notification ->
            logger.info("Email simulation sent to {} for {}", notification.getStudentId(), notification.getType()));

        notifications.stream()
            .map(mapper::toResponse)
            .forEach(webSocketService::sendNotification);

        logger.info("Processed {} notifications", notifications.size());
    }

    private Notification toNotification(CreateNotificationRequest request) {
        Instant createdAt = Instant.now();
        int score = PriorityScoringUtil.calculateScore(request.getType(), createdAt);
        return Notification.builder()
            .studentId(request.getStudentId())
            .type(request.getType())
            .message(request.getMessage())
            .read(false)
            .priorityScore(score)
            .createdAt(createdAt)
            .build();
    }
}
