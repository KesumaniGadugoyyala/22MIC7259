package com.affordmed.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.affordmed.cache.CacheNames;
import com.affordmed.dto.ApiResponse;
import com.affordmed.dto.BulkNotificationRequest;
import com.affordmed.dto.NotificationResponse;
import com.affordmed.dto.PageResponse;
import com.affordmed.entity.Notification;
import com.affordmed.entity.NotificationType;
import com.affordmed.exception.NotFoundException;
import com.affordmed.mapper.NotificationMapper;
import com.affordmed.queue.BulkNotificationMessage;
import com.affordmed.queue.NotificationPublisher;
import org.springframework.beans.factory.ObjectProvider;
import com.affordmed.repository.NotificationRepository;
import com.affordmed.service.NotificationService;
import com.affordmed.service.PriorityInboxService;
import com.affordmed.util.NotificationSpecifications;
import com.affordmed.util.PriorityScoringUtil;
import com.affordmed.websocket.NotificationWebSocketService;
import com.affordmed.config.AppProperties;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final PriorityInboxService priorityInboxService;
    private final ObjectProvider<NotificationPublisher> publisherProvider;
    private final NotificationWebSocketService webSocketService;
    private final AppProperties properties;

    @Override
    @Cacheable(cacheNames = CacheNames.NOTIFICATIONS,
        key = "T(com.affordmed.util.CacheKey).forNotifications(#studentId, #type, #read, #pageable)")
    public PageResponse<NotificationResponse> getNotifications(String studentId, NotificationType type, Boolean read,
        Pageable pageable) {
        Specification<Notification> spec = Specification.where(NotificationSpecifications.forStudent(studentId));
        if (type != null) {
            spec = spec.and(NotificationSpecifications.hasType(type));
        }
        if (read != null) {
            spec = spec.and(NotificationSpecifications.isRead(read));
        }
        Page<Notification> page = repository.findAll(spec, pageable);
        Page<NotificationResponse> mapped = page.map(mapper::toResponse);
        return PageResponse.from(mapped);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.NOTIFICATIONS, CacheNames.PRIORITY_INBOX}, allEntries = true)
    public NotificationResponse markRead(String studentId, UUID notificationId) {
        Notification notification = repository.findByIdAndStudentId(notificationId, studentId)
            .orElseThrow(() -> new NotFoundException("Notification not found"));
        notification.setRead(true);
        return mapper.toResponse(repository.save(notification));
    }

    @Override
    public NotificationResponse getNotification(String studentId, UUID notificationId) {
        Notification notification = repository.findByIdAndStudentId(notificationId, studentId)
            .orElseThrow(() -> new NotFoundException("Notification not found"));
        return mapper.toResponse(notification);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.NOTIFICATIONS, CacheNames.PRIORITY_INBOX}, allEntries = true)
    public ApiResponse markAllRead(String studentId) {
        int updated = repository.markAllRead(studentId);
        return ApiResponse.builder()
            .success(true)
            .message("Marked " + updated + " notifications as read")
            .build();
    }

    @Override
    @Cacheable(cacheNames = CacheNames.PRIORITY_INBOX, key = "#studentId")
    public List<NotificationResponse> getPriorityInbox(String studentId) {
        return priorityInboxService.getPriorityNotifications(studentId).stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(cacheNames = {CacheNames.NOTIFICATIONS, CacheNames.PRIORITY_INBOX}, allEntries = true)
    public ApiResponse enqueueBulk(BulkNotificationRequest request) {
        if (properties.getQueue().isEnabled()) {
            NotificationPublisher publisher = publisherProvider.getIfAvailable();
            if (publisher == null) {
                throw new IllegalStateException("Queue publisher not available");
            }
            publisher.publish(BulkNotificationMessage.builder()
                .notifications(request.getNotifications())
                .build());
            return ApiResponse.builder()
                .success(true)
                .message("Notifications queued")
                .build();
        }

        List<Notification> notifications = request.getNotifications().stream()
            .map(req -> {
                var createdAt = java.time.Instant.now();
                return Notification.builder()
                    .studentId(req.getStudentId())
                    .type(req.getType())
                    .message(req.getMessage())
                    .read(false)
                    .priorityScore(PriorityScoringUtil.calculateScore(req.getType(), createdAt))
                    .createdAt(createdAt)
                    .build();
            })
            .toList();

        repository.saveAll(notifications);
        notifications.stream()
            .map(mapper::toResponse)
            .forEach(webSocketService::sendNotification);

        return ApiResponse.builder()
            .success(true)
            .message("Notifications processed")
            .build();
    }
}
