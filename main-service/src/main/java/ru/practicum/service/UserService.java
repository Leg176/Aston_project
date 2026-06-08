package ru.practicum.service;

import ru.practicum.dto.UserRequestDto;
import ru.practicum.dto.UserResponseDto;
import ru.practicum.dto.UserUpdateDto;
import ru.practicum.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(UserRequestDto requestDto);

    User update(UserUpdateDto updateDto);

    List<User> getUsers(List<Long> ids);

    List<User> findAll();

    void deleteUser(Long id);

    User getUser(Long id);
}
