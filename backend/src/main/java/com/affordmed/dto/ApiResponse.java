package com.affordmed.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiResponse {
    boolean success;
    String message;
}
