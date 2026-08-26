package com.app.taskmanagement.repository;

import com.app.taskmanagement.model.Notification;
import com.app.taskmanagement.model.Task;
import com.app.taskmanagement.model.User;
import com.app.taskmanagement.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findNotificationByTask(Task task);

    List<Notification> findNotificationByUser(User user);

    List<Notification> findNotificationByType(NotificationType type);
    
}
