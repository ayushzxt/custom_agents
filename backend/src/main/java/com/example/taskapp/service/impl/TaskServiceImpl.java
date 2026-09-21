package com.example.taskapp.service.impl;

import com.example.taskapp.dto.CreateTaskRequest;
import com.example.taskapp.dto.TaskResponse;
import com.example.taskapp.entity.Task;
import com.example.taskapp.exception.ResourceNotFoundException;
import com.example.taskapp.repository.TaskRepository;
import com.example.taskapp.service.TaskService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<TaskResponse> findAll() {
        return taskRepository.findAll().stream().map(TaskResponse::from).toList();
    }

    @Override
    public TaskResponse findById(String id) {
        return taskRepository.findById(id)
                .map(TaskResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
    }

    @Override
    @Transactional
    public TaskResponse create(CreateTaskRequest request) {
        Task task = new Task(request.title(), request.description());
        return TaskResponse.from(taskRepository.save(task));
    }
}
