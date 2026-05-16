package com.affordmed.service;

import java.util.List;

import com.affordmed.entity.Notification;

public interface PriorityInboxService {
    List<Notification> getPriorityNotifications(String studentId);
}
