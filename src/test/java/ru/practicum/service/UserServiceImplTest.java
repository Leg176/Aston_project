package ru.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.dao.UserDao;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.dto.UserResponseDto;
import ru.practicum.dto.UserUpdateDto;
import ru.practicum.entity.User;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl service;

    private UserRequestDto requestDto;
    private UserUpdateDto updateDto;
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
    }


    @Test
    void save_userWithUniqueEmail_savesUser(){
        when(userDao.existsByEmail(requestDto.getEmail())).thenReturn(false);

        when(userDao.saveUser(any(User.class))).thenReturn(savedUser);

        UserResponseDto response = service.save(requestDto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Igor", response.getName());
        assertEquals("test@test.com", response.getEmail());
        assertEquals(23, response.getAge());

        verify(userDao).existsByEmail(requestDto.getEmail());
        verify(userDao, times(1)).saveUser(any(User.class));
    }

    @Test
    void save_duplicateEmail_throwsException() {
        when(userDao.existsByEmail(requestDto.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.save(requestDto));

        assertEquals("Данный email в базе уже присутствует!", exception.getMessage());

        verify(userDao, never()).saveUser(any(User.class));
    }

    @Test
    void update_validUser_updateUser() {
        when(userDao.existsByEmail(updateDto.getEmail())).thenReturn(false);
        when(userDao.getUserById(updateDto.getId())).thenReturn(Optional.of(savedUser));

        when(userDao.updateUser(any(User.class))).thenReturn(updateUser);

        UserResponseDto responseDto = service.update(updateDto);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals("Alex", responseDto.getName());
        assertEquals("alex@test.com", responseDto.getEmail());
        assertEquals(41, responseDto.getAge());

        verify(userDao).existsByEmail(updateDto.getEmail());
        verify(userDao).getUserById(updateDto.getId());
        verify(userDao, times(1)).updateUser(any(User.class));
    }

    @Test
    void update_nullId_throwsException() {
        updateDto.setId(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.update(updateDto));

        assertEquals("Id не может быть null", exception.getMessage());

        verify(userDao, never()).getUserById(anyLong());
        verify(userDao, never()).updateUser(any(User.class));
    }

    @Test
    void update_duplicateEmail_throwsException() {
        when(userDao.existsByEmail(updateDto.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.update(updateDto));

        assertEquals("Данный email в базе уже присутствует!", exception.getMessage());

        verify(userDao, never()).updateUser(any(User.class));
    }

    @Test
    void update_userNotFound_throwsException() {
        when(userDao.existsByEmail(updateDto.getEmail())).thenReturn(false);
        when(userDao.getUserById(updateDto.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.update(updateDto));

        assertEquals("Пользователь с id: " + updateDto.getId() + " в базе не найден!", exception.getMessage());

        verify(userDao, never()).updateUser(any(User.class));
    }

    @Test
    void get_validIds_returnsUsers() {
        List<Long> ids = List.of(1L, 2L);
        List<User> users = List.of(savedUser, savedUser1);

        when(userDao.getUsers(ids)).thenReturn(users);

        List<UserResponseDto> responseDtos = service.getUsers(ids);

        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.size());
        assertEquals("Igor", responseDtos.get(0).getName());
        assertEquals("Alex", responseDtos.get(1).getName());

        verify(userDao, times(1)).getUsers(ids);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void get_nullOrEmptyListId_returnsEmptyList(List<Long> ids) {
        List<UserResponseDto> userDtos = service.getUsers(ids);

        assertNotNull(userDtos);
        assertTrue(userDtos.isEmpty());

        verify(userDao, never()).getUsers(anyList());
    }

    @Test
    void get_listIdContainsOnlyNulls_returnsEmptyList() {
        List<Long> ids = new ArrayList<>();
        ids.add(null);
        ids.add(null);

        List<UserResponseDto> userDtos = service.getUsers(ids);

        assertNotNull(userDtos);
        assertTrue(userDtos.isEmpty());

        verify(userDao, never()).getUsers(anyList());
    }

    @Test
    void findAll_returnsAllUsers() {
        List<User> users =List.of(savedUser, savedUser1);

        when(userDao.findAll()).thenReturn(users);

        List<UserResponseDto> responseDtos = service.findAll();

        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.size());
        assertEquals("Igor", responseDtos.get(0).getName());
        assertEquals("Alex", responseDtos.get(1).getName());

        verify(userDao).findAll();
    }

    @Test
    void delete_validId_deleteUser() {
        Long id = 1L;

        when(userDao.deleteUser(id)).thenReturn(true);

        boolean isDelete = service.deleteUser(id);

        assertTrue(isDelete);

        verify(userDao).deleteUser(id);
    }

    @Test
    void delete_userNotFound_returnFalse() {
        Long id = 999L;

        when(userDao.deleteUser(id)).thenReturn(false);

        boolean isDelete = service.deleteUser(id);

        assertFalse(isDelete);

        verify(userDao).deleteUser(id);
    }

    @Test
    void delete_nullId_throwException() {
        Long id = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.deleteUser(id));

        assertEquals("Id не может быть null", exception.getMessage());

        verify(userDao, never()).deleteUser(anyLong());
    }
}
