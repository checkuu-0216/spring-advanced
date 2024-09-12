package org.example.expert.domain.auth.service;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    public void 회원가입이_정상작동_한다(){
        //given
        SignupRequest signupRequest = new SignupRequest("aa@aa.com","1234","USER");
        String encode = passwordEncoder.encode("1234");

        UserRole userRole =  UserRole.USER;

        User newUser = new User(signupRequest.getEmail(),encode,userRole);

        given(userRepository.save(any(User.class))).willReturn(newUser);
        //when
        SignupResponse result = authService.signup(signupRequest);
        //then
        assertNotNull(result);
    }

    @Test
    public void 로그인_정상작동_테스트(){

    }


}
