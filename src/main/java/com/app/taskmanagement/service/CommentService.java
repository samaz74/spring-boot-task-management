package com.app.taskmanagement.service;

import com.app.taskmanagement.dto.CommentRequest;
import com.app.taskmanagement.dto.CommentResponse;
import com.app.taskmanagement.dto.mapper.CommentMapper;
import com.app.taskmanagement.model.Comment;
import com.app.taskmanagement.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final NotificationService notificationService;
    private final TasksService tasksService;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, UserService userService, NotificationService notificationService, TasksService tasksService) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userService = userService;
        this.notificationService = notificationService;
        this.tasksService = tasksService;
    }
    public CommentResponse createComment(CommentRequest commentRequest, Principal principal){
        Comment comment = commentRepository.save(commentMapper.toEntity(commentRequest, userService.getUserByEmailEntity(principal.getName())));
        notificationService.addComment(comment, principal);
        return commentMapper.toResponse(comment);
    }

    public List<CommentResponse> getCommentByTaskId(Long taskId){
        return commentRepository.findCommentByTask(tasksService.getTaskByIdEntity(taskId)).stream().map(commentMapper::toResponse).collect(Collectors.toList());
    }
}
