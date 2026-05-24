package ru.practicum.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repository.UserRepository;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.dto.UserResponseDto;
import ru.practicum.dto.UserUpdateDto;
import ru.practicum.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl service;

    private UserRequestDto requestDto;
    private UserUpdateDto updateDto;
    private UserResponseDto responseDto;
    private UserResponseDto responseDto1;
    private User savedUser;
    private User savedUser1;
    private User updateUser;

    @BeforeEach
    void setUp() {
        LocalDateTime date = LocalDateTime.of(2026, 5, 15, 12, 0, 0);
        LocalDateTime date1 = LocalDateTime.of(2026, 2, 11, 11, 0, 0);

        requestDto = new UserRequestDto("Igor", "test@test.com", 23);

        savedUser = new User("Igor", "test@test.com", 23, date);
        savedUser.setId(1L);
        savedUser1 = new User("Alex", "alex@test.com", 41, date1);
        savedUser1.setId(2L);

        updateDto = new UserUpdateDto(1L, "Alex", "alex@test.com", 41);
        updateUser = new User("Alex", "alex@test.com", 41, date);
        updateUser.setId(1L);

        responseDto = new UserResponseDto(1L, "Igor", "test@test.com", 23, date);
        responseDto1 = new UserResponseDto(2L, "Alex", "alex@test.com", 41, date1);
    }


    @Test
    void save_userWithUniqueEmail_savesUser(){
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.mapToUser(requestDto)).thenReturn(savedUser);
        when(userMapper.mapToUserResponseDto(savedUser)).thenReturn(responseDto);

        UserResponseDto response = service.save(requestDto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Igor", response.getName());
        assertEquals("test@test.com", response.getEmail());
        assertEquals(23, response.getAge());

        verify(userRepository).existsByEmail(requestDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void save_duplicateEmail_throwsException() {
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.save(requestDto));

        assertEquals("Данный email в базе уже присутствует!", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_validUser_updateUser() {
        when(userRepository.existsByEmail(updateDto.getEmail())).thenReturn(false);
        when(userRepository.findById(updateDto.getId())).thenReturn(Optional.of(savedUser));
        when(userRepository.save(any(User.class))).thenReturn(updateUser);
        when(userMapper.mapToUserResponseDto(any(User.class))).thenAnswer(invocation -> {
                    User u = invocation.getArgument(0);
                    return new UserResponseDto(u.getId(), u.getName(), u.getEmail(), u.getAge(), u.getCreatedAt());
                });

        UserResponseDto responseDto = service.update(updateDto);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals("Alex", responseDto.getName());
        assertEquals("alex@test.com", responseDto.getEmail());
        assertEquals(41, responseDto.getAge());

        verify(userRepository).existsByEmail(updateDto.getEmail());
        verify(userRepository).findById(updateDto.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void update_nullId_throwsException() {
        updateDto.setId(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.update(updateDto));

        assertEquals("Id не может быть null", exception.getMessage());

        verify(userRepository, never()).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_duplicateEmail_throwsException() {
        when(userRepository.existsByEmail(updateDto.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.update(updateDto));

        assertEquals("Данный email в базе уже присутствует!", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_userNotFound_throwsException() {
        when(userRepository.existsByEmail(updateDto.getEmail())).thenReturn(false);
        when(userRepository.findById(updateDto.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.update(updateDto));

        assertEquals("Пользователь с id: " + updateDto.getId() + " в базе не найден!", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void get_validIds_returnsUsers() {
        List<Long> ids = List.of(1L, 2L);
        List<User> users = List.of(savedUser, savedUser1);

        when(userRepository.findAllById(ids)).thenReturn(users);
        when(userMapper.mapToListDto(anyList())).thenReturn(List.of(responseDto, responseDto1));

        List<UserResponseDto> responseDtos = service.getUsers(ids);

        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.size());
        assertEquals("Igor", responseDtos.get(0).getName());
        assertEquals("Alex", responseDtos.get(1).getName());

        verify(userRepository, times(1)).findAllById(ids);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void get_nullOrEmptyListId_returnsEmptyList(List<Long> ids) {
        List<UserResponseDto> userDtos = service.getUsers(ids);

        assertNotNull(userDtos);
        assertTrue(userDtos.isEmpty());

        verify(userRepository, never()).findAllById(anyList());
    }

    @Test
    void get_listIdContainsOnlyNulls_returnsEmptyList() {
        List<Long> ids = new ArrayList<>();
        ids.add(null);
        ids.add(null);

        List<UserResponseDto> userDtos = service.getUsers(ids);

        assertNotNull(userDtos);
        assertTrue(userDtos.isEmpty());

        verify(userRepository, never()).findAllById(anyList());
    }

    @Test
    void findAll_returnsAllUsers() {
        List<User> users =List.of(savedUser, savedUser1);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.mapToListDto(anyList())).thenReturn(List.of(responseDto, responseDto1));

        List<UserResponseDto> responseDtos = service.findAll();

        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.size());
        assertTrue(responseDtos.stream().anyMatch(u -> u.getName().equals("Igor")));
        assertTrue(responseDtos.stream().anyMatch(u -> u.getName().equals("Alex")));

        verify(userRepository).findAll();
    }

    @Test
    void delete_validId_deleteUser() {
        Long id = 1L;

        doNothing().when(userRepository).deleteById(id);

        service.deleteUser(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    void delete_nullId_throwException() {
        Long id = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.deleteUser(id));

        assertEquals("Id не может быть null", exception.getMessage());

        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void getUser_existingId_returnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(userMapper.mapToUserResponseDto(savedUser)).thenReturn(responseDto);
        UserResponseDto responseDto = service.getUser(1L);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals("Igor", responseDto.getName());
        assertEquals("test@test.com", responseDto.getEmail());
        assertEquals(23, responseDto.getAge());

        verify(userRepository).findById(1L);
    }

    @Test
    void getUser_nonExistingId_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.getUser(1L));

        assertEquals("Пользователь с id = 1 не найден", exception.getMessage());

        verify(userRepository).findById(1L);
    }
}
