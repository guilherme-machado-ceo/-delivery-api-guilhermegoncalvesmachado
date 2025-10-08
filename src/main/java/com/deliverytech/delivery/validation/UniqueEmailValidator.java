package com.deliverytech.delivery.validation;

import com.deliverytech.delivery.repository.ClienteRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        // Inicialização se necessária
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return true; // Deixa a validação @NotBlank lidar com isso
        }
        
        return clienteRepository.findByEmail(email).isEmpty();
    }
}