package com.affordmed.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.affordmed.dto.ApiResponse;
import com.affordmed.dto.BulkNotificationRequest;
import com.affordmed.dto.NotificationResponse;
import com.affordmed.dto.PageResponse;
import com.affordmed.entity.NotificationType;
import com.affordmed.security.SecurityUtils;
import com.affordmed.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public PageResponse<NotificationResponse> getNotifications(
        @RequestParam(required = false) NotificationType type,
        @RequestParam(required = false) Boolean read,
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        String studentId = SecurityUtils.getStudentId();
        return notificationService.getNotifications(studentId, type, read, pageable);
    }

    @PatchMapping("/{id}/read")
    public NotificationResponse markRead(@PathVariable("id") UUID id) {
        String studentId = SecurityUtils.getStudentId();
        return notificationService.markRead(studentId, id);
    }

    @GetMapping("/{id}")
    public NotificationResponse getNotification(@PathVariable("id") UUID id) {
        String studentId = SecurityUtils.getStudentId();
        return notificationService.getNotification(studentId, id);
    }

    @PatchMapping("/read-all")
    public ApiResponse markAllRead() {
        String studentId = SecurityUtils.getStudentId();
        return notificationService.markAllRead(studentId);
    }

    @GetMapping("/priority")
    public List<NotificationResponse> getPriorityInbox() {
        String studentId = SecurityUtils.getStudentId();
        return notificationService.getPriorityInbox(studentId);
    }

    @PostMapping("/bulk")
    public ApiResponse enqueueBulk(@Valid @RequestBody BulkNotificationRequest request) {
        return notificationService.enqueueBulk(request);
    }
}
