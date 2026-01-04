package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TaskList;



    public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    TaskList findByTaskListUuid(UUID taskListUuid);
   
  
  
}
    

