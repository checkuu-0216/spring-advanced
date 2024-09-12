package org.example.expert.aop;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class AspectTest {
// 범위 기반 연습
//    @Pointcut("execution(* org.example.expert.domain.comment.controller.CommentAdminController.*(..))")
//    private void comment() {}
//
//    @Pointcut("execution(* org.example.expert.domain.user.controller.UserAdminController.*(..))")
//    private void user() {}

    @Pointcut("@annotation(org.example.expert.domain.common.annotation.LogRecord)")
    private void LogAnnotation() {
    }

    //Around로 구현했다가 Before로 해도 될거같아서 수정
    @Before("LogAnnotation()")
    public void beforeMethod() {

        //유저 아이디와 url
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("Request userId : {}", request.getAttribute("userId"));
        log.info("Request URL : {} {}", request.getMethod(), request.getRequestURL());

        // 측정 시간
        LocalDateTime startTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedStartTime = startTime.format(formatter);
        log.info("startTime : {}", formattedStartTime);

    }
}