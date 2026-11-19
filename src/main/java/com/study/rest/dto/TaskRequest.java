package com.study.rest.dto;

import com.study.rest.model.TaskStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskRequest(
        @NotBlank @Size(min = 3, max = 100) String title,
        @Size(max = 500) String description,
        @NotNull TaskStatus status,
        @Future LocalDateTime dueDate) {
}
