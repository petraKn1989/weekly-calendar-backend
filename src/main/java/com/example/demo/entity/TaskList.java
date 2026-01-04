package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "task_lists")
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_list_uuid", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID taskListUuid;  // UUID pro seznam úkolů

    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;  // Seznam úkolů

    public TaskList() {
        this.taskListUuid = UUID.randomUUID();  // Automaticky generované UUID při vytvoření seznamu
    }

    // Gettery a Settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getTaskListUuid() {
        return taskListUuid;
    }

    public void setTaskListUuid(UUID taskListUuid) {
        this.taskListUuid = taskListUuid;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    
}
