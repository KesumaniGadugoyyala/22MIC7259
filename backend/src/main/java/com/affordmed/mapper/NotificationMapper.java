package com.affordmed.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.affordmed.dto.NotificationResponse;
import com.affordmed.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "read", source = "read")
    NotificationResponse toResponse(Notification notification);
}
