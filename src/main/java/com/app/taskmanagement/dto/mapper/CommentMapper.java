package com.app.taskmanagement.dto.mapper;

import com.app.taskmanagement.dto.CommentRequest;
import com.app.taskmanagement.dto.CommentResponse;
import com.app.taskmanagement.model.Comment;
import com.app.taskmanagement.model.User;
import com.app.taskmanagement.service.TaskService;
import com.app.taskmanagement.service.UserService;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class CommentMapper {
    private final TaskService taskService;
    private final UserService userService;

    public CommentMapper(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    public Comment toEntity(CommentRequest commentRequest, User user) {
        return new Comment(
                commentRequest.getContent(),
                taskService.getTaskByIdEntity(commentRequest.getTaskId()),
                user
        );
    }
    public CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder().
                id(comment.getId())
                .content(comment.getContent())
                .taskId(comment.getTask().getId())
                .taskTitle(comment.getTask().getTitle())
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getFName().concat(" " + comment.getUser().getLName()))
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
