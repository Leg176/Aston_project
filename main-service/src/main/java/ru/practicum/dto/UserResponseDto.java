package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;
import ru.practicum.constants.OpenApiConstants;

import java.time.LocalDateTime;

@Schema(description = OpenApiConstants.USER_RESPONSE_DESCRIPTION)
public class UserResponseDto extends RepresentationModel<UserResponseDto> {
    @Schema(description = OpenApiConstants.USER_ID_DESCRIPTION,
            example = OpenApiConstants.USER_ID_EXAMPLE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
    @Schema(description = OpenApiConstants.USER_CREATE_DESCRIPTION,
            example = OpenApiConstants.USER_CREATE_EXAMPLE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    public UserResponseDto() {
    }

    public UserResponseDto(Long id, String name, String email, Integer age, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
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

    @Override
    public String toString() {
        return "UserResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createdAt=" + createdAt +
                '}';
    }
}
