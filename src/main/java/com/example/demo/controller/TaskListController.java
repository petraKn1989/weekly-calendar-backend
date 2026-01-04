package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Task;
import com.example.demo.entity.TaskList;
import com.example.demo.service.TaskListService;

@RestController
@RequestMapping("/api/tasklists")
@CrossOrigin 
public class TaskListController {
    private final TaskListService taskListService;

    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    // Vytvoření nového seznamu úkolů
    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> createTaskList(@RequestBody List<Task> tasks) {

        TaskList createdTaskList = taskListService.createTaskList(tasks);

        Map<String, String> response = new HashMap<>();
        response.put("taskListUuid", createdTaskList.getTaskListUuid().toString());

        return ResponseEntity.ok(response); 
    }

    // 2️⃣ Přidání nových úkolů do existujícího seznamu
    @PostMapping("/addTasks")
    public TaskList addTasksToList(@RequestParam UUID taskListUuid, @RequestBody List<Task> tasks) {
     
        return taskListService.addTasksToList(taskListUuid, tasks);
    }

    @GetMapping("/{taskListUuid}")
    public ResponseEntity<TaskList> getTaskListByUuid(@PathVariable String taskListUuid) {
        try {
      
            UUID uuid = UUID.fromString(taskListUuid);
            TaskList taskList = taskListService.getTaskListByUuid(uuid);

            if (taskList == null) {
                return ResponseEntity.notFound().build(); 
            }

            return ResponseEntity.ok(taskList); 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); 
        }
    }

}
