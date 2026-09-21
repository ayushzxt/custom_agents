package com.example.taskapp.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.taskapp.dto.CreateTaskRequest;
import com.example.taskapp.dto.TaskResponse;
import com.example.taskapp.entity.Task;
import com.example.taskapp.exception.ResourceNotFoundException;
import com.example.taskapp.repository.TaskRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskServiceImpl taskService;

    private TaskServiceImpl service() {
        return new TaskServiceImpl(taskRepository);
    }

    @Test
    void findById_returnsTask_whenTaskExists() {
        taskService = service();
        Task task = new Task("Write docs", "Document the API");
        when(taskRepository.findById("1")).thenReturn(Optional.of(task));

        TaskResponse result = taskService.findById("1");

        assertThat(result.title()).isEqualTo("Write docs");
    }

    @Test
    void findById_throwsResourceNotFoundException_whenTaskMissing() {
        taskService = service();
        when(taskRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById("missing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("missing");
    }

    @Test
    void create_savesTaskWithOpenStatus() {
        taskService = service();
        CreateTaskRequest request = new CreateTaskRequest("New task", "details");
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse result = taskService.create(request);

        assertThat(result.title()).isEqualTo("New task");
        assertThat(result.status().name()).isEqualTo("OPEN");
        verify(taskRepository).save(any(Task.class));
    }
}
