package ru.practicum.dto;

public class UserRequestDto {
    private String name;
    private String email;
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
