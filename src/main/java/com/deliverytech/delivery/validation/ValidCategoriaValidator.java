package com.deliverytech.delivery.validation;

import com.deliverytech.delivery.enums.CategoriaRestaurante;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidCategoriaValidator implements ConstraintValidator<ValidCategoria, String> {

    @Override
    public void initialize(ValidCategoria constraintAnnotation) {
        // Inicialização se necessária
    }

    @Override
    public boolean isValid(String categoria, ConstraintValidatorContext context) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return false;
        }

        // Normaliza a categoria para comparação
        String categoriaNormalizada = categoria.trim().toUpperCase().replace(" ", "_");
        
        // Verifica se a categoria existe no enum
        try {
            CategoriaRestaurante.valueOf(categoriaNormalizada);
            return true;
        } catch (IllegalArgumentException e) {
            // Tenta algumas variações comuns
            return verificarVariacoes(categoriaNormalizada);
        }
    }

    private boolean verificarVariacoes(String categoria) {
        // Mapeamento de variações comuns
        switch (categoria) {
            case "PIZZA":
            case "PIZZAS":
                return CategoriaRestaurante.PIZZARIA != null;
            case "HAMBURGUER":
            case "HAMBURGERS":
            case "BURGER":
                return CategoriaRestaurante.HAMBURGUERIA != null;
            case "DOCE":
            case "DOCES":
            case "SOBREMESA":
            case "SOBREMESAS":
                return CategoriaRestaurante.DOCES_SOBREMESAS != null;
            case "BEBIDA":
                return CategoriaRestaurante.BEBIDAS != null;
            case "OUTRO":
            case "OUTRAS":
                return CategoriaRestaurante.OUTROS != null;
            default:
                return false;
        }
    }
}