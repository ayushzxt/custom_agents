package com.example.taskapp.repository;

import com.example.taskapp.entity.Task;
import com.example.taskapp.entity.TaskStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findByStatus(TaskStatus status);
}
