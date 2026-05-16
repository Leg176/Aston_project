package ru.practicum.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.practicum.dao.UserDao;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.dto.UserResponseDto;
import ru.practicum.dto.UserUpdateDto;
import ru.practicum.entity.User;
import ru.practicum.mapper.UserMapper;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserResponseDto save(UserRequestDto requestDto) {
        log.info("Сохранение User с email: {}", requestDto.getEmail());

        if (userDao.existsByEmail(requestDto.getEmail())) {
            log.warn("User с email {} в базе существует", requestDto.getEmail());
            throw new IllegalArgumentException("Данный email в базе уже присутствует!");
        }

        User user = UserMapper.mapToUser(requestDto);
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userDao.saveUser(user);
        log.info("User сохранён с id: {}", savedUser.getId());

        return UserMapper.mapToUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto update(UserUpdateDto updateDto) {
        log.info("Обновление User с id: {}", updateDto.getId());

        if (updateDto.getId() == null) {
            throw new IllegalArgumentException("Id не может быть null");
        }

        if (updateDto.hasEmail() && userDao.existsByEmail(updateDto.getEmail())) {
            log.warn("User с email {} в базе существует", updateDto.getEmail());
            throw new IllegalArgumentException("Данный email в базе уже присутствует!");
        }

        Long idUserUpdate = updateDto.getId();
        Optional<User> userOpt = userDao.getUserById(idUserUpdate);

        if (userOpt.isEmpty()) {
            log.warn("Пользователь с id {} не найден", idUserUpdate);
            throw new EntityNotFoundException("Пользователь с id: " + idUserUpdate + " в базе не найден!");
        }

        User user = userOpt.get();

        UserMapper.updateUserFields(user, updateDto);
        User updateUser = userDao.updateUser(user);
        log.info("User с id обновлён: {}", updateUser.getId());

        return UserMapper.mapToUserResponseDto(updateUser);
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

        List<User> users = userDao.getUsers(idsFilter);
        log.info("Найдено Users: {}", users.size());
        return UserMapper.mapToListDto(users);
    }

    @Override
    public List<UserResponseDto> findAll() {
        log.info("Поиск всех User в базе");
        List<User> users = userDao.findAll();
        log.info("Общее количество Users в базе данных: {}", users.size());

        return UserMapper.mapToListDto(users);
    }

    @Override
    public boolean deleteUser(Long id) {
        log.info("Удаление User с id: {}", id);

        if (id == null) {
            log.error("Попытка удаления User с id = null");
            throw new IllegalArgumentException("Id не может быть null");
        }

        boolean isDeleted = userDao.deleteUser(id);

        if (isDeleted) {
            log.info("User с id {} удалён", id);
        } else {
            log.warn("User с id {} не найден", id);
        }

        return isDeleted;
    }
}
