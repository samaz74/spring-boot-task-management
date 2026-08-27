package com.app.taskmanagement.dto.mapper;

import com.app.taskmanagement.dto.TaskRequest;
import com.app.taskmanagement.dto.TaskResponse;
import com.app.taskmanagement.model.Task;
import com.app.taskmanagement.service.UserService;
import org.springframework.stereotype.Component;

import java.security.Principal;
@Component
public class TaskMapper {
    private final UserService userService;

    public TaskMapper(UserService userService) {
        this.userService = userService;
    }

    public Task toEntity(TaskRequest taskRequest, Principal principal) {
        return new Task(
                taskRequest.getTitle(),
                taskRequest.getDescription(),
                taskRequest.getStatus(),
                taskRequest.getPriority(),
                userService.getUserByEmailEntity(principal.getName()),
                userService.getUserByIdEntity(taskRequest.getAssignedToId()),
                taskRequest.getDueDate()
        );
    }
    public TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .CreatedById(task.getCreatedBy().getId())
                .CreatedByName(task.getCreatedBy().getFName().concat(" " + task.getCreatedBy().getLName()))
                .assignedToId(task.getAssignedTo().getId())
                .assignedToName(task.getAssignedTo().getFName().concat(" " + task.getAssignedTo().getLName()))
                .createdDate(task.getCreatedAt())
                .updatedDate(task.getUpdatedAt())
                .build();
    }
}
