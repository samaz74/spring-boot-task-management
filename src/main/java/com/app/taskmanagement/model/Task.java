package com.app.taskmanagement.model;

import com.app.taskmanagement.model.enums.Priority;
import com.app.taskmanagement.model.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    @ManyToOne
    @JoinColumn(name = "CREATED_BY")
    private User createdBy;
    @ManyToOne
    @JoinColumn(name = "ASSIGNED_BY")
    private User assignedBy;
    private LocalDateTime dueDate;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    public Task(String title, String description, TaskStatus status, Priority priority, User createdBy, User assignedBy, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdBy = createdBy;
        this.assignedBy = assignedBy;
        this.dueDate = dueDate;

    }

}
