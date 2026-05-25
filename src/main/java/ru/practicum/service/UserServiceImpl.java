package ru.practicum.service;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.repository.UserRepository;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.dto.UserResponseDto;
import ru.practicum.dto.UserUpdateDto;
import ru.practicum.entity.User;
import ru.practicum.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        return userMapper.mapToUserResponseDto(user);
    }

    @Override
    public UserResponseDto save(UserRequestDto requestDto) {
        log.info("Сохранение User с email: {}", requestDto.getEmail());

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("User с email {} в базе существует", requestDto.getEmail());
            throw new IllegalArgumentException("Данный email в базе уже присутствует!");
        }

        User user = userMapper.mapToUser(requestDto);
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        log.info("User сохранён с id: {}", savedUser.getId());

        return userMapper.mapToUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto update(UserUpdateDto updateDto) {
        log.info("Обновление User с id: {}", updateDto.getId());

        if (updateDto.getId() == null) {
            throw new IllegalArgumentException("Id не может быть null");
        }

        if (updateDto.hasEmail() && userRepository.existsByEmail(updateDto.getEmail())) {
            log.warn("User с email {} в базе существует", updateDto.getEmail());
            throw new IllegalArgumentException("Данный email в базе уже присутствует!");
        }

        Long idUserUpdate = updateDto.getId();
        Optional<User> userOpt = userRepository.findById(idUserUpdate);

        if (userOpt.isEmpty()) {
            log.warn("Пользователь с id {} не найден", idUserUpdate);
            throw new EntityNotFoundException("Пользователь с id: " + idUserUpdate + " в базе не найден!");
        }

        User user = userOpt.get();

        userMapper.updateUserFields(user, updateDto);
        User updateUser = userRepository.save(user);
        log.info("User с id обновлён: {}", updateUser.getId());

        return userMapper.mapToUserResponseDto(updateUser);
    }

    @Override
    public List<UserResponseDto> getUsers(List<Long> ids) {
        log.info("Поиск User по ids: {}", ids);

        if (ids == null || ids.isEmpty()) {
            log.warn("Список ids пуст или равен null");
            return List.of();
        }

        List<Long> idsFilter = ids.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (idsFilter.isEmpty()) {
            log.warn("Пользователи не найдены т. к. список ids не содержит валидные значения");
            return List.of();
        }

        List<User> users = userRepository.findAllById(idsFilter);
        log.info("Найдено Users: {}", users.size());
        return userMapper.mapToListDto(users);
    }

    @Override
    public List<UserResponseDto> findAll() {
        log.info("Поиск всех User в базе");
        List<User> users = userRepository.findAll();
        log.info("Общее количество Users в базе данных: {}", users.size());

        return userMapper.mapToListDto(users);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Удаление User с id: {}", id);

        if (id == null) {
            log.error("Попытка удаления User с id = null");
            throw new IllegalArgumentException("Id не может быть null");
        }

        userRepository.deleteById(id);
    }
}
