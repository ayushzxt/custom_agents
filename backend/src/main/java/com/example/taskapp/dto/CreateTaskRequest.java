package com.example.taskapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
        @NotBlank(message = "title is required") @Size(max = 200) String title,
        @Size(max = 2000) String description) {
}
