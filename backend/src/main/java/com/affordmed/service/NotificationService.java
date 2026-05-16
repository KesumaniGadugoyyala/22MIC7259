package com.affordmed.service;

import org.springframework.data.domain.Pageable;

import com.affordmed.dto.ApiResponse;
import com.affordmed.dto.BulkNotificationRequest;
import com.affordmed.dto.NotificationResponse;
import com.affordmed.dto.PageResponse;
import com.affordmed.entity.NotificationType;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    PageResponse<NotificationResponse> getNotifications(String studentId, NotificationType type, Boolean read,
        Pageable pageable);

    NotificationResponse markRead(String studentId, UUID notificationId);

    NotificationResponse getNotification(String studentId, UUID notificationId);

    ApiResponse markAllRead(String studentId);

    List<NotificationResponse> getPriorityInbox(String studentId);

    ApiResponse enqueueBulk(BulkNotificationRequest request);
}
