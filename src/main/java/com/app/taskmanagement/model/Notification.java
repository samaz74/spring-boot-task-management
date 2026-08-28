package com.app.taskmanagement.model;

import com.app.taskmanagement.model.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Boolean isRead;
    @ManyToOne
    @JoinColumn(name = "TASK_ID")
    private Task task;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    @CreationTimestamp
    private LocalDateTime created;

    public Notification(String content, Boolean isRead, Task task, User user, NotificationType type) {
        this.content = content;
        this.isRead = isRead;
        this.task = task;
        this.user = user;
        this.type = type;
    }

}
