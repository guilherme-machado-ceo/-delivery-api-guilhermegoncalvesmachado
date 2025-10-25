package com.deliverytech.delivery.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Validação do RestauranteDTO")
class RestauranteDTOValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar RestauranteDTO válido")
    void deveValidarRestauranteDTOValido() {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setNome("Pizzaria do João");
        dto.setCategoria("PIZZARIA");
        dto.setEndereco("Rua das Pizzas, 456 - Centro - São Paulo/SP - CEP: 01234-567");
        dto.setTelefone("(11) 99999-9999");
        dto.setTaxaEntrega(new BigDecimal("5.50"));
        dto.setTempoEntrega(45);
        dto.setHorarioFuncionamento("08:00-22:00");
        dto.setAvaliacao(4.5);

        Set<ConstraintViolation<RestauranteDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar nome vazio")
    void deveRejeitarNomeVazio() {
        RestauranteDTO dto = criarRestauranteValido();
        dto.setNome("");

        Set<ConstraintViolation<RestauranteDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        
        boolean temViolacaoNome = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("nome"));
        assertTrue(temViolacaoNome);
    }

    @Test
    @DisplayName("Deve rejeitar nome muito curto")
    void deveRejeitarNomeMuitoCurto() {
        RestauranteDTO dto = criarRestauranteValido();
        dto.setNome("A");

        Set<ConstraintViolation<RestauranteDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar nome muito longo")
    void deveRejeitarNomeMuitoLongo() {
        RestauranteDTO dto = criarRestauranteValido();
        dto.setNome("A".repeat(101));

        Set<ConstraintViolation<RestauranteDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar categoria inválida")
    void deveRejeitarCategoriaInvalida() {
        RestauranteDTO dto = criarRestauranteValido();
        dto.setCategoria("CATEGORIA_INEXISTENTE");

        Set<ConstraintViolation<RestauranteDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar telefone inválido")
    void deveRejeitarTelefoneInvalido() {
        RestauranteDTO dto = criarRestauranteValido();
        dto.setTelefone("123");

        Set<ConstraintViolation<RestauranteDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1.0", "1000.0"})
    @DisplayName("Deve rejeitar taxa de entrega inválida")
    void deveRejeitarTaxaEntregaInvalida(String taxa) {
        RestauranteDTO dto = criarRestauranteValido();
        dto.setTaxaEntrega(new BigDecimal(taxa));

        Set<ConstraintViolation<RestauranteDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 150})
    @DisplayName("Deve rejeitar tempo de entrega inválido")
    void deveRejeitarTempoEntregaInvalido(int tempo) {
        RestauranteDTO dto = criarRestauranteValido();
        dto.setTempoEntrega(tempo);

        Set<ConstraintViolation<RestauranteDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar horário de funcionamento inválido")
    void deveRejeitarHorarioFuncionamentoInvalido() {
        RestauranteDTO dto = criarRestauranteValido();
        dto.setHorarioFuncionamento("25:00-26:00");

        Set<ConstraintViolation<RestauranteDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.5, 5.5})
    @DisplayName("Deve rejeitar avaliação fora do range")
    void deveRejeitarAvaliacaoForaDoRange(double avaliacao) {
        RestauranteDTO dto = criarRestauranteValido();
        dto.setAvaliacao(avaliacao);

        Set<ConstraintViolation<RestauranteDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve aceitar campos opcionais nulos")
    void deveAceitarCamposOpcionaisNulos() {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setNome("Pizzaria do João");
        dto.setCategoria("PIZZARIA");
        dto.setEndereco("Rua das Pizzas, 456 - Centro - São Paulo/SP");
        dto.setTelefone("(11) 99999-9999");
        dto.setTaxaEntrega(new BigDecimal("5.50"));
        dto.setTempoEntrega(45);
        // horarioFuncionamento e avaliacao são opcionais

        Set<ConstraintViolation<RestauranteDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    private RestauranteDTO criarRestauranteValido() {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setNome("Pizzaria do João");
        dto.setCategoria("PIZZARIA");
        dto.setEndereco("Rua das Pizzas, 456 - Centro - São Paulo/SP - CEP: 01234-567");
        dto.setTelefone("(11) 99999-9999");
        dto.setTaxaEntrega(new BigDecimal("5.50"));
        dto.setTempoEntrega(45);
        dto.setHorarioFuncionamento("08:00-22:00");
        dto.setAvaliacao(4.5);
        return dto;
    }
}