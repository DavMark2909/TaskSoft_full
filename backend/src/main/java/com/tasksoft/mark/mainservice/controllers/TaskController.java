package com.tasksoft.mark.mainservice.controllers;

import com.tasksoft.mark.mainservice.dto.TaskCreateDto;
import com.tasksoft.mark.mainservice.dto.TaskUpdateDto;
import com.tasksoft.mark.mainservice.entity.Task;
import com.tasksoft.mark.mainservice.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createTask(@RequestBody TaskCreateDto taskCreateDto) {
        Task task = taskService.createTask(taskCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(task.getId());
    }



    @GetMapping("/update")
    public ResponseEntity<Long> updateTask(@RequestBody TaskUpdateDto taskUpdateDto) {
        Task task = taskService.updateStatus(taskUpdateDto.id(), taskUpdateDto.name(), taskUpdateDto.description(), taskUpdateDto.type());
        return ResponseEntity.status(HttpStatus.OK).body(task.getId());
    }
}
