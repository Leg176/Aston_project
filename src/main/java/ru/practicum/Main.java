package ru.practicum;

import ru.practicum.dal.UserRepository;
import ru.practicum.dal.UserRepositoryImpl;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.dto.UserResponseDto;
import ru.practicum.dto.UserUpdateDto;
import ru.practicum.service.UserService;
import ru.practicum.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepositoryImpl();
        UserService userService = new UserServiceImpl(userRepository);

        boolean isWork = true;

        while (isWork) {

            printMenu();

            System.out.print("Выберите действие: ");
            String point = scanner.nextLine();

            try {
                switch (point) {
                    case "1" -> createUser(userService);
                    case "2" -> updateUser(userService);
                    case "3" -> findAllUsers(userService);
                    case "4" -> findUsersByIds(userService);
                    case "5" -> deleteUser(userService);
                    case "0" -> {
                        isWork = false;
                        System.out.println("Выход из программы...");
                    }
                    default -> System.out.println("Неверный пункт меню!");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("USER меню");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Обновить пользователя");
        System.out.println("3. Получить всех пользователей");
        System.out.println("4. Получить пользователей по ids");
        System.out.println("5. Удалить пользователя");
        System.out.println("0. Выход");
        System.out.println();
    }

    private static void createUser(UserService userService) {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        System.out.print("Введите возраст: ");
        Integer age = Integer.parseInt(scanner.nextLine());

        UserRequestDto requestDto = new UserRequestDto(name, email, age);
        UserResponseDto responseDto = userService.save(requestDto);

        System.out.println("Пользователь успешно создан: " + responseDto);
    }

    private static void updateUser(UserService userService) {
        System.out.print("Введите id пользователя: ");
        Long id = Long.parseLong(scanner.nextLine());

        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setId(id);

        System.out.print("Введите новое имя: ");
        String name = scanner.nextLine();
        updateDto.setName(name);

        System.out.print("Введите новый email: ");
        String email = scanner.nextLine();
        updateDto.setEmail(email);

        System.out.print("Введите новый возраст: ");
        String ageInput = scanner.nextLine();

        if (!ageInput.isBlank()) {
            updateDto.setAge(Integer.parseInt(ageInput));
        }

        UserResponseDto responseDto = userService.update(updateDto);

        System.out.println("Пользователь успешно обновлён: " + responseDto);
    }

    private static void findAllUsers(UserService userService) {
        List<UserResponseDto> usersDto = userService.findAll();

        if (usersDto.isEmpty()) {
            System.out.println("Пользователи отсутствуют");
            return;
        }

        System.out.println("Пользователи хранящиеся в базе данных: ");

        for (UserResponseDto dto : usersDto) {
            System.out.println(dto);
        }
    }

    private static void findUsersByIds(UserService userService) {
        System.out.print("Введите ids через запятую: ");
        String input = scanner.nextLine();
        String[] splitIds = input.split(",");

        List<Long> ids = new ArrayList<>();

        for (String id : splitIds) {
            ids.add(Long.parseLong(id.trim()));
        }

        List<UserResponseDto> usersDto = userService.getUsers(ids);

        if (usersDto.isEmpty()) {
            System.out.println("Пользователи не найдены");
            return;
        }

        System.out.println("Пользователи найденные в базе данных: ");

        for (UserResponseDto dto : usersDto) {
            System.out.println(dto);
        }
    }

    private static void deleteUser(UserService userService) {

        System.out.print("Введите id пользователя: ");
        Long id = Long.parseLong(scanner.nextLine());

        boolean deleted = userService.deleteUser(id);

        if (deleted) {
            System.out.println("Пользователь удалён");
        } else {
            System.out.println("Пользователь не найден");
        }
    }
}