package com.app.taskmanagement.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private Long taskId;
    private String taskTitle;
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;
}
