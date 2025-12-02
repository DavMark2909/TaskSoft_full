package com.tasksoft.mark.mainservice.repository;

import com.tasksoft.mark.mainservice.entity.Task;
import com.tasksoft.mark.mainservice.entity.enums.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    long countByAssigneeUserIdAndTaskType(Long assigneeId, TaskType taskType);

    long countByAssigneeGroupIdAndTaskType(Long assigneeGroupId, TaskType taskType);

    List<Task> findByAssigneeUserIdAndTaskType(Long userId, TaskType type);

    List<Task> findByAssigneeGroupIdInAndTaskTypeOrderByAssigneeGroupNameAsc(Collection<Long> groupIds, TaskType type);

    @Query("SELECT t FROM Task t WHERE " +
            "(t.assigneeUser.id = :userId OR t.assigneeGroup.id IN :groupIds) " +
            "AND t.taskType = :type")
    long countMyTasksByType(
            @Param("userId") Long userId,
            @Param("groupIds") Collection<Long> groupIds,
            @Param("type") TaskType type
    );
}
