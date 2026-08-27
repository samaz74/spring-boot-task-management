package com.app.taskmanagement.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
    private String content;
    private Long taskId;
}
