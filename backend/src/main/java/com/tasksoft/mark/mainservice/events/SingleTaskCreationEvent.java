package com.tasksoft.mark.mainservice.events;

import com.tasksoft.mark.mainservice.entity.Task;
import lombok.Getter;

@Getter
public class SingleTaskCreationEvent {
    private final Task task;

    public SingleTaskCreationEvent(Task task) {
        this.task = task;
    }
}
