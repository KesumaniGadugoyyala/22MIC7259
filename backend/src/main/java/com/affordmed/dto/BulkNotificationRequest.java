package com.affordmed.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BulkNotificationRequest {
    @NotEmpty
    @Valid
    private List<CreateNotificationRequest> notifications;
}
