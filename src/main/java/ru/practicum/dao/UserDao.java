package ru.practicum.dao;

import ru.practicum.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    User saveUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(Long id);

    List<User> getUsers(List<Long> ids);

    List<User> findAll();

    boolean deleteUser(Long id);

    boolean existsByEmail(String email);

    boolean existsById(Long id);
}
