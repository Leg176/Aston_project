package ru.practicum.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.practicum.entity.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class UserDaoImplTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static SessionFactory sessionFactory;
    private UserDao userDao;
    private User user;
    private User user1;

    @BeforeAll
    static void beforeAll() {
        postgres.start();

        Map<String, Object> settings = new HashMap<>();
        settings.put("hibernate.connection.url", postgres.getJdbcUrl());
        settings.put("hibernate.connection.username", postgres.getUsername());
        settings.put("hibernate.connection.password", postgres.getPassword());
        settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        settings.put("hibernate.hbm2ddl.auto", "create-drop");

        Properties properties = new Properties();
        properties.putAll(settings);

        sessionFactory = new Configuration()
                .addProperties(properties)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }

    @BeforeEach
    void setUp() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createNativeQuery("delete from users").executeUpdate();
            tx.commit();
        }

        userDao = new UserDaoImpl(sessionFactory);

        LocalDateTime date = LocalDateTime.of(2026, 5, 15, 12, 0, 0);
        LocalDateTime date1 = LocalDateTime.of(2026, 2, 11, 11, 0, 0);

        user = new User("Alex", "alex@test.com", 25, date1);
        user1 = new User("Igor", "test@test.com", 23, date);
    }

    @AfterAll
    static void afterAll() {

        if (sessionFactory != null) {
            sessionFactory.close();
        }

        postgres.stop();
    }

    @Test
    void saveUser_validUser_returnsSavedUser() {
        User savedUser = userDao.saveUser(user);

        assertNotNull(savedUser.getId());
        assertEquals("Alex", savedUser.getName());
    }

    @Test
    void updateUser_existingUser_returnsUpdatedUser() {
        User savedUser = userDao.saveUser(user);
        savedUser.setName("Ivan");
        User updatedUser = userDao.updateUser(savedUser);

        assertEquals("Ivan", updatedUser.getName());
    }

    @Test
    void getUserById_existingId_returnsUser() {
        User savedUser = userDao.saveUser(user);
        Optional<User> foundUser = userDao.getUserById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
    }

    @Test
    void getUserById_nonExistingId_returnsEmptyOptional() {
        Optional<User> foundUser = userDao.getUserById(999L);

        assertTrue(foundUser.isEmpty());
    }

    @Test
    void getUsers_existingIds_returnsUsersList() {
        User savedUser1 = userDao.saveUser(user);
        User savedUser2 = userDao.saveUser(user1);
        List<User> users = userDao.getUsers(List.of(savedUser1.getId(), savedUser2.getId()));

        assertEquals(2, users.size());
    }

    @Test
    void findAll_usersExist_returnsUsersList() {
        userDao.saveUser(user);
        userDao.saveUser(user1);
        List<User> users = userDao.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void deleteUser_existingId_returnsTrue() {
        User savedUser = userDao.saveUser(user);
        boolean deleted = userDao.deleteUser(savedUser.getId());

        assertTrue(deleted);

        Optional<User> foundUser = userDao.getUserById(savedUser.getId());

        assertTrue(foundUser.isEmpty());
    }

    @Test
    void deleteUser_nonExistingId_returnsFalse() {
        boolean deleted = userDao.deleteUser(999L);

        assertFalse(deleted);
    }

    @Test
    void existsByEmail_existingEmail_returnsTrue() {
        userDao.saveUser(user);
        boolean exists = userDao.existsByEmail("alex@test.com");

        assertTrue(exists);
    }

    @Test
    void existsByEmail_nonExistingEmail_returnsFalse() {
        boolean exists = userDao.existsByEmail("vbvb@test.com");

        assertFalse(exists);
    }

    @Test
    void existsById_existingId_returnsTrue() {
        User savedUser = userDao.saveUser(user);
        boolean exists = userDao.existsById(savedUser.getId());

        assertTrue(exists);
    }

    @Test
    void existsById_nonExistingId_returnsFalse() {
        boolean exists = userDao.existsById(999L);

        assertFalse(exists);
    }
}
