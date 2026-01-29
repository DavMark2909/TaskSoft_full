package com.tasksoft.mark.mainservice.service;

import com.tasksoft.mark.mainservice.dto.GroupCreateDto;
import com.tasksoft.mark.mainservice.dto.GroupDashboardDTO;
import com.tasksoft.mark.mainservice.dto.GroupDto;
import com.tasksoft.mark.mainservice.dto.HomeDashboardDTO;
import com.tasksoft.mark.mainservice.entity.Group;
import com.tasksoft.mark.mainservice.entity.Task;
import com.tasksoft.mark.mainservice.entity.User;
import com.tasksoft.mark.mainservice.entity.enums.TaskType;
import com.tasksoft.mark.mainservice.exception.GroupNotFoundException;
import com.tasksoft.mark.mainservice.exception.OperationFailedException;
import com.tasksoft.mark.mainservice.repository.GroupRepository;
import com.tasksoft.mark.mainservice.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;

    public GroupService(GroupRepository groupRepository, TaskRepository taskRepository, UserService userService) {
        this.groupRepository = groupRepository;
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Transactional
    public Group createGroup(GroupCreateDto dto){
        Group group = new Group();
        group.setName(dto.name());
        List<User> users = userService.getUsersById(dto.users());
        if (users.size() != dto.users().size()) {
            throw new OperationFailedException("One or more users not found. Failed creating a group");
        }
        for (User user : users) {
            group.addMember(user);
        }
        return groupRepository.save(group);
    }

    @Transactional
    public void addUserToGroup(Long groupId, Long userId){
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
        User user = userService.getUserById(userId);
        group.addMember(user);
    }

    @Transactional
    public void removeUserFromGroup(Long groupId, Long userId){
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
        User user = userService.getUserById(userId);
        group.removeMember(user);
    }

    public Group getGroup(Long groupId){
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
    }

    public List<Group> getUserGroups(Long userId) {
        User user = userService.getUserById(userId);
        return user.getGroups();
    }

    public void deleteGroup(Long groupId){
        groupRepository.deleteById(groupId);
    }

    public List<GroupDto> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(group -> new GroupDto(group.getId(), group.getName())).toList();
    }

    public GroupDashboardDTO getGroupDashboard(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
        long pendingCount = taskRepository.countByAssigneeGroupIdAndTaskType(id, TaskType.CREATED);
        long delayedCount = taskRepository.countByAssigneeGroupIdAndTaskType(id, TaskType.DELAYED);
        long completedCount = taskRepository.countByAssigneeGroupIdAndTaskType(id, TaskType.COMPLETED);

        List<Task> groupTask = taskRepository.findByAssigneeGroupIdAndTaskType(id, TaskType.CREATED);

        List<HomeDashboardDTO.TaskSummary> groupList = groupTask.stream().map(this::convertToTaskSummary).toList();
        HomeDashboardDTO.DashboardStats dashboardStats = new HomeDashboardDTO.DashboardStats(completedCount, pendingCount, delayedCount);

        return new GroupDashboardDTO(group.getName(), dashboardStats, groupList);
    }

    private HomeDashboardDTO.TaskSummary convertToTaskSummary(Task task) {
        return new HomeDashboardDTO.TaskSummary(task.getId(), task.getName(), task.getDescription(), task.getAssigneeGroup().getName(), task.getDueDate());
    }
}
