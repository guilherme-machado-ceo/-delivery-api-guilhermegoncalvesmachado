package com.deliverytech.delivery.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ValidCEPValidator implements ConstraintValidator<ValidCEP, String> {
    
    private static final Pattern CEP_PATTERN = Pattern.compile("^\\d{5}-?\\d{3}$");
    
    @Override
    public void initialize(ValidCEP constraintAnnotation) {
        // Inicialização se necessária
    }
    
    @Override
    public boolean isValid(String cep, ConstraintValidatorContext context) {
        if (cep == null || cep.trim().isEmpty()) {
            return true; // Deixa @NotBlank validar se é obrigatório
        }
        
        // Remove espaços em branco
        cep = cep.trim();
        
        // Verifica se está no formato correto
        if (!CEP_PATTERN.matcher(cep).matches()) {
            return false;
        }
        
        // Remove hífen para validações adicionais
        String cepNumerico = cep.replace("-", "");
        
        // Verifica se não é um CEP inválido conhecido (como 00000000)
        if (cepNumerico.equals("00000000") || 
            cepNumerico.equals("11111111") || 
            cepNumerico.equals("22222222") ||
            cepNumerico.equals("33333333") ||
            cepNumerico.equals("44444444") ||
            cepNumerico.equals("55555555") ||
            cepNumerico.equals("66666666") ||
            cepNumerico.equals("77777777") ||
            cepNumerico.equals("88888888") ||
            cepNumerico.equals("99999999")) {
            return false;
        }
        
        return true;
    }
}