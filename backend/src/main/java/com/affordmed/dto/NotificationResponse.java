package com.affordmed.dto;

import java.time.Instant;
import java.util.UUID;

import com.affordmed.entity.NotificationType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NotificationResponse {
    UUID id;
    String studentId;
    NotificationType type;
    String message;
    boolean read;
    int priorityScore;
    Instant createdAt;
}
