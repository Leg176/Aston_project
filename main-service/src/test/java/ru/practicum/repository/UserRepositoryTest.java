package ru.practicum.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.practicum.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private User user;
    private User user1;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        LocalDateTime date = LocalDateTime.of(2026, 5, 15, 12, 0, 0);
        LocalDateTime date1 = LocalDateTime.of(2026, 2, 11, 11, 0, 0);

        user = new User("Alex", "alex@test.com", 25, date1);
        user1 = new User("Igor", "test@test.com", 23, date);
    }

    @Test
    void saveUser_validUser_returnsSavedUser() {
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("Alex", savedUser.getName());
    }

    @Test
    void getUserById_existingId_returnsUser() {
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
    }

    @Test
    void getUserById_nonExistingId_returnsEmptyOptional() {
        Optional<User> foundUser = userRepository.findById(999L);

        assertTrue(foundUser.isEmpty());
    }

    @Test
    void getUsers_existingIds_returnsUsersList() {
        User savedUser1 = userRepository.save(user);
        User savedUser2 = userRepository.save(user1);
        List<User> users = userRepository.findAllById(List.of(savedUser1.getId(), savedUser2.getId()));

        assertEquals(2, users.size());
    }

    @Test
    void findAll_usersExist_returnsUsersList() {
        userRepository.save(user);
        userRepository.save(user1);
        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void deleteUser_existingId_returnsTrue() {
        User savedUser = userRepository.save(user);
        userRepository.deleteById(savedUser.getId());

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertTrue(foundUser.isEmpty());
    }

    @Test
    void existsByEmail_existingEmail_returnsTrue() {
        userRepository.save(user);
        boolean exists = userRepository.existsByEmail("alex@test.com");

        assertTrue(exists);
    }

    @Test
    void existsByEmail_nonExistingEmail_returnsFalse() {
        boolean exists = userRepository.existsByEmail("vbvb@test.com");

        assertFalse(exists);
    }

    @Test
    void existsById_existingId_returnsTrue() {
        User savedUser = userRepository.save(user);
        boolean exists = userRepository.existsById(savedUser.getId());

        assertTrue(exists);
    }

    @Test
    void existsById_nonExistingId_returnsFalse() {
        boolean exists = userRepository.existsById(999L);

        assertFalse(exists);
    }
}
