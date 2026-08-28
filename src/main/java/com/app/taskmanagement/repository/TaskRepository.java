package com.app.taskmanagement.repository;

import com.app.taskmanagement.model.Task;
import com.app.taskmanagement.model.User;
import com.app.taskmanagement.model.enums.Priority;
import com.app.taskmanagement.model.enums.TaskStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findTaskByCreatedBy(User createdBy);

    List<Task> findTaskByAssignedTo(User assignedTo);

    List<Task> findTaskByPriority(Priority priority);

    List<Task> findTaskByStatusAndAssignedTo(TaskStatus status, User assignedTo);

    List<Task> findTaskByStatusAndCreatedBy(TaskStatus status, User CreatedBy);
}
