package com.study.rest.controller;

import com.study.rest.dto.TaskRequest;
import com.study.rest.exception.TaskNotFoundException;
import com.study.rest.model.Task;
import com.study.rest.model.TaskStatus;
import com.study.rest.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> all(@RequestParam(required = false) TaskStatus status) {
        return service.findAll().stream().filter(t -> status == null || t.status() == status).toList();
    }

    @GetMapping("/{id}")
    public Task get(@PathVariable UUID id) {
        return service.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody TaskRequest r) {
        Task t = service.create(r);
        return ResponseEntity.created(URI.create("/tasks/" + t.id())).body(t);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable UUID id, @Valid @RequestBody TaskRequest r) {
        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public Task status(@PathVariable UUID id, @Valid @RequestBody StatusRequest r) {
        return service.updateStatus(id, r.status());
    }

    @GetMapping("/stats")
    public Map<TaskStatus, Long> stats() {
        return service.findAll().stream()
                .collect(Collectors.groupingBy(Task::status, Collectors.counting()));
    }

    public record StatusRequest(@NotNull TaskStatus status) {
    }
}
