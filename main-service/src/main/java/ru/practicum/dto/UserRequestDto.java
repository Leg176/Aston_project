package ru.practicum.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequestDto {
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 250, message = "Длинна имени должна быть от 2 до 250 символов")
    private String name;
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Неверный формат электронной почты")
    @Size(min = 6, max = 254, message = "Длинна email должна быть от 6 до 254 символов")
    private String email;
    @Min(value = 0, message = "Возраст не может быть отрицательным")
    private Integer age;

    public UserRequestDto() {
    }

    public UserRequestDto(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public boolean hasAge() {
        return age != null;
    }
}
