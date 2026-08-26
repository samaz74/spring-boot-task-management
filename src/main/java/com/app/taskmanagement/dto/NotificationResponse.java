package com.app.taskmanagement.dto;

import com.app.taskmanagement.model.enums.NotificationType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private String content;
    private Boolean isRead;
    private Long taskId;
    private String taskTitle;
    private Long userId;
    private String userName;
    private NotificationType type;
}
