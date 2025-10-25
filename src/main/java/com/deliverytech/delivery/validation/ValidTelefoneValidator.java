package com.deliverytech.delivery.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ValidTelefoneValidator implements ConstraintValidator<ValidTelefone, String> {

    // Padrões para telefones brasileiros
    private static final Pattern TELEFONE_PATTERN = Pattern.compile(
        "^(?:\\+55\\s?)?(?:\\(?(?:0?11|0?[12-9][0-9])\\)?\\s?)?(?:9?[0-9]{4}[-\\s]?[0-9]{4})$"
    );
    
    // Padrão mais simples para apenas dígitos
    private static final Pattern DIGITOS_PATTERN = Pattern.compile("^\\d{10,11}$");

    @Override
    public void initialize(ValidTelefone constraintAnnotation) {
        // Inicialização se necessária
    }

    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }

        String telefoneNormalizado = normalizarTelefone(telefone);
        
        // Verifica se tem entre 10 e 11 dígitos
        if (!DIGITOS_PATTERN.matcher(telefoneNormalizado).matches()) {
            return false;
        }

        // Validações específicas para telefones brasileiros
        return validarTelefoneBrasileiro(telefoneNormalizado);
    }

    private String normalizarTelefone(String telefone) {
        // Remove todos os caracteres não numéricos
        return telefone.replaceAll("[^0-9]", "");
    }

    private boolean validarTelefoneBrasileiro(String telefone) {
        // Telefone com 10 dígitos: fixo (XX) XXXX-XXXX
        if (telefone.length() == 10) {
            return validarTelefoneFixo(telefone);
        }
        
        // Telefone com 11 dígitos: celular (XX) 9XXXX-XXXX
        if (telefone.length() == 11) {
            return validarTelefoneCelular(telefone);
        }
        
        return false;
    }

    private boolean validarTelefoneFixo(String telefone) {
        // Código de área válido (11-99, exceto alguns códigos inexistentes)
        String codigoArea = telefone.substring(0, 2);
        int area = Integer.parseInt(codigoArea);
        
        if (area < 11 || area > 99) {
            return false;
        }
        
        // Primeiro dígito do número não pode ser 0 ou 1
        char primeiroDigito = telefone.charAt(2);
        return primeiroDigito >= '2' && primeiroDigito <= '9';
    }

    private boolean validarTelefoneCelular(String telefone) {
        // Código de área válido
        String codigoArea = telefone.substring(0, 2);
        int area = Integer.parseInt(codigoArea);
        
        if (area < 11 || area > 99) {
            return false;
        }
        
        // Deve começar com 9 (celular)
        char nono = telefone.charAt(2);
        if (nono != '9') {
            return false;
        }
        
        // Segundo dígito do número não pode ser 0 ou 1
        char segundoDigito = telefone.charAt(3);
        return segundoDigito >= '2' && segundoDigito <= '9';
    }
}