package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

@Entity
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // Zde bude uložen BCrypt hash

    // Propojení: Jeden uživatel má více seznamů úkolů
    @JsonIgnore // Tato řádka zajistí, že se úkoly nebudou balit do balíčku pro Angular
@OneToMany(mappedBy = "user")
    private List<TaskList> taskLists = new ArrayList<>();

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Gettery a Settery ...
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<TaskList> getTaskLists() { return taskLists; }
    public void setTaskLists(List<TaskList> taskLists) { this.taskLists = taskLists; }

}
