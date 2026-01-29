package com.tasksoft.mark.mainservice.exception;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(Long groupId) {
        super(String.format("Group %s not found", groupId));
    }
}
