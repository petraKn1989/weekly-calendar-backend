package com.example.demo.service;

import com.example.demo.entity.Task;
import com.example.demo.entity.TaskList;
import com.example.demo.repository.TaskListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskListService {

    private final TaskListRepository taskListRepository;

    public TaskListService(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    // 1️⃣ Vytvoření nového seznamu úkolů
    public TaskList createTaskList(List<Task> tasks) {
        // Vytvoří nový TaskList s generovaným UUID
        TaskList taskList = new TaskList();  // UUID bude generováno automaticky v konstruktoru

        // Každému úkolu přiřadíme tento nový TaskList
        for (Task task : tasks) {
            task.setTaskList(taskList);  // Přiřazení taskList k úkolu
        }

        // Nastavení úkolů do seznamu
        taskList.setTasks(tasks);

        // Uložení nového TaskListu s úkoly
        return taskListRepository.save(taskList);
    }

    // 2️⃣ Přidání úkolů do existujícího seznamu
    public TaskList addTasksToList(UUID taskListUuid, List<Task> tasks) {
        // Najdeme existující TaskList podle UUID
        TaskList taskList = taskListRepository.findByTaskListUuid(taskListUuid);

        if (taskList == null) {
            throw new RuntimeException("Task list not found with UUID: " + taskListUuid);
        }

        // Přiřadíme úkoly k nalezenému TaskListu
        for (Task task : tasks) {
            task.setTaskList(taskList);  // Přiřadíme TaskList k úkolu
            taskList.getTasks().add(task);  // Přidáme úkol do seznamu úkolů
        }

        // Uložíme aktualizovaný TaskList s novými úkoly
        return taskListRepository.save(taskList);
    }

    public TaskList getTaskListByUuid(UUID taskListUuid) {
    return taskListRepository.findByTaskListUuid(taskListUuid); // Najde seznam úkolů podle UUID
}



}
