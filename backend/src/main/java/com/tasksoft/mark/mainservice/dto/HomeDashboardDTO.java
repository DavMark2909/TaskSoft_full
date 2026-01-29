package com.tasksoft.mark.mainservice.dto;

import java.time.LocalDateTime;
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
            String description,
            String group,
            LocalDateTime dueDate
    ) {}
}
