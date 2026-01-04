package com.example.demo.controller;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskListRepository;
import com.example.demo.service.TaskService;

import java.util.Map;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService, TaskListRepository taskListRepository) {
        this.taskService = taskService;

    }

    @PatchMapping("/{taskId}/day")
    public ResponseEntity<Task> updateTaskDay(
            @PathVariable Long taskId,
            @RequestBody Map<String, Integer> body) {
        Integer newDay = body.get("day");
        if (newDay == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Task updatedTask = taskService.updateTaskDay(taskId, newDay);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build(); // 204
    }
}
