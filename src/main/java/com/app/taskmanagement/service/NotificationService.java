package com.app.taskmanagement.service;

import com.app.taskmanagement.dto.NotificationResponse;
import com.app.taskmanagement.dto.TaskResponse;
import com.app.taskmanagement.dto.mapper.NotificationMapper;
import com.app.taskmanagement.exception.ResourceNotFoundException;
import com.app.taskmanagement.model.Comment;
import com.app.taskmanagement.model.Notification;
import com.app.taskmanagement.model.Task;
import com.app.taskmanagement.model.enums.NotificationType;
import com.app.taskmanagement.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, TaskService taskService, UserService userService, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.taskService = taskService;
        this.userService = userService;
        this.notificationMapper = notificationMapper;
    }

    public void CreateTask(TaskResponse taskResponse){
        notificationRepository.save(new Notification(taskResponse.getTitle(),false,taskService.getTaskByIdEntity(taskResponse.getId()),userService.getUserByIdEntity(taskResponse.getAssignedToId()), NotificationType.TASK_ASSIGNED));
    }
    public void updateTaskStatus(Task task, Principal principal){
        if (task.getCreatedBy().equals(userService.getUserByEmailEntity(principal.getName())))
            notificationRepository.save(new Notification(task.getTitle(),false,task,task.getAssignedTo(), NotificationType.TASK_UPDATED));
        else
            notificationRepository.save(new Notification(task.getTitle(),false,task,task.getCreatedBy(), NotificationType.TASK_UPDATED));
    }
    public void UpdateTaskReadStatus(Long id){
        Notification notification = notificationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }
    public void deleteNotification(Long id){
        notificationRepository.deleteById(id);
    }
    public List<NotificationResponse> getTasksAssigned(Principal principal){
        return notificationRepository.findNotificationByUser(userService.getUserByEmailEntity(principal.getName())).stream().map(notificationMapper::toNotificationResponse).collect(Collectors.toList());
    }
    public void addComment(Comment comment, Principal principal){
        if(comment.getTask().getCreatedBy().equals(userService.getUserByEmailEntity(principal.getName()))){
            notificationRepository.save(new Notification(comment.getContent(),false,comment.getTask(),comment.getTask().getAssignedTo(), NotificationType.COMMENT_ADDED));
        }else{
            notificationRepository.save(new Notification(comment.getContent(),false,comment.getTask(),comment.getTask().getCreatedBy(), NotificationType.COMMENT_ADDED));
        }
    }
}
