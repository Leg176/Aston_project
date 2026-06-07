package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.assembler.UserAssembler;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.dto.UserResponseDto;
import ru.practicum.dto.UserUpdateDto;
import ru.practicum.entity.User;
import ru.practicum.exception.NotFoundException;
import ru.practicum.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User user1;
    private User user2;
    private UserRequestDto request;
    private UserResponseDto dto1;
    private UserResponseDto dto2;

    @BeforeEach
    void setUp() {

        LocalDateTime date = LocalDateTime.of(2026, 5, 15, 12, 0, 0);
        LocalDateTime date1 = LocalDateTime.of(2026, 2, 11, 11, 0, 0);

        user1 = new User(1L, "Alex", "alex@test.com", 25, date1);
        user2 = new User(2L, "Igor", "test@test.com", 23, date);
        dto1 = new UserResponseDto(1L, "Alex", "alex@test.com", 25, date1);
        dto2 = new UserResponseDto(2L, "Igor", "test@test.com", 23, date);

        request = new UserRequestDto("Igor", "test@test.com", 23);
    }

    @Test
    void getAllUsers_returnsList() throws Exception {

        List<User> response = List.of(user1, user2);
        EntityModel<UserResponseDto> model1 = EntityModel.of(dto1);
        EntityModel<UserResponseDto> model2 = EntityModel.of(dto2);

        when(userService.findAll()).thenReturn(response);
        when(assembler.toModel(user1)).thenReturn(model1);
        when(assembler.toModel(user2)).thenReturn(model2);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userResponseDtoList.length()").value(2))
                .andExpect(jsonPath("$._embedded.userResponseDtoList[0].name").value("Alex"));
        verify(userService, times(1)).findAll();
    }

    @Test
    void getUserById_returnsUser() throws Exception {

        when(userService.getUser(1L)).thenReturn(user1);
        when(assembler.toModel(user1)).thenReturn(EntityModel.of(dto1));

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("alex@test.com"));

        verify(userService).getUser(1L);
        verify(assembler).toModel(user1);
    }


    @Test
    void createUser_returnsCreatedUser() throws Exception {

        when(userService.save(any())).thenReturn(user2);
        when(assembler.toModel(user2)).thenReturn(EntityModel.of(dto2));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Igor"));

        verify(userService).save(any());
    }

    @Test
    void updateUser_returnsUpdatedUser() throws Exception {
        UserUpdateDto requestUpdate = new UserUpdateDto(1L, "Alex", "alex@test.com", 25);

        when(userService.update(any())).thenReturn(user1);
        when(assembler.toModel(user1)).thenReturn(EntityModel.of(dto1));

        mockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alex"));

        verify(userService).update(any());
    }

    @Test
    void deleteUser_returnsOk() throws Exception {

        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isOk());

        verify(userService).deleteUser(1L);
    }

    @Test
    void createUser_invalidEmail_returnsBadRequest() throws Exception {
        UserRequestDto badRequest = new UserRequestDto("Igor",  "invalid-email", 23);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUser_invalidId_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/users/-1")).andExpect(status().isBadRequest());
    }

    @Test
    void getUser_notFound_returns404() throws Exception {
        when(userService.getUser(99L)).thenThrow(new NotFoundException("Пользователь не найден"));

        mockMvc.perform(get("/api/v1/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Пользователь не найден"));
    }
}
