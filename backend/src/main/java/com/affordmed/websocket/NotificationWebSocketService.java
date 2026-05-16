package com.affordmed.websocket;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.affordmed.dto.NotificationResponse;

@Service
@RequiredArgsConstructor
public class NotificationWebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(NotificationResponse response) {
        String destination = "/topic/notifications/" + response.getStudentId();
        messagingTemplate.convertAndSend(destination, response);
    }
}
