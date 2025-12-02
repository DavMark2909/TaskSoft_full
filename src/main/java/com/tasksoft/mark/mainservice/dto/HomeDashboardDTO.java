package com.tasksoft.mark.mainservice.dto;

import java.time.LocalDate;
import java.util.List;

public record HomeDashboardDTO(
        String userName,
        DashboardStats stats,
        List<TaskSummary> ongoingTasks
) {
    public record DashboardStats(
            long totalCompleted,
            long totalPending,
            long overdueCount
    ) {}

    public record TaskSummary(
            Long id,
            String title,
            String priority, // HIGH, MEDIUM, LOW
            LocalDate dueDate
    ) {}
}

//TODO: modify the task schema to include the create and due times