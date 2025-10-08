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
    public boolean isValid(BigDecimal price, ConstraintValidatorContext context) {
        if (price == null) {
            return true; // Deixa a validação @NotNull lidar com isso
        }
        
        return price.compareTo(BigDecimal.ZERO) > 0;
    }
}