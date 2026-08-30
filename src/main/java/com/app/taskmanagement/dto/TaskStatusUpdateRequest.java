package com.app.taskmanagement.dto;

import com.app.taskmanagement.model.enums.TaskActions;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusUpdateRequest {
    @NotNull
    Long taskId;
    @NotNull
    TaskActions taskActions;
}
