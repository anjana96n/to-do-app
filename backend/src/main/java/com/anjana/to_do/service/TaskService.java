package com.anjana.to_do.service;

import com.anjana.to_do.model.Task;
import com.anjana.to_do.model.Priority;
import com.anjana.to_do.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getIncompleteTasks() {
        return taskRepository.findTop5ByCompletedFalseOrderByDueDateAsc();
    }

    public Task createTask(Task task) {
        validateTask(task);
        if (task.getPriority() == null) {
            task.setPriority(Priority.LOW);
        }
        return taskRepository.save(task);
    }

    private void validateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
    }

    public void markTaskAsDone(Long id) {
        taskRepository.findById(id).ifPresent(task -> {
            task.setCompleted(true);
            taskRepository.save(task);
        });
    }
} 