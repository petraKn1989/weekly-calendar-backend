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
@CrossOrigin // pokud voláš z frontendu
public class TaskListController {
    private final TaskListService taskListService;

    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    // 1️⃣ Vytvoření nového seznamu úkolů
    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> createTaskList(@RequestBody List<Task> tasks) {
       
        TaskList createdTaskList = taskListService.createTaskList(tasks);

        Map<String, String> response = new HashMap<>();
        response.put("taskListUuid", createdTaskList.getTaskListUuid().toString());

        return ResponseEntity.ok(response);  // Vrátíme UUID seznamu úkolů
    }

    // 2️⃣ Přidání nových úkolů do existujícího seznamu
    @PostMapping("/addTasks")
    public TaskList addTasksToList(@RequestParam UUID taskListUuid, @RequestBody List<Task> tasks) {
        // Backend najde TaskList podle UUID a přidá nové úkoly
        return taskListService.addTasksToList(taskListUuid, tasks);
    }

   @GetMapping("/{taskListUuid}")
public ResponseEntity<TaskList> getTaskListByUuid(@PathVariable String taskListUuid) {
    try {
        // Převod UUID z parametru v URL
        UUID uuid = UUID.fromString(taskListUuid);
        
        // Zavolání služby pro získání TaskListu podle UUID
        TaskList taskList = taskListService.getTaskListByUuid(uuid); 
        
        if (taskList == null) {
            return ResponseEntity.notFound().build(); // Vrátí 404 pokud TaskList neexistuje
        }
        
        return ResponseEntity.ok(taskList); // Vrátí TaskList v odpovědi
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().build(); // Pokud je UUID neplatné, vrátí 400
    }
}


    
}
