package com.example.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.TaskList;



    public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    TaskList findByTaskListUuid(UUID taskListUuid);

    @Query("SELECT tl FROM TaskList tl LEFT JOIN FETCH tl.tasks WHERE tl.taskListUuid = :uuid")
    TaskList findByTaskListUuidWithTasks(@Param("uuid") UUID uuid);
   
  
  
}
    

