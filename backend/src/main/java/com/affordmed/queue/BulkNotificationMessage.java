package com.affordmed.queue;

import java.util.List;

import com.affordmed.dto.CreateNotificationRequest;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BulkNotificationMessage {
    List<CreateNotificationRequest> notifications;
}
