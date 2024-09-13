package org.example.expert.domain.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.hibernate.annotations.ManyToAny;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
public class TodoControllerTest extends Timestamped {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private TodoService todoService;

    @Autowired
    private TodoController todoController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }

    @Test
    public void 할일_저장_controller() throws Exception {
        AuthUser authUser = new AuthUser(1L, "aaa@aa.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        UserResponse userResponse = new UserResponse(user.getId(), user.getEmail());
        TodoSaveResponse todoSaveResponse = new TodoSaveResponse(1L, "title", "contents", "sunny", userResponse);
        TodoSaveRequest todoSaveRequest = new TodoSaveRequest("title", "contents");

        given(todoService.saveTodo(any(), any())).willReturn(todoSaveResponse);

        String todoInfo = objectMapper.writeValueAsString(todoSaveRequest);

        mockMvc.perform(post("/todos")
                .content(todoInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

//    @Test
//    public void 할일_조회_controller () throws Exception {
//        int page = 1;
//        int size = 10;
//        AuthUser authUser = new AuthUser(1L, "aaa@aa.com", UserRole.USER);
//        User user = User.fromAuthUser(authUser);
//        UserResponse userResponse = new UserResponse(
//                user.getId(),
//                user.getEmail());
//        TodoResponse todoResponse = new TodoResponse(
//                1L,
//                "title",
//                "cotents",
//                "sunny",
//                userResponse,
//                getCreatedAt(),
//                getModifiedAt());
//        Page<TodoResponse> todoPage = new PageImpl<>(Collections.singletonList(todoResponse));
//        given(todoService.getTodos(1,10)).willReturn(todoPage);
//
//        mockMvc.perform(get("/todos")
//                .param("page", String.valueOf(1))
//                .param("size", String.valueOf(10))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }

    @Test
    public void 할일_단건_조회()  throws Exception {
        //given
        AuthUser authUser = new AuthUser(1L, "aaa@aa.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        UserResponse userResponse = new UserResponse(user.getId(), user.getEmail());
        TodoResponse todoResponse = new TodoResponse(1L,"title","contents","sunny",userResponse,LocalDateTime.now(),LocalDateTime.now());
        long todoId = 1L;
        given(todoService.getTodo(todoId)).willReturn(todoResponse);
        //when then
        mockMvc.perform(get("/todos/{todoId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}
