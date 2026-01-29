package com.tasksoft.mark.mainservice.dto;

import java.util.List;

public record GroupCreateDto(
        String name,
        List<Long> users
) {
}
