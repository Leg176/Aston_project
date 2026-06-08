package ru.practicum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.assembler.UserAssembler;
import ru.practicum.constants.ControllerApiConstants;
import ru.practicum.constants.ErrorApiConstants;
import ru.practicum.constants.SuccessApiConstants;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.dto.UserResponseDto;
import ru.practicum.dto.UserUpdateDto;
import ru.practicum.entity.User;
import ru.practicum.service.UserService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/api/v1/users")
@Validated
@Tag(name = ControllerApiConstants.TAG_NAME, description = ControllerApiConstants.TAG_DESCRIPTION)
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserAssembler assembler;

    public UserController(UserService userService, UserAssembler assembler) {
        this.userService = userService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = ControllerApiConstants.GET_ALL_SUMMARY)
    public CollectionModel<EntityModel<UserResponseDto>> findAll() {
        log.info("Запрос на получение всех пользователей");

        List<EntityModel<UserResponseDto>> users = userService.findAll()
                .stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).findAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = ControllerApiConstants.GET_BY_ID)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SuccessApiConstants.USER_FOUND),
            @ApiResponse(responseCode = "404", description = ErrorApiConstants.USER_NOT_FOUND),
            @ApiResponse(responseCode = "400", description = ErrorApiConstants.INVALID_ID)
    })
    public EntityModel<UserResponseDto> getUser(@PathVariable @Positive (message = "id должен быть больше 0") Long id) {
        log.info("Запрос на получение пользователя с id={}", id);

        User user = userService.getUser(id);

        return assembler.toModel(user);
    }

    @GetMapping("/batch")
    @Operation(summary = ControllerApiConstants.GET_BY_IDS)
    @ApiResponse(responseCode = "200", description = SuccessApiConstants.USERS_FOUND)
    public CollectionModel<EntityModel<UserResponseDto>> getUsers(@RequestParam List<Long> ids) {
        log.info("Запрос на получение пользователей по ids={}", ids);

        List<EntityModel<UserResponseDto>> users = userService.getUsers(ids).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(
                users,
                linkTo(methodOn(UserController.class).getUsers(ids)).withSelfRel()
        );
    }

    @PostMapping
    @Operation(summary = ControllerApiConstants.CREATE_USER)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SuccessApiConstants.USER_CREATED),
            @ApiResponse(responseCode = "400", description = ErrorApiConstants.ERROR_VALIDATION)
    })
    public EntityModel<UserResponseDto> create(@Valid @RequestBody UserRequestDto userRequest) {
        log.info("Запрос на создание пользователя с email={}", userRequest.getEmail());

        User user = userService.save(userRequest);

        return assembler.toModel(user);
    }

    @PutMapping
    @Operation(summary = ControllerApiConstants.UPDATE_USER)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SuccessApiConstants.USER_UPDATE),
            @ApiResponse(responseCode = "404", description = ErrorApiConstants.USER_NOT_FOUND)
    })
    public EntityModel<UserResponseDto> update(@Valid @RequestBody UserUpdateDto userUpdate) {
        log.info("Запрос на обновление пользователя id={}", userUpdate.getId());
        User user = userService.update(userUpdate);

        return assembler.toModel(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = ControllerApiConstants.DELETE_USER)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SuccessApiConstants.USER_DELETE),
            @ApiResponse(responseCode = "404", description = ErrorApiConstants.USER_NOT_FOUND)
    })
    public ResponseEntity<Void> removeUser(@PathVariable @Positive(message = "id должен быть больше 0") Long id) {
        log.info("Запрос на удаление пользователя id={}", id);

        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
