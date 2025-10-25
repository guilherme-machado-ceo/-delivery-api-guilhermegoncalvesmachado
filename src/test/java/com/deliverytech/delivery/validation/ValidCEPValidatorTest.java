package com.deliverytech.delivery.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do ValidCEPValidator")
class ValidCEPValidatorTest {

    private ValidCEPValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ValidCEPValidator();
        validator.initialize(null);
    }

    @Test
    @DisplayName("Deve aceitar CEP válido com hífen")
    void deveAceitarCepValidoComHifen() {
        assertTrue(validator.isValid("01234-567", null));
        assertTrue(validator.isValid("12345-678", null));
        assertTrue(validator.isValid("98765-432", null));
    }

    @Test
    @DisplayName("Deve aceitar CEP válido sem hífen")
    void deveAceitarCepValidoSemHifen() {
        assertTrue(validator.isValid("01234567", null));
        assertTrue(validator.isValid("12345678", null));
        assertTrue(validator.isValid("98765432", null));
    }

    @Test
    @DisplayName("Deve aceitar valor nulo")
    void deveAceitarValorNulo() {
        assertTrue(validator.isValid(null, null));
    }

    @Test
    @DisplayName("Deve aceitar string vazia")
    void deveAceitarStringVazia() {
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid("   ", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "00000000", "00000-000",
        "11111111", "11111-111", 
        "22222222", "22222-222",
        "99999999", "99999-999"
    })
    @DisplayName("Deve rejeitar CEPs inválidos conhecidos")
    void deveRejeitarCepsInvalidosConhecidos(String cep) {
        assertFalse(validator.isValid(cep, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "123", "1234", "12345", "123456", "1234567", "123456789",
        "12345-67", "1234-567", "123456-78",
        "abcde-fgh", "12abc-def",
        "12345-", "-12345", "12345--567"
    })
    @DisplayName("Deve rejeitar formatos inválidos")
    void deveRejeitarFormatosInvalidos(String cep) {
        assertFalse(validator.isValid(cep, null));
    }

    @Test
    @DisplayName("Deve aceitar CEPs com espaços nas bordas")
    void deveAceitarCepsComEspacos() {
        assertTrue(validator.isValid("  01234-567  ", null));
        assertTrue(validator.isValid(" 12345678 ", null));
    }
}