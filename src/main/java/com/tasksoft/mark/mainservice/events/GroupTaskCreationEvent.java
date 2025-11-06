package com.tasksoft.mark.mainservice.events;

import com.tasksoft.mark.mainservice.entity.Task;
import lombok.Getter;

@Getter
public class GroupTaskCreationEvent {
    private final Task task;

    public GroupTaskCreationEvent(Task task) {
        this.task = task;
    }
}
