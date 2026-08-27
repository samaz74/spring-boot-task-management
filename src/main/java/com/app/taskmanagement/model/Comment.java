package com.app.taskmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    @JoinColumn(name = "TASK_ID")
    private Task task;
    @ManyToOne
    @JoinColumn(name = "CREATEDBY_ID")
    private User user;
    @CreationTimestamp
    private LocalDateTime createdAt;
    public Comment(String content, Task task, User user) {
        this.content = content;
        this.task = task;
        this.user =user;
    }
}
