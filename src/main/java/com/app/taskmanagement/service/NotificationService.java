package com.app.taskmanagement.service;

import com.app.taskmanagement.dto.NotificationResponse;
import com.app.taskmanagement.dto.TaskResponse;
import com.app.taskmanagement.dto.mapper.NotificationMapper;
import com.app.taskmanagement.exception.AccessDeniedException;
import com.app.taskmanagement.exception.ResourceNotFoundException;
import com.app.taskmanagement.model.Comment;
import com.app.taskmanagement.model.Notification;
import com.app.taskmanagement.model.Task;
import com.app.taskmanagement.model.enums.NotificationType;
import com.app.taskmanagement.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final NotificationMapper notificationMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationService(NotificationRepository notificationRepository, UserService userService, NotificationMapper notificationMapper,SimpMessagingTemplate simpMessagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.notificationMapper = notificationMapper;
        this.simpMessagingTemplate=simpMessagingTemplate;
    }

    public void createTask(Task task){
        Notification notification = notificationRepository.save(new Notification(task.getTitle(),false,task,userService.getUserByIdEntity(task.getAssignedTo().getId()), NotificationType.TASK_ASSIGNED));
        simpMessagingTemplate.convertAndSend("/topic/notifications/" + task.getAssignedTo().getId(), notificationMapper.toNotificationResponse(notification));
    }
    public void updateTaskStatus(Task task, Principal principal){
        if (task.getCreatedBy().equals(userService.getUserByEmailEntity(principal.getName()))) {
            Notification notification = notificationRepository.save(new Notification(task.getTitle(), false, task, task.getAssignedTo(), NotificationType.TASK_UPDATED));
            simpMessagingTemplate.convertAndSend("/topic/notifications/" + task.getAssignedTo().getId(), notificationMapper.toNotificationResponse(notification));

        }else{
            Notification notification = notificationRepository.save(new Notification(task.getTitle(),false,task,task.getCreatedBy(), NotificationType.TASK_UPDATED));
            simpMessagingTemplate.convertAndSend("/topic/notifications/" + task.getCreatedBy().getId(), notificationMapper.toNotificationResponse(notification));

        }
    }
    public void updateTaskReadStatus(Long id,Principal principal){
        Notification notification = notificationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Notification not found"));
        if(notification.getUser().equals(userService.getUserByEmailEntity(principal.getName()))){
        notification.setIsRead(true);
        notificationRepository.save(notification);
        }else throw new AccessDeniedException("Access denied");
    }
    public void deleteNotification(Long id , Principal principal){
        Notification notification = notificationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Notification not found"));
        if(notification.getUser().equals(userService.getUserByEmailEntity(principal.getName()))){
            notificationRepository.deleteById(id);
        }else throw new AccessDeniedException("Access denied");
    }

    public void addComment(Comment comment, Principal principal){
        if(comment.getTask().getCreatedBy().equals(userService.getUserByEmailEntity(principal.getName()))){
            Notification notification = notificationRepository.save(new Notification(comment.getContent(),false,comment.getTask(),comment.getTask().getAssignedTo(), NotificationType.COMMENT_ADDED));
            simpMessagingTemplate.convertAndSend("/topic/notifications/" + comment.getTask().getAssignedTo().getId(), notificationMapper.toNotificationResponse(notification));

        }else{
            Notification notification = notificationRepository.save(new Notification(comment.getContent(),false,comment.getTask(),comment.getTask().getCreatedBy(), NotificationType.COMMENT_ADDED));
            simpMessagingTemplate.convertAndSend("/topic/notifications/" + comment.getTask().getCreatedBy().getId(), notificationMapper.toNotificationResponse(notification));

        }
    }
    public List<NotificationResponse> getUserNotifications(Principal principal){
        return notificationRepository.findNotificationByUser(userService.getUserByEmailEntity(principal.getName())).stream().map(notificationMapper::toNotificationResponse).collect(Collectors.toList());
    }
}
