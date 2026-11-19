package com.study.rest.exception;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {
    private final UUID id;

    public TaskNotFoundException(UUID id) {
        super("Task not found: " + id);
        this.id = id;
    }

    public UUID getTaskId() {
        return id;
    }
}
