package com.app.taskmanagement.repository;

import com.app.taskmanagement.model.Task;
import com.app.taskmanagement.model.User;
import com.app.taskmanagement.model.enums.Priority;
import com.app.taskmanagement.model.enums.TaskStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findTaskByCreatedBy(User createdBy);

    List<Task> findTaskByAssignedTo(User assignedTo);
    @Query(""" 
        select t from Task t join fetch t.createdBy join fetch t.assignedTo where t.priority = :priority""")
    List<Task> findTaskByPriority(Priority priority);

    List<Task> findTaskByStatusAndAssignedTo(TaskStatus status, User assignedTo);

    List<Task> findTaskByStatusAndCreatedBy(TaskStatus status, User CreatedBy);
}
