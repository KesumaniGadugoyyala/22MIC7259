package com.affordmed.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "notifications",
    indexes = {
        @Index(name = "idx_notifications_student_read_created", columnList = "student_id, is_read, created_at"),
        @Index(name = "idx_notifications_student_type_created", columnList = "student_id, type, created_at"),
        @Index(name = "idx_notifications_priority", columnList = "student_id, priority_score, created_at")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "student_id", nullable = false, length = 64)
    private String studentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 32)
    private NotificationType type;

    @Column(name = "message", nullable = false, length = 1024)
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean read;

    @Column(name = "priority_score", nullable = false)
    private int priorityScore;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
