package com.study.rest;

import com.study.rest.dto.TaskRequest;
import com.study.rest.exception.TaskNotFoundException;
import com.study.rest.model.TaskStatus;
import com.study.rest.service.TaskService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    TaskService service = new TaskService();
    TaskRequest request = new TaskRequest("Задача", null, TaskStatus.TODO, null);

    @Test
    void createsWithGeneratedFields() {
        var t = service.create(request);
        assertNotNull(t.id());
        assertNotNull(t.createdAt());
        assertEquals(1, service.findAll().size());
    }

    @Test
    void updatePreservesCreation() {
        var t = service.create(request);
        var out = service.update(t.id(), new TaskRequest("Изменено", null, TaskStatus.DONE, null));
        assertEquals(t.createdAt(), out.createdAt());
        assertEquals(TaskStatus.DONE, out.status());
    }

    @Test
    void deleteAndMissing() {
        var t = service.create(request);
        service.delete(t.id());
        assertTrue(service.findById(t.id()).isEmpty());
        assertThrows(TaskNotFoundException.class, () -> service.delete(UUID.randomUUID()));
    }
}
