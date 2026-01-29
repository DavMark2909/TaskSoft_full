package com.tasksoft.mark.mainservice.dto;

import com.tasksoft.mark.mainservice.entity.enums.TaskType;

public record TaskUpdateDto(
        Long id,
        String description,
        String name,
        TaskType type
) {
}
