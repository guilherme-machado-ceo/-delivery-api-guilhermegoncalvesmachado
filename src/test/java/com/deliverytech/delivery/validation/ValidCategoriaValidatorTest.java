package com.deliverytech.delivery.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do ValidCategoriaValidator")
class ValidCategoriaValidatorTest {

    private ValidCategoriaValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ValidCategoriaValidator();
        validator.initialize(null);
    }

    @Test
    @DisplayName("Deve rejeitar valor nulo")
    void deveRejeitarValorNulo() {
        assertFalse(validator.isValid(null, null));
    }

    @Test
    @DisplayName("Deve rejeitar string vazia")
    void deveRejeitarStringVazia() {
        assertFalse(validator.isValid("", null));
        assertFalse(validator.isValid("   ", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "FAST_FOOD", "PIZZARIA", "HAMBURGUERIA", "JAPONESA", "ITALIANA",
        "BRASILEIRA", "CHINESA", "MEXICANA", "VEGETARIANA", "DOCES_SOBREMESAS"
    })
    @DisplayName("Deve aceitar categorias válidas do enum")
    void deveAceitarCategoriasValidasDoEnum(String categoria) {
        assertTrue(validator.isValid(categoria, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "fast_food", "pizzaria", "hamburgueria", "japonesa", "italiana",
        "brasileira", "chinesa", "mexicana", "vegetariana"
    })
    @DisplayName("Deve aceitar categorias em minúsculo")
    void deveAceitarCategoriasEmMinusculo(String categoria) {
        assertTrue(validator.isValid(categoria, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Fast Food", "Pizzaria", "Hamburgueria", "Japonesa", "Italiana"
    })
    @DisplayName("Deve aceitar categorias com espaços")
    void deveAceitarCategoriasComEspacos(String categoria) {
        assertTrue(validator.isValid(categoria, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "PIZZA", "PIZZAS", "HAMBURGUER", "HAMBURGERS", "BURGER",
        "DOCE", "DOCES", "SOBREMESA", "SOBREMESAS", "BEBIDA", "OUTRO", "OUTRAS"
    })
    @DisplayName("Deve aceitar variações comuns de categorias")
    void deveAceitarVariacoesComunsDeCategorias(String categoria) {
        assertTrue(validator.isValid(categoria, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "CATEGORIA_INEXISTENTE", "INVALID_CATEGORY", "TESTE", "XPTO",
        "123", "CATEGORIA123", "!@#$%"
    })
    @DisplayName("Deve rejeitar categorias inválidas")
    void deveRejeitarCategoriasInvalidas(String categoria) {
        assertFalse(validator.isValid(categoria, null));
    }

    @Test
    @DisplayName("Deve aceitar categorias com espaços nas bordas")
    void deveAceitarCategoriasComEspacosNasBordas() {
        assertTrue(validator.isValid("  PIZZARIA  ", null));
        assertTrue(validator.isValid(" Fast Food ", null));
    }

    @Test
    @DisplayName("Deve normalizar categoria antes da validação")
    void deveNormalizarCategoriaAntesValidacao() {
        assertTrue(validator.isValid("fast food", null)); // Será normalizado para FAST_FOOD
        assertTrue(validator.isValid("Doces & Sobremesas", null)); // Será normalizado
        assertTrue(validator.isValid("pizza", null)); // Variação aceita
    }
}