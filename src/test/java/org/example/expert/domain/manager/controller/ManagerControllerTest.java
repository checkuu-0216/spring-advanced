package org.example.expert.domain.manager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.auth.service.AuthService;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.service.ManagerService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ManagerController.class)
public class ManagerControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ManagerService managerService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private AuthService authService;

    @Autowired
    private ManagerController managerController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(managerController).build();
    }

    @Test
    public void Manager_등록_작동() throws Exception {
        AuthUser authUser = new AuthUser(1L, "aaa@aa.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        UserResponse userResponse = new UserResponse(user.getId(), user.getEmail());
        ManagerSaveResponse managerSaveResponse = new ManagerSaveResponse(1L, userResponse);
        ManagerSaveRequest managerSaveRequest = new ManagerSaveRequest(user.getId());
        long todoId = 1L;
        given(managerService.saveManager(authUser, todoId, managerSaveRequest)).willReturn(managerSaveResponse);
        String managerInfo = objectMapper.writeValueAsString(managerSaveRequest);

        mockMvc.perform(post("/todos/{todoId}/managers", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer test-jwt-token")
                .content(managerInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

    }
}
