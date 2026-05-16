package com.affordmed.util;

import com.affordmed.entity.Notification;
import com.affordmed.entity.NotificationType;
import org.springframework.data.jpa.domain.Specification;

public final class NotificationSpecifications {
    private NotificationSpecifications() {
    }

    public static Specification<Notification> forStudent(String studentId) {
        return (root, query, cb) -> cb.equal(root.get("studentId"), studentId);
    }

    public static Specification<Notification> hasType(NotificationType type) {
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }

    public static Specification<Notification> isRead(boolean read) {
        return (root, query, cb) -> cb.equal(root.get("read"), read);
    }
}
