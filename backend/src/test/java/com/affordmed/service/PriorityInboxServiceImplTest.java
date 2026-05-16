package com.affordmed.service;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import com.affordmed.config.AppProperties;
import com.affordmed.entity.Notification;
import com.affordmed.repository.NotificationRepository;
import com.affordmed.service.impl.PriorityInboxServiceImpl;

class PriorityInboxServiceImplTest {
    @Test
    void returnsTopNotificationsByPriority() {
        NotificationRepository repository = Mockito.mock(NotificationRepository.class);
        AppProperties properties = new AppProperties();
        properties.getPriorityInbox().setMaxSize(2);
        properties.getPriorityInbox().setSampleSize(5);

        List<Notification> sample = List.of(
            Notification.builder().studentId("S-1").priorityScore(100).createdAt(Instant.now()).build(),
            Notification.builder().studentId("S-1").priorityScore(300).createdAt(Instant.now()).build(),
            Notification.builder().studentId("S-1").priorityScore(200).createdAt(Instant.now()).build()
        );

        Mockito.when(repository.findRecentForPriority(Mockito.eq("S-1"), Mockito.any(PageRequest.class)))
            .thenReturn(sample);

        PriorityInboxService service = new PriorityInboxServiceImpl(repository, properties);
        List<Notification> result = service.getPriorityNotifications("S-1");

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.get(0).getPriorityScore() >= result.get(1).getPriorityScore());
    }
}
