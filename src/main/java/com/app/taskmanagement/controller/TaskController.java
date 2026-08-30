package com.app.taskmanagement.controller;

import com.app.taskmanagement.dto.TaskRequest;
import com.app.taskmanagement.dto.TaskResponse;
import com.app.taskmanagement.dto.TaskStatusUpdateRequest;
import com.app.taskmanagement.model.enums.TaskStatus;
import com.app.taskmanagement.service.TasksService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TasksService tasksService;

    public TaskController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @GetMapping("/{taskId}")
    public TaskResponse getTask(@PathVariable Long taskId){
        return tasksService.getTaskById(taskId);
    }
    @PostMapping("")
    public TaskResponse createTask(@Valid @RequestBody TaskRequest task, Principal principal){
        return tasksService.createTask(task,principal);
    }
    @PutMapping("/{taskId}")
    public TaskResponse updateTask(@PathVariable Long taskId, @Valid @RequestBody TaskRequest task, Principal principal){
        return tasksService.updateTask(task,principal,taskId);
    }
    @GetMapping("/search/assignedTo/{userId}")
    public List<TaskResponse> getAssignedToTask(@PathVariable Long userId){
        return tasksService.getTaskByAssignedToId(userId);
    }
    @GetMapping("/search/createdBy/{userId}")
    public List<TaskResponse> getCreatedByTask(@PathVariable Long userId){
        return tasksService.getTaskByCreatedUser(userId);
    }
    @GetMapping("/search/assignedToAndState/{state}")
    public List<TaskResponse> getAssignedToAndState(@PathVariable TaskStatus state, Principal principal){
        return tasksService.getTaskByStatusForAssignedUser(state,principal);
    }
    @GetMapping("/search/createdByAndState/{state}")
    public List<TaskResponse> getCreatedByAndState(@PathVariable TaskStatus state, Principal principal ){
        return tasksService.getTaskByStatusAndCreatedUser (state,principal);
    }
    @PatchMapping("/update/status")
    public TaskResponse updateTaskStatus(@Valid @RequestBody TaskStatusUpdateRequest taskStatusUpdateRequest, Principal principal){
        return tasksService.updateTaskStatus(taskStatusUpdateRequest,principal);
    }



}
