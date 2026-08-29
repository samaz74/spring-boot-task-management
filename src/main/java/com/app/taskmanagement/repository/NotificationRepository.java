package com.app.taskmanagement.repository;

import com.app.taskmanagement.dto.NotificationResponse;
import com.app.taskmanagement.model.Notification;
import com.app.taskmanagement.model.Task;
import com.app.taskmanagement.model.User;
import com.app.taskmanagement.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findNotificationByUser(User user);

    List<Notification> findNotificationByType(NotificationType type);

    List<Notification> findNotificationByIsReadAndUser(boolean isRead, User user);

    }
