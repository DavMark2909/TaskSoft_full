package com.tasksoft.mark.mainservice.dto;

public record SecurityUserDto(
        String username,
        Long id,
        String role
) {
}
