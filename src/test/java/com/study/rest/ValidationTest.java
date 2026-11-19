package com.study.rest;

import com.study.rest.controller.TaskController;
import com.study.rest.exception.GlobalExceptionHandler;
import com.study.rest.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ValidationTest {
    MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc =
                MockMvcBuilders.standaloneSetup(new TaskController(new TaskService()))
                        .setControllerAdvice(new GlobalExceptionHandler())
                        .build();
    }

    @Test
    void emptyTitle() throws Exception {
        invalid("{\"title\":\"\",\"status\":\"TODO\"}", "title");
    }

    @Test
    void missingStatus() throws Exception {
        invalid("{\"title\":\"Название\"}", "status");
    }

    @Test
    void pastDueDate() throws Exception {
        invalid(
                "{\"title\":\"Название\",\"status\":\"TODO\",\"dueDate\":\"2000-01-01T00:00:00\"}",
                "dueDate");
    }

    void invalid(String json, String field) throws Exception {
        mvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value(field));
    }

    @Test
    void createAndNotFound() throws Exception {
        mvc.perform(
                        post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"Название\",\"status\":\"TODO\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
        mvc.perform(get("/tasks/" + java.util.UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
