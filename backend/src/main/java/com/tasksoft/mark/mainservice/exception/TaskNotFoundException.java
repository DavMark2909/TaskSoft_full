package com.tasksoft.mark.mainservice.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super(String.format("Task with id %s not found", id));
    }
}
