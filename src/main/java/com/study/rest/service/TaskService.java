package com.study.rest.service;

import com.study.rest.dto.TaskRequest;
import com.study.rest.exception.TaskNotFoundException;
import com.study.rest.model.Task;
import com.study.rest.model.TaskStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {
    private final ConcurrentHashMap<UUID, Task> tasks = new ConcurrentHashMap<>();

    public Task create(TaskRequest r) {
        Task t =
                new Task(
                        UUID.randomUUID(),
                        r.title(),
                        r.description(),
                        r.status(),
                        LocalDateTime.now(),
                        r.dueDate());
        tasks.put(t.id(), t);
        return t;
    }

    public List<Task> findAll() {
        return List.copyOf(tasks.values());
    }

    public Optional<Task> findById(UUID id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public Task update(UUID id, TaskRequest r) {
        Task t =
                tasks.computeIfPresent(
                        id,
                        (k, old) ->
                                new Task(id, r.title(), r.description(), r.status(), old.createdAt(), r.dueDate()));
        if (t == null) throw new TaskNotFoundException(id);
        return t;
    }

    public Task updateStatus(UUID id, TaskStatus status) {
        Task t =
                tasks.computeIfPresent(
                        id,
                        (k, old) ->
                                new Task(
                                        id, old.title(), old.description(), status, old.createdAt(), old.dueDate()));
        if (t == null) throw new TaskNotFoundException(id);
        return t;
    }

    public void delete(UUID id) {
        if (tasks.remove(id) == null) throw new TaskNotFoundException(id);
    }
}
