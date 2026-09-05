package com.app.taskmanagement;

import com.app.taskmanagement.controller.TaskController;
import com.app.taskmanagement.dto.TaskRequest;
import com.app.taskmanagement.dto.TaskResponse;
import com.app.taskmanagement.exception.GlobalExceptionHandler;
import com.app.taskmanagement.exception.ResourceNotFoundException;
import com.app.taskmanagement.model.enums.TaskStatus;
import com.app.taskmanagement.service.TasksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TasksService tasksService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        TaskController controller = new TaskController(tasksService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void shouldReturnTaskById() throws Exception{
        TaskResponse response = TaskResponse.builder().id(1L).title("Learn MockMvc").status(TaskStatus.CREATED).build();

        when(tasksService.getTaskById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/task/{taskId}",1L))
                        .andExpect(status().isOk()).andExpect(jsonPath("$.id")
                        .value(1)).andExpect(jsonPath("$.title")
                        .value("Learn MockMvc")).andExpect(jsonPath("$.status")
                        .value("CREATED"));
        verify(tasksService).getTaskById(1L);
    }

    @Test
    void getAssignedToTask() throws Exception{
        TaskResponse response = TaskResponse.builder().id(1L).title("Learn MockMvc").status(TaskStatus.CREATED).build();
        List<TaskResponse> responses =new ArrayList<>();
        responses.add(response);

        when(tasksService.getTaskByAssignedToId(7L)).thenReturn(responses);

        mockMvc.perform(get("/api/task/search/assignedTo/{assignedToId}", 7L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Learn MockMvc"))
                .andExpect(jsonPath("$[0].status").value("CREATED"));
        verify(tasksService).getTaskByAssignedToId(7L);
    }

    @Test
    void shouldReturnBadRequestWhenTaskRequestIsInvalid() throws Exception {
        TaskRequest invalidRequest = TaskRequest.builder()
                .title("")
                .description("test")
                .build();
        String requestJson = objectMapper.writeValueAsString(invalidRequest);
        mockMvc.perform(post("/api/task").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isBadRequest());
        verifyNoInteractions(tasksService);

    }
    @Test
    void shouldReturn404WhenTaskDoesNotExist() throws Exception{
        when(tasksService.getTaskById(999L)).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/api/task/{taskId}", 999L)).andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404));
        verify(tasksService).getTaskById(999L);
    }


}
