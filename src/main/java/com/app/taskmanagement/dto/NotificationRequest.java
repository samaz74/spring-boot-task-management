package com.app.taskmanagement.dto;

import com.app.taskmanagement.model.enums.NotificationType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {
    private String content;
    private Boolean isRead;
    private Long taskId;
    private Long userID;
    private NotificationType notificationType;
}
