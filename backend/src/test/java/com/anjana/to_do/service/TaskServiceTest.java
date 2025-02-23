package com.anjana.to_do.service;

import com.anjana.to_do.model.Task;
import com.anjana.to_do.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

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
    void getIncompleteTasks_ShouldReturnListOfTasks() {
        // Arrange
        List<Task> expectedTasks = Arrays.asList(task);
        when(taskRepository.findTop5ByCompletedFalseOrderByDueDateAsc()).thenReturn(expectedTasks);

        // Act
        List<Task> actualTasks = taskService.getIncompleteTasks();

        // Assert
        assertNotNull(actualTasks);
        assertEquals(expectedTasks.size(), actualTasks.size());
        assertEquals(expectedTasks.get(0).getTitle(), actualTasks.get(0).getTitle());
        verify(taskRepository).findTop5ByCompletedFalseOrderByDueDateAsc();
    }

    @Test
    void createTask_ShouldReturnSavedTask() {
        // Arrange
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task savedTask = taskService.createTask(task);

        // Assert
        assertNotNull(savedTask);
        assertEquals(task.getTitle(), savedTask.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    void markTaskAsDone_ShouldUpdateTaskToCompleted() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        taskService.markTaskAsDone(1L);

        // Assert
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(task);
        assertTrue(task.isCompleted());
    }

    @Test
    void markTaskAsDone_WhenTaskNotFound_ShouldDoNothing() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        taskService.markTaskAsDone(1L);

        // Assert
        verify(taskRepository).findById(1L);
        verify(taskRepository, never()).save(any());
    }
} 