package com.app.taskmanagement.service;

import com.app.taskmanagement.dto.CommentRequest;
import com.app.taskmanagement.dto.CommentResponse;
import com.app.taskmanagement.dto.mapper.CommentMapper;
import com.app.taskmanagement.model.Comment;
import com.app.taskmanagement.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final NotificationService notificationService;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, UserService userService, NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userService = userService;
        this.notificationService = notificationService;
    }
    public CommentResponse createComment(CommentRequest commentRequest, Principal principal){
        Comment comment = commentRepository.save(commentMapper.toEntity(commentRequest, userService.getUserByEmailEntity(principal.getName())));
        notificationService.addComment(comment, principal);
        return commentMapper.toResponse(comment);
    }

    public CommentResponse getCommentByTaskId(Long taskId){
        Comment comment = commentRepository.findById(taskId).orElseThrow(()-> new RuntimeException("Task not found"));
        return commentMapper.toResponse(comment);
    }
}
