package com.app.taskmanagement.dto.mapper;

import com.app.taskmanagement.dto.CommentRequest;
import com.app.taskmanagement.dto.CommentResponse;
import com.app.taskmanagement.model.Comment;
import com.app.taskmanagement.model.User;
import com.app.taskmanagement.service.TasksService;
import com.app.taskmanagement.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    private final TasksService tasksService;
    private final UserService userService;

    public CommentMapper(TasksService tasksService, UserService userService) {
        this.tasksService = tasksService;
        this.userService = userService;
    }

    public Comment toEntity(CommentRequest commentRequest, User user) {
        return new Comment(
                commentRequest.getContent(),
                tasksService.getTaskByIdEntity(commentRequest.getTaskId()),
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
