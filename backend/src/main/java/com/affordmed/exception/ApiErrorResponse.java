package com.affordmed.exception;

import java.time.Instant;
import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiErrorResponse {
    String message;
    int status;
    Instant timestamp;
    Map<String, String> details;
}
