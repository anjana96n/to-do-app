package com.anjana.to_do.controller;


import com.anjana.to_do.model.Task;
import com.anjana.to_do.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public List<Task> getTasks() {
        return taskRepository.findTop5ByCompletedFalseOrderByDueDateAsc();
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @PutMapping("/{id}/done")
    public void markTaskAsDone(@PathVariable Long id) {
        taskRepository.findById(id).ifPresent(task -> {
            task.setCompleted(true);
            taskRepository.save(task);
        });
    }
}