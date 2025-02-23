package com.anjana.to_do.controller;

import com.anjana.to_do.model.Task;
import com.anjana.to_do.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
    }

    @Test
    void getTasks_ShouldReturnListOfTasks() throws Exception {
        // Arrange
        List<Task> tasks = Arrays.asList(task);
        when(taskService.getIncompleteTasks()).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(task.getId()))
                .andExpect(jsonPath("$[0].title").value(task.getTitle()))
                .andExpect(jsonPath("$[0].description").value(task.getDescription()))
                .andExpect(jsonPath("$[0].completed").value(task.isCompleted()));

        verify(taskService).getIncompleteTasks();
    }

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        // Arrange
        when(taskService.createTask(any(Task.class))).thenReturn(task);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()));

        verify(taskService).createTask(any(Task.class));
    }

    @Test
    void markTaskAsDone_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(taskService).markTaskAsDone(1L);

        // Act & Assert
        mockMvc.perform(put("/api/tasks/{id}/done", 1L))
                .andExpect(status().isOk());

        verify(taskService).markTaskAsDone(1L);
    }

    @Test
    void createTask_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange
        Task invalidTask = new Task();
        // Not setting required fields

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTask)))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any(Task.class));
    }
} 