package ru.practicum.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.practicum.constants.OpenApiConstants;

@Schema(description = OpenApiConstants.USER_UPDATE_DESCRIPTION)
public class UserUpdateDto {
    @Schema(description = OpenApiConstants.USER_ID_DESCRIPTION,
            example = OpenApiConstants.USER_ID_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    @Schema(description = OpenApiConstants.USER_NAME_DESCRIPTION,
            example = OpenApiConstants.USER_NAME_EXAMPLE)
    private String name;
    @Schema(description = OpenApiConstants.USER_EMAIL_DESCRIPTION,
            example = OpenApiConstants.USER_EMAIL_EXAMPLE)
    private String email;
    @Schema(description = OpenApiConstants.USER_AGE_DESCRIPTION,
            example = OpenApiConstants.USER_AGE_EXAMPLE)
    private Integer age;

    public UserUpdateDto() {
    }

    public UserUpdateDto(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasEmail() {
        return email != null && !email.isBlank();
    }

    public boolean hasAge() {
        return age != null;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
