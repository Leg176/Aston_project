package ru.practicum.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import ru.practicum.controller.UserController;
import ru.practicum.dto.UserResponseDto;
import ru.practicum.entity.User;
import ru.practicum.mapper.UserMapper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler {

    private final UserMapper mapper;

    public UserAssembler(UserMapper mapper) {
        this.mapper = mapper;
    }

    public EntityModel<UserResponseDto> toModel(User user) {

        UserResponseDto dto = mapper.mapToUserResponseDto(user);

        return EntityModel.of(dto,
                linkTo(methodOn(UserController.class)
                        .getUser(user.getId()))
                        .withSelfRel(),
                linkTo(methodOn(UserController.class)
                        .findAll())
                        .withRel("all-users"),
                linkTo(methodOn(UserController.class)
                        .update(null))
                        .withRel("update"),
                linkTo(methodOn(UserController.class)
                        .removeUser(user.getId()))
                        .withRel("delete")
        );
    }
}
