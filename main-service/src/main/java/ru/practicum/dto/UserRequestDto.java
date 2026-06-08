package ru.practicum.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.practicum.constants.OpenApiConstants;

@Schema(description = OpenApiConstants.USER_REQUEST_DESCRIPTION)
public class UserRequestDto {
    @Schema(description = OpenApiConstants.USER_NAME_DESCRIPTION,
            example = OpenApiConstants.USER_NAME_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 250, message = "Длинна имени должна быть от 2 до 250 символов")
    private String name;
    @Schema(description = OpenApiConstants.USER_EMAIL_DESCRIPTION,
            example = OpenApiConstants.USER_EMAIL_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Неверный формат электронной почты")
    @Size(min = 6, max = 254, message = "Длинна email должна быть от 6 до 254 символов")
    private String email;
    @Schema(description = OpenApiConstants.USER_AGE_DESCRIPTION,
            example = OpenApiConstants.USER_AGE_EXAMPLE,
            minimum = OpenApiConstants.USER_AGE_MINIMUM)
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
