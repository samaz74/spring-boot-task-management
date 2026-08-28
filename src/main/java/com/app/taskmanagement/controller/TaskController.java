package com.app.taskmanagement.controller;

import com.app.taskmanagement.dto.TaskRequest;
import com.app.taskmanagement.dto.TaskResponse;
import com.app.taskmanagement.model.enums.TaskStatus;
import com.app.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{taskId}")
    public TaskResponse getTask(@PathVariable Long taskId){
        return taskService.getTaskById(taskId);
    }
    @PostMapping("")
    public TaskResponse createTask(@Valid @RequestBody TaskRequest task, Principal principal){
        return taskService.createTask(task,principal);
    }
    @PutMapping("/{taskId}")
    public TaskResponse updateTask(@PathVariable Long taskId, @Valid @RequestBody TaskRequest task, Principal principal){
        return taskService.updateTask(task,principal,taskId);
    }
    @GetMapping("/search/assignedTo/{userId}")
    public List<TaskResponse> getAssignedToTask(@PathVariable Long userId){
        return taskService.getTaskByAssignedToId(userId);
    }
    @GetMapping("/search/createdBy/{userId}")
    public List<TaskResponse> getCreatedByTask(@PathVariable Long userId){
        return taskService.getTaskByCreatedUser(userId);
    }
    @GetMapping("/search/assignedToAndState/{state}")
    public List<TaskResponse> getAssignedToAndState(@PathVariable TaskStatus state, Principal principal){
        return taskService.getTaskByStatusForAssignedUser(state,principal);
    }
    @GetMapping("/search/cratedByAndState/{state}")
    public List<TaskResponse> getCreatedByAndState(@PathVariable TaskStatus state, Principal principal){
        return taskService.getTaskByStatusAndCreatedUser (state,principal);
    }



}
