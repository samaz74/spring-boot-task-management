package com.app.taskmanagement.controller;

import com.app.taskmanagement.dto.NotificationResponse;
import com.app.taskmanagement.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {


    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/")
    public List<NotificationResponse> getNotification(Principal principal){
        return notificationService.getUserNotifications(principal);
    }
    @PatchMapping("/{notificationId}")
    public void readNotifications(@PathVariable Long notificationId, Principal principal){
        notificationService.updateTaskReadStatus(notificationId,principal);
    }
    @DeleteMapping("/{notificationId}")
    public void deleteNotification(@PathVariable Long notificationId,Principal principal){
        notificationService.deleteNotification(notificationId,principal);
    }
}
