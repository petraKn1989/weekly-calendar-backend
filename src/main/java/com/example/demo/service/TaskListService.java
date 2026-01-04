package com.example.demo.service;

import com.example.demo.entity.Task;
import com.example.demo.entity.TaskList;
import com.example.demo.repository.TaskListRepository;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

@Service
public class TaskListService {

    private static final Logger logger = LoggerFactory.getLogger(TaskListService.class);

    private final TaskListRepository taskListRepository;

    public TaskListService(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    // Vytvoření nového seznamu úkolů
    @Transactional
    public TaskList createTaskList(List<Task> tasks) {

        TaskList taskList = new TaskList();

        for (Task task : tasks) {
            task.setTaskList(taskList);
        }

        taskList.setTasks(tasks);

        TaskList savedList = taskListRepository.save(taskList);

        logger.info("Created TaskList {} with {} tasks", savedList.getTaskListUuid(), tasks.size());
        return savedList;
    }

    // Přidání úkolů do existujícího seznamu
    @Transactional
    public TaskList addTasksToList(UUID taskListUuid, List<Task> tasks) {

        TaskList taskList = taskListRepository.findByTaskListUuidWithTasks(taskListUuid);

        if (taskList == null) {
            logger.warn("Attempt to add tasks to non-existing TaskList UUID {}", taskListUuid);
            throw new RuntimeException("Task list not found with UUID: " + taskListUuid);
        }

        for (Task task : tasks) {
            task.setTaskList(taskList);
            taskList.getTasks().add(task);
        }

        TaskList updatedList = taskListRepository.save(taskList);
        logger.info("Added {} tasks to TaskList {}", tasks.size(), taskListUuid);

        return updatedList;
    }

    @Transactional(readOnly = true)
    public TaskList getTaskListByUuid(UUID taskListUuid) {
        TaskList taskList = taskListRepository.findByTaskListUuidWithTasks(taskListUuid);

        if (taskList == null) {
            logger.warn("TaskList not found with UUID {}", taskListUuid);
        } else {
            logger.info("Fetched TaskList {} with {} tasks", taskListUuid, taskList.getTasks().size());
        }

        return taskList;
    }

}
