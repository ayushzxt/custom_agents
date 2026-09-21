package com.example.taskapp.service;

import com.example.taskapp.dto.CreateTaskRequest;
import com.example.taskapp.dto.TaskResponse;
import java.util.List;

public interface TaskService {

    List<TaskResponse> findAll();

    TaskResponse findById(String id);

    TaskResponse create(CreateTaskRequest request);
}
