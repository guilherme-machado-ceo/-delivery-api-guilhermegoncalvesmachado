package com.deliverytech.delivery.exception;

import com.deliverytech.delivery.dto.ErrorResponse;
import com.deliverytech.delivery.dto.FieldError;
import com.deliverytech.delivery.dto.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        logger.warn("Validation error on {}: {}", request.getRequestURI(), ex.getMessage());
        
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()))
                .collect(Collectors.toList());

        ValidationErrorResponse errorResponse = ValidationErrorResponse.of(fieldErrors);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException ex, HttpServletRequest request) {
        
        logger.warn("Entity not found on {}: {}", request.getRequestURI(), ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of("ENTITY_NOT_FOUND", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex, HttpServletRequest request) {
        
        logger.warn("Duplicate resource on {}: {}", request.getRequestURI(), ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of("DUPLICATE_RESOURCE", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        
        logger.warn("Business rule violation on {}: {}", request.getRequestURI(), ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of("BUSINESS_RULE_VIOLATION", ex.getMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        
        logger.error("Data integrity violation on {}: {}", request.getRequestURI(), ex.getMessage());
        
        String message = "Violação de integridade dos dados";
        String details = null;
        
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("email")) {
                message = "Email já está em uso";
                details = "O email informado já está cadastrado no sistema";
            } else if (ex.getMessage().contains("unique")) {
                message = "Dados duplicados";
                details = "Os dados informados já existem no sistema";
            }
        }
        
        ErrorResponse errorResponse = details != null ? 
            ErrorResponse.of("DATA_INTEGRITY_VIOLATION", message, details) :
            ErrorResponse.of("DATA_INTEGRITY_VIOLATION", message);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        logger.error("Unexpected error on {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.of(
            "INTERNAL_SERVER_ERROR", 
            "Erro interno do servidor",
            "Ocorreu um erro inesperado. Tente novamente mais tarde."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}