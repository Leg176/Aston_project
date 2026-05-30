package ru.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.dto.UserResponseDto;
import ru.practicum.dto.UserUpdateDto;
import ru.practicum.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users")
@Validated
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<UserResponseDto> findAll() {
        log.info("Запрос на получение всех пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable @NotNull @Positive (message = "id должен быть больше 0") Long id) {
        log.info("Запрос на получение пользователя с id={}", id);
        return userService.getUser(id);
    }

    @GetMapping("/batch")
    public Collection<UserResponseDto> getUsers(@RequestParam List<Long> ids) {
        log.info("Запрос на получение пользователей по ids={}", ids);
        return userService.getUsers(ids);
    }

    @PostMapping
    public UserResponseDto create(@Valid @RequestBody @NotNull UserRequestDto userRequest) {
        log.info("Запрос на создание пользователя с email={}", userRequest.getEmail());
        return userService.save(userRequest);
    }

    @PutMapping
    public UserResponseDto update(@Valid @RequestBody @NotNull UserUpdateDto userUpdate) {
        log.info("Запрос на обновление пользователя id={}", userUpdate.getId());
        return userService.update(userUpdate);
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable @NotNull @Positive(message = "id должен быть больше 0") Long id) {
        log.info("Запрос на удаление пользователя id={}", id);
        userService.deleteUser(id);
    }
}
