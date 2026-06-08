package ru.practicum.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.practicum.constans.OpenApiConstants;
import ru.practicum.entity.MailOperationType;

@Schema(description = OpenApiConstants.MAIL_REQUEST_DESCRIPTION)
public class MailRequestDto {
    @Schema(description = OpenApiConstants.USER_EMAIL_DESCRIPTION,
            example = OpenApiConstants.USER_EMAIL_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Неверный формат электронной почты")
    @Size(min = 6, max = 254, message = "Длинна email должна быть от 6 до 254 символов")
    private String email;

    @Schema(description = OpenApiConstants.TYPE_OPERATION,
            example = OpenApiConstants.TYPE_EXAMPLE,
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private MailOperationType operation;

    public MailRequestDto() {}

    public String getEmail() {
        return email;
    }

    public MailOperationType getOperation() {
        return operation;
    }
}
