package com.app.taskmanagement.repository;

import com.app.taskmanagement.model.Comment;
import com.app.taskmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findCommentByTask(Task task);
}
