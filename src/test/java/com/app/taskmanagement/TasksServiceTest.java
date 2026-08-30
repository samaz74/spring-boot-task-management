package com.app.taskmanagement;

import com.app.taskmanagement.dto.TaskStatusUpdateRequest;
import com.app.taskmanagement.dto.mapper.TaskMapper;
import com.app.taskmanagement.exception.AccessDeniedException;
import com.app.taskmanagement.exception.InvalidOperationException;
import com.app.taskmanagement.model.Task;
import com.app.taskmanagement.model.User;
import com.app.taskmanagement.model.enums.Priority;
import com.app.taskmanagement.model.enums.Roles;
import com.app.taskmanagement.model.enums.TaskActions;
import com.app.taskmanagement.model.enums.TaskStatus;
import com.app.taskmanagement.repository.TaskRepository;
import com.app.taskmanagement.service.NotificationService;
import com.app.taskmanagement.service.TasksService;
import com.app.taskmanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TasksServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private Principal principal;
    @Mock
    private UserService userService;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private TasksService tasksService;

    @Test
    public void testUpdateTaskStatusWithRightData1(){
        User user = new User();
        user.setId(1L);
        user.setFName("Test");
        user.setLName("Test");
        user.setEmail("Test@test.com");
        user.setPassword("Test");
        user.setRole(Roles.USER);
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task Title");
        task.setDescription("Task Description");
        task.setPriority(Priority.HIGH);
        task.setCreatedBy(user);
        task.setStatus(TaskStatus.CREATED);
        task.setAssignedTo(user);
        TaskStatusUpdateRequest taskStatusUpdateRequest = new TaskStatusUpdateRequest(1L, TaskActions.START);



        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        when(userService.getUserByEmailEntity(any())).thenReturn(user);
        when(principal.getName()).thenReturn("Test@test.com");

        tasksService.updateTaskStatus(taskStatusUpdateRequest, principal);

        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    public void testUpdateTaskStatusWithRightData2(){
        User user = new User();
        user.setId(1L);
        user.setFName("Test");
        user.setLName("Test");
        user.setEmail("Test@test.com");
        user.setPassword("Test");
        user.setRole(Roles.USER);
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task Title");
        task.setDescription("Task Description");
        task.setPriority(Priority.HIGH);
        task.setCreatedBy(user);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setAssignedTo(user);
        TaskStatusUpdateRequest taskStatusUpdateRequest = new TaskStatusUpdateRequest(1L, TaskActions.COMPLETE);



        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        when(userService.getUserByEmailEntity(any())).thenReturn(user);
        when(principal.getName()).thenReturn("Test@test.com");

        tasksService.updateTaskStatus(taskStatusUpdateRequest, principal);

        assertThat(task.getStatus()).isEqualTo(TaskStatus.DONE);
    }

    @Test
    public void testUpdateTaskStatusWithException1(){
        User user = new User();
        user.setId(1L);
        user.setFName("Test");
        user.setLName("Test");
        user.setEmail("Test@test.com");
        user.setPassword("Test");
        user.setRole(Roles.USER);
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task Title");
        task.setDescription("Task Description");
        task.setPriority(Priority.HIGH);
        task.setCreatedBy(user);
        task.setStatus(TaskStatus.CREATED);
        task.setAssignedTo(user);
        TaskStatusUpdateRequest taskStatusUpdateRequest = new TaskStatusUpdateRequest(1L, TaskActions.COMPLETE);



        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        when(userService.getUserByEmailEntity(any())).thenReturn(user);
        when(principal.getName()).thenReturn("Test@test.com");


        assertThatThrownBy(()->tasksService.updateTaskStatus(taskStatusUpdateRequest, principal)).isInstanceOf(InvalidOperationException.class);
    }

    @Test
    public void testUpdateTaskStatusWithRightData3(){
        User user = new User();
        user.setId(1L);
        user.setFName("Test");
        user.setLName("Test");
        user.setEmail("Test@test.com");
        user.setPassword("Test");
        user.setRole(Roles.USER);
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task Title");
        task.setDescription("Task Description");
        task.setPriority(Priority.HIGH);
        task.setCreatedBy(user);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setAssignedTo(user);
        TaskStatusUpdateRequest taskStatusUpdateRequest = new TaskStatusUpdateRequest(1L, TaskActions.CANCEL);



        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        when(userService.getUserByEmailEntity(any())).thenReturn(user);
        when(principal.getName()).thenReturn("Test@test.com");

        tasksService.updateTaskStatus(taskStatusUpdateRequest, principal);

        assertThat(task.getStatus()).isEqualTo(TaskStatus.CANCELED);
    }
    @Test
    public void testUpdateTaskStatusWithRightData4(){
        User user = new User();
        user.setId(1L);
        user.setFName("Test");
        user.setLName("Test");
        user.setEmail("Test@test.com");
        user.setPassword("Test");
        user.setRole(Roles.USER);
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task Title");
        task.setDescription("Task Description");
        task.setPriority(Priority.HIGH);
        task.setCreatedBy(user);
        task.setStatus(TaskStatus.CANCELED);
        task.setAssignedTo(user);
        TaskStatusUpdateRequest taskStatusUpdateRequest = new TaskStatusUpdateRequest(1L, TaskActions.REOPEN);



        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        when(userService.getUserByEmailEntity(any())).thenReturn(user);
        when(principal.getName()).thenReturn("Test@test.com");

        tasksService.updateTaskStatus(taskStatusUpdateRequest, principal);

        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    public void testUpdateTaskStatusWithNotHaveAccessUser(){
        User user = new User();
        user.setId(1L);
        user.setFName("Test");
        user.setLName("Test");
        user.setEmail("Test@test.com");
        user.setPassword("Test");
        user.setRole(Roles.USER);
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task Title");
        task.setDescription("Task Description");
        task.setPriority(Priority.HIGH);
        task.setCreatedBy(user);
        task.setStatus(TaskStatus.CANCELED);
        task.setAssignedTo(user);
        TaskStatusUpdateRequest taskStatusUpdateRequest = new TaskStatusUpdateRequest(1L, TaskActions.REOPEN);
        User user2 = new User();
        user2.setId(2L);
        user2.setFName("Test2");
        user2.setLName("Test2");
        user2.setEmail("Test2@test.com");
        user2.setPassword("Test2");
        user2.setRole(Roles.USER);



        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        when(userService.getUserByEmailEntity(any())).thenReturn(user2);
        when(principal.getName()).thenReturn("Test2@test.com");

        assertThatThrownBy(()->tasksService.updateTaskStatus(taskStatusUpdateRequest, principal)).isInstanceOf(AccessDeniedException.class);
    }

}


