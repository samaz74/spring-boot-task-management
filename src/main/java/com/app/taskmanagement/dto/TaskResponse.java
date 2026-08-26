package com.app.taskmanagement.dto;

import com.app.taskmanagement.model.enums.Priority;
import com.app.taskmanagement.model.enums.TaskStatus;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private Long CreatedById;
    private String CreatedByName;
    private Long assignedToId;
    private String assignedToName;
    private LocalDateTime dueDate;
}
