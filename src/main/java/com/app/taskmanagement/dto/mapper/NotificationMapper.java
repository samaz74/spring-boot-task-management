package com.app.taskmanagement.dto.mapper;

import com.app.taskmanagement.dto.NotificationResponse;
import com.app.taskmanagement.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationResponse toNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .content(notification.getContent())
                .isRead(notification.getIsRead())
                .userId(notification.getUser().getId())
                .userName(notification.getUser().getFName().concat(" " + notification.getUser().getLName()))
                .taskId(notification.getTask().getId())
                .taskTitle(notification.getTask().getTitle())
                .build();
    }

}
