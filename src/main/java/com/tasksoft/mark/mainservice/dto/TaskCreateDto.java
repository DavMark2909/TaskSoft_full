package com.tasksoft.mark.mainservice.dto;

import com.tasksoft.mark.mainservice.entity.enums.NotificationType;

import java.time.LocalDateTime;

public record TaskCreateDto(
        String name,
        String description,
        Long assignerId,
        Long assigneeId,
        NotificationType type,
        LocalDateTime dueDate
) {
}
