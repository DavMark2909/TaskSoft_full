package com.tasksoft.mark.mainservice.service;

import com.tasksoft.mark.mainservice.dto.HomeDashboardDTO;
import com.tasksoft.mark.mainservice.entity.Group;
import com.tasksoft.mark.mainservice.entity.Task;
import com.tasksoft.mark.mainservice.entity.User;
import com.tasksoft.mark.mainservice.entity.enums.TaskType;
import com.tasksoft.mark.mainservice.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HomeService {

    private final UserService userService;
    private final TaskRepository taskRepository;

    public HomeService(UserService userService, TaskRepository taskRepository) {
        this.userService = userService;
        this.taskRepository = taskRepository;
    }

    public HomeDashboardDTO getUserDashboard(Long userId) {
        User userById = userService.getUserById(userId);
        List<Long> userGroupIds = userById.getGroups().stream().map(Group::getId).toList();

        if (userGroupIds.isEmpty()) {
            return getDashboardForUserOnly(userId, userById.getFirstName());
        }

        long pendingCount = taskRepository.countMyTasksByType(userId, userGroupIds, TaskType.CREATED);
        long delayedCount = taskRepository.countMyTasksByType(userId, userGroupIds, TaskType.DELAYED);
        long completedCount = taskRepository.countMyTasksByType(userId, userGroupIds, TaskType.COMPLETED);

        List<Task> personalTask = taskRepository.findByAssigneeUserIdAndTaskType(userId, TaskType.CREATED);
        List<Task> groupTask = taskRepository.findByAssigneeGroupIdInAndTaskTypeOrderByAssigneeGroupNameAsc(userGroupIds, TaskType.CREATED);

        List<HomeDashboardDTO.TaskSummary> personalList = personalTask.stream().map(task -> convertToTaskSummary(task, true)).toList();
        List<HomeDashboardDTO.TaskSummary> groupList = groupTask.stream().map(task -> convertToTaskSummary(task, false)).toList();

        HomeDashboardDTO.DashboardStats dashboardStats = new HomeDashboardDTO.DashboardStats(completedCount, pendingCount, delayedCount);

        return new HomeDashboardDTO(userById.getFirstName(), dashboardStats, Stream.concat(personalList.stream(), groupList.stream()).collect(Collectors.toList()));
    }

    private HomeDashboardDTO getDashboardForUserOnly(Long userId, String userFirstName) {
        long pendingCount = taskRepository.countByAssigneeUserIdAndTaskType(userId, TaskType.CREATED);
        long delayedCount = taskRepository.countByAssigneeUserIdAndTaskType(userId, TaskType.DELAYED);
        long completedCount = taskRepository.countByAssigneeUserIdAndTaskType(userId, TaskType.COMPLETED);

        List<Task> personalTask = taskRepository.findByAssigneeUserIdAndTaskType(userId, TaskType.CREATED);
        List<HomeDashboardDTO.TaskSummary> personalList = personalTask.stream().map(task -> convertToTaskSummary(task, true)).toList();

        HomeDashboardDTO.DashboardStats dashboardStats = new HomeDashboardDTO.DashboardStats(completedCount, pendingCount, delayedCount);

        return new HomeDashboardDTO(userFirstName, dashboardStats, personalList);
    }

    private HomeDashboardDTO.TaskSummary convertToTaskSummary(Task task, boolean personal) {
        if (personal) {
            return new HomeDashboardDTO.TaskSummary(task.getId(), task.getName(), task.getDescription(), "Personal", task.getDueDate());
        }
        return new HomeDashboardDTO.TaskSummary(task.getId(), task.getName(), task.getDescription(), task.getAssigneeGroup().getName(), task.getDueDate());
    }

}
