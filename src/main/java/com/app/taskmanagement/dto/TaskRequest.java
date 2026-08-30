package com.app.taskmanagement.dto;

import com.app.taskmanagement.model.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private Priority priority;
    @NotNull
    private Long assignedToId;
    @NotNull
    private LocalDateTime dueDate;
}
