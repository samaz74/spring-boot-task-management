package com.app.taskmanagement.service;

import com.app.taskmanagement.dto.TaskRequest;
import com.app.taskmanagement.dto.TaskResponse;
import com.app.taskmanagement.dto.TaskStatusUpdateRequest;
import com.app.taskmanagement.dto.mapper.TaskMapper;
import com.app.taskmanagement.exception.AccessDeniedException;
import com.app.taskmanagement.exception.InvalidOperationException;
import com.app.taskmanagement.exception.ResourceNotFoundException;
import com.app.taskmanagement.model.Task;
import com.app.taskmanagement.model.enums.Priority;
import com.app.taskmanagement.model.enums.Roles;
import com.app.taskmanagement.model.enums.TaskStatus;
import com.app.taskmanagement.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TasksService {
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final NotificationService notificationService;
    private final TaskRepository taskRepository;
    public TasksService(TaskRepository taskRepository, TaskMapper taskMapper, UserService userService, NotificationService notificationService) {
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
        Task task = taskMapper.toEntity(taskRequest, userService.getUserByEmailEntity(principal.getName()));
        task.setStatus(TaskStatus.CREATED);
        taskRepository.save(task);
        notificationService.createTask(task);
        return taskMapper.toResponse(task);
    }
    @Transactional
    public TaskResponse updateTask(TaskRequest taskRequest, Principal principal,Long id){

        Task task = taskRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Task not found"));
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setPriority(taskRequest.getPriority());
        task.setDueDate(taskRequest.getDueDate());
        task.setAssignedTo(userService.getUserByIdEntity(taskRequest.getAssignedToId()));
        return taskMapper.toResponse(task);

    }

    @Transactional
    public TaskResponse updateTaskStatus(TaskStatusUpdateRequest taskStatusUpdateRequest, Principal principal){
        Task task = taskRepository.findById(taskStatusUpdateRequest.getTaskId()).orElseThrow(()->new ResourceNotFoundException("Task not found"));
        if(userService.getUserByEmailEntity(principal.getName()).equals(task.getCreatedBy())||userService.getUserByEmailEntity(principal.getName()).equals(task.getAssignedTo())||userService.getUserByEmailEntity(principal.getName()).getRole().equals(Roles.ADMIN)){
            switch (taskStatusUpdateRequest.getTaskActions()){
                case START -> {if(task.getStatus() == TaskStatus.CREATED) {task.setStatus(TaskStatus.IN_PROGRESS);}else throw new InvalidOperationException("invalid operation");}
                case COMPLETE ->{
                    if(task.getStatus() == TaskStatus.IN_PROGRESS){
                    task.setStatus(TaskStatus.DONE);}else throw new InvalidOperationException("invalid operation");
                }
                case REOPEN -> {if(task.getStatus() ==TaskStatus.CANCELED){ task.setStatus(TaskStatus.IN_PROGRESS);}else throw new InvalidOperationException("invalid operation");}
                case CANCEL -> {if(task.getStatus() == TaskStatus.CREATED || task.getStatus() ==TaskStatus.IN_PROGRESS){task.setStatus(TaskStatus.CANCELED);}else throw new InvalidOperationException("invalid operation");}
            }
            return taskMapper.toResponse(task);
        }else throw new AccessDeniedException("User not authorized");
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

    public List<TaskResponse> getTaskByAssignedToOrderByCreateUser(Long userId){
        return taskRepository.findTaskByAssignedToOrderByCreatedByDesc(userService.getUserByIdEntity(userId)).stream().map(taskMapper::toResponse).collect(Collectors.toList());
    }
    public Page<TaskResponse> getTasks(Pageable pageable){
        return taskRepository.findAll(pageable).map(taskMapper::toResponse);
    }

}
