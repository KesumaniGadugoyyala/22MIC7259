package com.affordmed.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.affordmed.config.AppProperties;
import com.affordmed.entity.Notification;
import com.affordmed.repository.NotificationRepository;
import com.affordmed.service.PriorityInboxService;

@Service
@RequiredArgsConstructor
public class PriorityInboxServiceImpl implements PriorityInboxService {
    private final NotificationRepository repository;
    private final AppProperties properties;

    @Override
    public List<Notification> getPriorityNotifications(String studentId) {
        int maxSize = properties.getPriorityInbox().getMaxSize();
        int sampleSize = properties.getPriorityInbox().getSampleSize();
        List<Notification> candidates = repository.findRecentForPriority(studentId, PageRequest.of(0, sampleSize));

        PriorityQueue<Notification> heap = new PriorityQueue<>(Comparator
            .comparingInt(Notification::getPriorityScore)
            .thenComparing(Notification::getCreatedAt));

        for (Notification notification : candidates) {
            if (heap.size() < maxSize) {
                heap.offer(notification);
            } else if (compare(notification, heap.peek()) > 0) {
                heap.poll();
                heap.offer(notification);
            }
        }

        List<Notification> result = new ArrayList<>(heap);
        result.sort(Comparator
            .comparingInt(Notification::getPriorityScore).reversed()
            .thenComparing(Notification::getCreatedAt).reversed());
        return result;
    }

    private int compare(Notification left, Notification right) {
        int scoreCompare = Integer.compare(left.getPriorityScore(), right.getPriorityScore());
        if (scoreCompare != 0) {
            return scoreCompare;
        }
        return left.getCreatedAt().compareTo(right.getCreatedAt());
    }
}
