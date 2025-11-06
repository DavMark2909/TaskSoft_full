package com.tasksoft.mark.mainservice.service;

import com.tasksoft.mark.mainservice.entity.Group;
import com.tasksoft.mark.mainservice.entity.enums.TaskType;
import com.tasksoft.mark.mainservice.events.GroupTaskCreationEvent;
import com.tasksoft.mark.mainservice.events.SingleTaskCreationEvent;
import com.tasksoft.mark.mainservice.exception.TaskNotFoundException;
import com.tasksoft.mark.mainservice.dto.TaskCreateDto;
import com.tasksoft.mark.mainservice.entity.Task;
import com.tasksoft.mark.mainservice.entity.User;
import com.tasksoft.mark.mainservice.repository.TaskRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final GroupService groupService;
    private final ApplicationEventPublisher eventPublisher;

    public TaskService(TaskRepository taskRepository, UserService userService, GroupService groupService, ApplicationEventPublisher eventPublisher) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.groupService = groupService;
        this.eventPublisher = eventPublisher;
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public Task createTask(TaskCreateDto dto) {
        User assigner = userService.getUserById(dto.assignerId());
        Task task = new Task();
        task.setAssigner(assigner);
        task.setTaskType(TaskType.CREATED);
        task.setName(dto.name());
        task.setDescription(dto.description());
        switch (dto.type()){
            case GROUP:
                Group group = groupService.getGroup(dto.assigneeId());
                task.setGroup(group);
                break;
            case SINGLE:
                User user = userService.getUserById(dto.assigneeId());
                task.setUser(user);
                break;
        }
        Task savedTask = taskRepository.save(task);
        switch (dto.type()){
            case GROUP:
                eventPublisher.publishEvent(new GroupTaskCreationEvent(savedTask));
                break;
            case SINGLE:
                eventPublisher.publishEvent(new SingleTaskCreationEvent(savedTask));
                break;
        }
        return savedTask;
    }

    @Transactional
    public Task updateStatus(Long id, String name, String description, TaskType type) {
        Task task = getTaskById(id);
        task.setName(name);
        task.setDescription(description);
        task.setTaskType(type);
        return task;
    }

}
