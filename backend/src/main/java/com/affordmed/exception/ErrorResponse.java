package com.affordmed.exception;

import java.time.Instant;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResponse {
    String message;
    String path;
    Instant timestamp;
}
