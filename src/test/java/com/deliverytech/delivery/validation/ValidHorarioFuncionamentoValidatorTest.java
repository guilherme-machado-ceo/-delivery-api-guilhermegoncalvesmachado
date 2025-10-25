package com.deliverytech.delivery.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes do ValidHorarioFuncionamentoValidator")
class ValidHorarioFuncionamentoValidatorTest {

    private ValidHorarioFuncionamentoValidator validator;
    
    @Mock
    private ConstraintValidatorContext context;
    
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new ValidHorarioFuncionamentoValidator();
        validator.initialize(null);
        
        when(context.buildConstraintViolationWithTemplate(anyString()))
            .thenReturn(violationBuilder);
        when(violationBuilder.addConstraintViolation())
            .thenReturn(context);
    }

    @Test
    @DisplayName("Deve aceitar valor nulo")
    void deveAceitarValorNulo() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    @DisplayName("Deve aceitar string vazia")
    void deveAceitarStringVazia() {
        assertTrue(validator.isValid("", context));
        assertTrue(validator.isValid("   ", context));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "08:00-18:00", "09:30-22:30", "06:00-23:59",
        "10:00-14:00", "18:00-02:00", "22:00-06:00"
    })
    @DisplayName("Deve aceitar horários válidos")
    void deveAceitarHorariosValidos(String horario) {
        assertTrue(validator.isValid(horario, context));
    }

    @Test
    @DisplayName("Deve aceitar funcionamento 24 horas")
    void deveAceitarFuncionamento24Horas() {
        assertTrue(validator.isValid("00:00-00:00", context));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "25:00-18:00", "08:60-18:00", "08:00-25:00", "08:00-18:60",
        "8:00-18:00", "08:0-18:00", "08:00-18:0", "08-18:00",
        "08:00-18", "0800-1800", "08h00-18h00"
    })
    @DisplayName("Deve rejeitar formatos inválidos")
    void deveRejeitarFormatosInvalidos(String horario) {
        assertFalse(validator.isValid(horario, context));
    }

    @Test
    @DisplayName("Deve rejeitar horário com duração menor que 1 hora")
    void deveRejeitarHorarioComDuracaoMenorQue1Hora() {
        assertFalse(validator.isValid("10:00-10:30", context));
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(
            "Restaurante deve funcionar por pelo menos 1 hora");
    }

    @Test
    @DisplayName("Deve rejeitar horário com duração maior que 18 horas")
    void deveRejeitarHorarioComDuracaoMaiorQue18Horas() {
        assertFalse(validator.isValid("05:00-00:00", context));
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(
            "Restaurante não deve funcionar mais que 18 horas seguidas");
    }

    @Test
    @DisplayName("Deve validar horário que cruza meia-noite")
    void deveValidarHorarioQueCruzaMeiaNoite() {
        // Horário válido que cruza meia-noite (4 horas)
        assertTrue(validator.isValid("22:00-02:00", context));
        
        // Horário inválido que cruza meia-noite (30 minutos)
        assertFalse(validator.isValid("23:30-00:00", context));
    }

    @Test
    @DisplayName("Deve aceitar horários com espaços nas bordas")
    void deveAceitarHorariosComEspacos() {
        assertTrue(validator.isValid("  08:00-18:00  ", context));
        assertTrue(validator.isValid(" 09:00-17:00 ", context));
    }
}