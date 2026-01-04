package com.example.demo.service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Task updateTaskDay(Long taskId, Integer newDay) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    logger.warn("Task not found with id {}", taskId);
                    return new RuntimeException("Task not found");
                });

        task.setDay(newDay);
        Task updatedTask = taskRepository.save(task);

        logger.info("Task {} updated to day {}", taskId, newDay);
        return updatedTask;
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            logger.warn("Attempt to delete non-existing task {}", taskId);
            throw new RuntimeException("Task not found: " + taskId);
        }

        taskRepository.deleteById(taskId);
        logger.info("Task {} deleted", taskId);
    }
}
