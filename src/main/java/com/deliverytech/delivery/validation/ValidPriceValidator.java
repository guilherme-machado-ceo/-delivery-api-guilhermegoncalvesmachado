package com.deliverytech.delivery.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class ValidPriceValidator implements ConstraintValidator<ValidPrice, BigDecimal> {
    
    @Override
    public void initialize(ValidPrice constraintAnnotation) {
        // Inicialização se necessária
    }
    
    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Deixa @NotNull validar se é obrigatório
        }
        
        // Verifica se é positivo
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        // Verifica se tem no máximo 2 casas decimais
        if (value.scale() > 2) {
            return false;
        }
        
        // Verifica se não é muito grande (máximo 999999.99)
        if (value.compareTo(new BigDecimal("999999.99")) > 0) {
            return false;
        }
        
        return true;
    }
}