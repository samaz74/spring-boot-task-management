package com.app.taskmanagement.controller;

import com.app.taskmanagement.dto.CommentRequest;
import com.app.taskmanagement.dto.CommentResponse;
import com.app.taskmanagement.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{taskId}")
    public List<CommentResponse> getComments(@PathVariable Long taskId){
        return commentService.getCommentByTaskId(taskId);
    }
    @PostMapping("")
    public CommentResponse addComment(@Valid @RequestBody CommentRequest commentRequest, Principal principal){
        return commentService.createComment(commentRequest, principal);
    }


}
