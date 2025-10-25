package com.deliverytech.delivery.exception;

import java.util.List;
import java.util.Map;

/**
 * Exceção para erros de validação customizados
 * Resulta em HTTP 400 Bad Request
 */
public class ValidationException extends RuntimeException {
    
    private final String field;
    private final Object rejectedValue;
    private final String errorCode;
    private final Map<String, String> fieldErrors;
    private final List<String> globalErrors;

    public ValidationException(String message) {
        super(message);
        this.field = null;
        this.rejectedValue = null;
        this.errorCode = "VALIDATION_ERROR";
        this.fieldErrors = null;
        this.globalErrors = null;
    }

    public ValidationException(String field, Object rejectedValue, String message) {
        super(message);
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.errorCode = "VALIDATION_ERROR";
        this.fieldErrors = null;
        this.globalErrors = null;
    }

    public ValidationException(String errorCode, String message) {
        super(message);
        this.field = null;
        this.rejectedValue = null;
        this.errorCode = errorCode;
        this.fieldErrors = null;
        this.globalErrors = null;
    }

    public ValidationException(Map<String, String> fieldErrors) {
        super("Erro de validação em múltiplos campos");
        this.field = null;
        this.rejectedValue = null;
        this.errorCode = "MULTIPLE_VALIDATION_ERRORS";
        this.fieldErrors = fieldErrors;
        this.globalErrors = null;
    }

    public ValidationException(List<String> globalErrors) {
        super("Erro de validação global");
        this.field = null;
        this.rejectedValue = null;
        this.errorCode = "GLOBAL_VALIDATION_ERROR";
        this.fieldErrors = null;
        this.globalErrors = globalErrors;
    }

    public ValidationException(String errorCode, String message, Map<String, String> fieldErrors) {
        super(message);
        this.field = null;
        this.rejectedValue = null;
        this.errorCode = errorCode;
        this.fieldErrors = fieldErrors;
        this.globalErrors = null;
    }

    public String getField() {
        return field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public List<String> getGlobalErrors() {
        return globalErrors;
    }

    // Factory methods para validações específicas

    public static ValidationException campoObrigatorio(String field) {
        return new ValidationException(field, null, "Campo obrigatório não informado");
    }

    public static ValidationException valorInvalido(String field, Object value) {
        return new ValidationException(field, value, "Valor inválido para o campo");
    }

    public static ValidationException formatoInvalido(String field, Object value, String formatoEsperado) {
        return new ValidationException(field, value, 
            String.format("Formato inválido. Esperado: %s", formatoEsperado));
    }

    public static ValidationException valorForaDoIntervalo(String field, Object value, Object min, Object max) {
        return new ValidationException(field, value, 
            String.format("Valor deve estar entre %s e %s", min, max));
    }

    public static ValidationException tamanhoInvalido(String field, Object value, int min, int max) {
        return new ValidationException(field, value, 
            String.format("Tamanho deve estar entre %d e %d caracteres", min, max));
    }

    public static ValidationException emailInvalido(String email) {
        return new ValidationException("email", email, "Formato de email inválido");
    }

    public static ValidationException telefoneInvalido(String telefone) {
        return new ValidationException("telefone", telefone, "Formato de telefone inválido");
    }

    public static ValidationException cepInvalido(String cep) {
        return new ValidationException("cep", cep, "Formato de CEP inválido");
    }

    public static ValidationException categoriaInvalida(String categoria) {
        return new ValidationException("categoria", categoria, "Categoria não é válida");
    }

    public static ValidationException horarioInvalido(String horario) {
        return new ValidationException("horarioFuncionamento", horario, 
            "Horário deve estar no formato HH:MM-HH:MM");
    }
}