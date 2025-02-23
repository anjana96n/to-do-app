package com.anjana.to_do.controller;


import com.anjana.to_do.model.Task;
import com.anjana.to_do.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getTasks() {
        return taskService.getIncompleteTasks();
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}/done")
    public void markTaskAsDone(@PathVariable Long id) {
        taskService.markTaskAsDone(id);
    }
}