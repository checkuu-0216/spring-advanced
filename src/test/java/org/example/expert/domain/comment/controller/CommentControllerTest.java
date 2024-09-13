package org.example.expert.domain.comment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Autowired
    private CommentController commentController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    public void 댓글_저장_컨트롤러 () throws Exception {
        //given
        AuthUser authUser = new AuthUser(1L, "aaa@aa.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        UserResponse userResponse = new UserResponse(user.getId(),user.getEmail());
        long todoId = 1L;
        CommentSaveResponse commentSaveResponse = new CommentSaveResponse(1L,"contents",userResponse);
        CommentSaveRequest commentSaveRequest = new CommentSaveRequest("contents");

        given(commentService.saveComment(any(),anyLong(),any(commentSaveRequest.getClass()))).willReturn(commentSaveResponse);

        String commentInfo = objectMapper.writeValueAsString(commentSaveRequest);

        //when&then
        mockMvc.perform(post("/todos/{todoId}/comments",1L)
                .content(commentInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk());
    }

    @Test
    public void 댓글_조회_컨트롤러_확인 () throws Exception {
        //given
        AuthUser authUser = new AuthUser(1L, "aaa@aa.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        UserResponse userResponse = new UserResponse(user.getId(),user.getEmail());
        CommentResponse commentResponse1 = new CommentResponse(1L,"contents",userResponse);
        CommentResponse commentResponse2 = new CommentResponse(2L,"contents",userResponse);

        List<CommentResponse> commentResponse = Arrays.asList(commentResponse1,commentResponse2);

        long todoId = 1L;

        given(commentService.getComments(todoId)).willReturn(commentResponse);

        //when then
        mockMvc.perform(get("/todos/{todoId}/comments",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

}
