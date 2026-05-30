package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.dto.UserResponseDto;
import ru.practicum.dto.UserUpdateDto;
import ru.practicum.entity.User;

import java.util.List;

@Component
public class UserMapper {

    public UserResponseDto mapToUserResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCreatedAt());
    }

    public User mapToUser(UserRequestDto request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        if (request.hasAge()) {
            user.setAge(request.getAge());
        }

        return user;
    }

    public User updateUserFields(User user, UserUpdateDto updateDto) {

        if (updateDto.hasName()) {
            user.setName(updateDto.getName());
        }

        if (updateDto.hasEmail()) {
            user.setEmail(updateDto.getEmail());
        }

        if (updateDto.hasAge()) {
            user.setAge(updateDto.getAge());
        }

        return user;
    }

    public List<UserResponseDto> mapToListDto(List<User> users) {
        return users.stream()
                .map(this::mapToUserResponseDto)
                .toList();
    }
}
