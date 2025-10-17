package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Resposta de erro de validação")
public class ValidationErrorResponse extends ErrorResponse {
    
    public ValidationErrorResponse() {
        super();
    }
    
    public ValidationErrorResponse(List<FieldError> fieldErrors) {
        super(new ErrorDetails("VALIDATION_ERROR", "Dados inválidos", fieldErrors));
    }
    
    public static ValidationErrorResponse of(List<FieldError> fieldErrors) {
        return new ValidationErrorResponse(fieldErrors);
    }
    
    public static ValidationErrorResponse of(String field, String message) {
        List<FieldError> fieldErrors = List.of(new FieldError(field, message));
        return new ValidationErrorResponse(fieldErrors);
    }
}