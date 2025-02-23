package com.anjana.to_do.integration;

import com.anjana.to_do.model.Task;
import com.anjana.to_do.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void createAndRetrieveTask() throws Exception {
        // Create a new task
        Task task = new Task();
        task.setTitle("Integration Test Task");
        task.setDescription("Test Description");
        task.setDueDate(new Date());
        task.setCompleted(false);

        // POST request to create task
        String response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Test Task"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Task createdTask = objectMapper.readValue(response, Task.class);

        // Verify task is in database
        Task savedTask = taskRepository.findById(createdTask.getId())
                .orElseThrow(() -> new AssertionError("Task not found"));
        assertEquals("Integration Test Task", savedTask.getTitle());

        // GET request to retrieve tasks
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Integration Test Task"));
    }

    @Test
    void markTaskAsDone() throws Exception {
        // Create a task first
        Task task = new Task();
        task.setTitle("Task to Complete");
        task.setDescription("Test Description");
        task.setCompleted(false);
        Task savedTask = taskRepository.save(task);

        // Mark task as done
        mockMvc.perform(put("/api/tasks/{id}/done", savedTask.getId()))
                .andExpect(status().isOk());

        // Verify task is marked as completed in database
        Task completedTask = taskRepository.findById(savedTask.getId())
                .orElseThrow(() -> new AssertionError("Task not found"));
        assertTrue(completedTask.isCompleted());
    }

    @Test
    void getIncompleteTasks() throws Exception {
        // Create multiple tasks
        for (int i = 0; i < 6; i++) {
            Task task = new Task();
            task.setTitle("Task " + i);
            task.setDescription("Description " + i);
            task.setDueDate(new Date());
            task.setCompleted(i >= 3); // First 3 tasks incomplete
            taskRepository.save(task);
        }

        // Verify only top 5 incomplete tasks are returned
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3)); // Only 3 incomplete tasks
    }
} 