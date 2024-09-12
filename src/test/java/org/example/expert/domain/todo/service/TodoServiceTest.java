package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
    private TodoService todoService;
    @Mock
    private WeatherClient weatherClient;

    @Test
    public void Todo_등록이_정상작동_한다 (){
        //given
        TodoSaveRequest request = new TodoSaveRequest("Title","TitleContents");
        AuthUser authUser = new AuthUser(1L,"aaa@aa.com", UserRole.USER);

        User user = User.fromAuthUser(authUser);
        String weather = weatherClient.getTodayWeather();
        Todo todo = new Todo(request.getTitle(),request.getContents(),weather,user);

        given(todoRepository.save(any())).willReturn(todo);
        //when
        TodoSaveResponse result = todoService.saveTodo(authUser, request);
        //then
        assertNotNull(result);
    }

//    @Test
//    public void Todo_단건조회중_해당todo가_없어서_에러 (){
//        //given
//        long todoId = 1L;
//
//        when(todoRepository.findByIdWithUser(todoId)).thenReturn(Optional.empty());
//        //when
//        assertThrows(InvalidRequestException.class, ()->todoService.getTodo(todoId));
//        //then
//        verify(todoRepository).findByIdWithUser(todoId);
//    }

    @Test
    public void Todo_페이지_레파지토리에서_뽑아오기(){
        //given
        int page = 1;
        int size = 10;
        User user = new User();
        Pageable pageable = PageRequest.of(page,size);
        List<Todo> todos = List.of(new Todo("title","contents","sunny",user));
        Page<Todo> todoss = new PageImpl<>(todos,pageable,1);

        given(todoRepository.findAllByOrderByModifiedAtDesc(any(Pageable.class))).willReturn(todoss);
        //when
        Page<TodoResponse> result = todoService.getTodos(page,size).map(a->new TodoResponse(
                a.getId(),
                a.getTitle(),
                a.getContents(),
                a.getWeather(),
                new UserResponse(a.getUser().getId(), a.getUser().getEmail()),
                a.getCreatedAt(),
                a.getModifiedAt()
        ));
        //then
        assertNotNull(result);
    }

    @Test
    public void Todo_단건조회중_해당todo가_없어서_에러 (){
        //given
        long todoId = 1L;

        given(todoRepository.findByIdWithUser(anyLong())).willReturn(Optional.empty());
        //when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,()->todoService.getTodo(todoId));
        //then
        assertEquals("Todo not found",exception.getMessage());
    }



}
