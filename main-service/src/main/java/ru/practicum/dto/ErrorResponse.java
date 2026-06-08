package ru.practicum.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.practicum.constants.ErrorApiConstants;

@Schema(description = ErrorApiConstants.ERROR_RESPONSE_DESCRIPTION)
public class ErrorResponse {

    @Schema(description = ErrorApiConstants.ERROR_DESCRIPTION, example = ErrorApiConstants.USER_NOT_FOUND)
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
