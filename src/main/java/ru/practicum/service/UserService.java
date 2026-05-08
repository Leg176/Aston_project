package ru.practicum.service;

import ru.practicum.dto.UserRequestDto;
import ru.practicum.dto.UserResponseDto;
import ru.practicum.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    UserResponseDto save(UserRequestDto requestDto);

    UserResponseDto update(UserUpdateDto updateDto);

    List<UserResponseDto> getUsers(List<Long> ids);

    List<UserResponseDto> findAll();

    boolean deleteUser(Long id);
}
