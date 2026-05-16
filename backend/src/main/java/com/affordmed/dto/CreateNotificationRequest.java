package com.affordmed.dto;

import com.affordmed.entity.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateNotificationRequest {
    @NotBlank
    @Size(max = 64)
    private String studentId;

    @NotNull
    private NotificationType type;

    @NotBlank
    @Size(max = 1024)
    private String message;
}
