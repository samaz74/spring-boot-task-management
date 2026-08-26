package com.app.taskmanagement.dto;

import com.app.taskmanagement.model.enums.Priority;
import com.app.taskmanagement.model.enums.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskRequest {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private Long CreatedById;
    private Long assignedToId;
    private LocalDateTime dueDate;
}
