package com.app.taskmanagement.service;

import com.app.taskmanagement.dto.TaskRequest;
import com.app.taskmanagement.exception.ResourceNotFoundException;
import com.app.taskmanagement.model.Task;
import com.app.taskmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    public Task getTaskByIdEntity(Long id){
        return taskRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Task not found"));
    }
}
