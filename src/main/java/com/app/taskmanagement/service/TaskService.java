package com.app.taskmanagement.service;

import com.app.taskmanagement.dto.TaskRequest;
import com.app.taskmanagement.dto.TaskResponse;
import com.app.taskmanagement.dto.mapper.TaskMapper;
import com.app.taskmanagement.exception.ResourceNotFoundException;
import com.app.taskmanagement.model.Task;
import com.app.taskmanagement.model.enums.Priority;
import com.app.taskmanagement.model.enums.TaskStatus;
import com.app.taskmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final NotificationService notificationService;
    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, UserService userService, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userService = userService;
        this.notificationService = notificationService;
    }
    public Task getTaskByIdEntity(Long id){
        return taskRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Task not found"));
    }
    public TaskResponse getTaskById(Long id){
        return taskRepository.findById(id).map(taskMapper::toResponse).orElseThrow(()->new ResourceNotFoundException("Task not found"));
    }

    public TaskResponse createTask(TaskRequest taskRequest, Principal principal){
        Task task = taskRepository.save(taskMapper.toEntity(taskRequest, userService.getUserByEmailEntity(principal.getName())));
        notificationService.CreateTask(task);
        return taskMapper.toResponse(task);
    }
    public TaskResponse updateTask(TaskRequest taskRequest, Principal principal,Long id){
        Task task = taskMapper.toEntity(taskRequest, userService.getUserByEmailEntity(principal.getName()));
        task.setId(id);
        taskRepository.save(task);
        notificationService.updateTaskStatus(task,principal);
        return taskMapper.toResponse(task);
    }


    public List<TaskResponse> getTaskByAssignedToId(Long id){
        return taskRepository.findTaskByAssignedTo(userService.getUserByIdEntity(id)).stream().map(taskMapper::toResponse).collect(Collectors.toList());
    }
    public List<TaskResponse> getTaskByPriority(Priority priority){
        return taskRepository.findTaskByPriority(priority).stream().map(taskMapper::toResponse).collect(Collectors.toList());
    }
    public List<TaskResponse> getTaskByCreatedUser(Long id){
        return taskRepository.findTaskByCreatedBy(userService.getUserByIdEntity(id)).stream().map(taskMapper::toResponse).collect(Collectors.toList());
    }

    public List<TaskResponse> getTaskByStatusForAssignedUser(TaskStatus status, Principal principal){
        return taskRepository.findTaskByStatusAndAssignedTo(status, userService.getUserByEmailEntity(principal.getName())).stream().map(taskMapper::toResponse).collect(Collectors.toList());
    }

    public List<TaskResponse> getTaskByStatusAndCreatedUser(TaskStatus status, Principal principal){
        return taskRepository.findTaskByStatusAndCreatedBy(status, userService.getUserByEmailEntity(principal.getName())).stream().map(taskMapper::toResponse).collect(Collectors.toList());
    }

}
