package com.deliverytech.delivery.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do ValidTelefoneValidator")
class ValidTelefoneValidatorTest {

    private ValidTelefoneValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ValidTelefoneValidator();
        validator.initialize(null);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "(11) 99999-9999", "(11) 9999-9999", "11999999999", "1199999999",
        "(21) 98888-8888", "21988888888", "(85) 97777-7777", "8597777777"
    })
    @DisplayName("Deve aceitar telefones celulares válidos")
    void deveAceitarTelefonesCelularesValidos(String telefone) {
        assertTrue(validator.isValid(telefone, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "(11) 3333-4444", "1133334444", "(21) 2222-3333", "2122223333",
        "(85) 3555-6666", "8535556666"
    })
    @DisplayName("Deve aceitar telefones fixos válidos")
    void deveAceitarTelefonesFixosValidos(String telefone) {
        assertTrue(validator.isValid(telefone, null));
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
        "123", "12345", "123456789", "123456789012",
        "(11) 1234-5678", "1112345678", // Celular começando com 1
        "(11) 0999-9999", "1109999999", // Celular começando com 0
        "(10) 9999-9999", "1099999999", // Código de área inválido
        "(00) 9999-9999", "0099999999"  // Código de área inválido
    })
    @DisplayName("Deve rejeitar telefones inválidos")
    void deveRejeitarTelefonesInvalidos(String telefone) {
        assertFalse(validator.isValid(telefone, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "11 99999-9999", "+55 11 99999-9999", "+5511999999999",
        "(11)99999-9999", "11-99999-9999", "11.99999.9999"
    })
    @DisplayName("Deve aceitar diferentes formatos válidos")
    void deveAceitarDiferentesFormatosValidos(String telefone) {
        assertTrue(validator.isValid(telefone, null));
    }

    @Test
    @DisplayName("Deve validar códigos de área brasileiros")
    void deveValidarCodigosDeAreaBrasileiros() {
        // Códigos válidos
        assertTrue(validator.isValid("1133334444", null)); // SP
        assertTrue(validator.isValid("2133334444", null)); // RJ
        assertTrue(validator.isValid("8533334444", null)); // CE
        assertTrue(validator.isValid("4733334444", null)); // SC
        
        // Códigos inválidos
        assertFalse(validator.isValid("0133334444", null));
        assertFalse(validator.isValid("1033334444", null));
    }
}