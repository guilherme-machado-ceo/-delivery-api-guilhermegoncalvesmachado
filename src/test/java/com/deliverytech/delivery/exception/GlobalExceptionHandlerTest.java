package com.deliverytech.delivery.exception;

import com.deliverytech.delivery.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GlobalExceptionHandlerTest.TestController.class)
@DisplayName("Testes do GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @PostMapping("/validation")
        public String testValidation(@RequestBody TestDTO dto) {
            return "success";
        }

        @GetMapping("/not-found")
        public String testNotFound() {
            throw EntityNotFoundException.restaurante(999L);
        }

        @GetMapping("/business")
        public String testBusiness() {
            throw BusinessException.restauranteInativo(1L);
        }

        @GetMapping("/conflict")
        public String testConflict() {
            throw ConflictException.emailJaExiste("test@test.com");
        }

        @GetMapping("/generic")
        public String testGeneric() {
            throw new RuntimeException("Erro inesperado");
        }
    }

    static class TestDTO {
        private String nome;
        private String email;

        // Getters and setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    @Test
    @DisplayName("Deve tratar EntityNotFoundException com status 404")
    void deveTratarEntityNotFoundExceptionCom404() throws Exception {
        MvcResult result = mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertFalse(errorResponse.isSuccess());
        assertNotNull(errorResponse.getError());
        assertEquals("ENTITY_NOT_FOUND", errorResponse.getError().getCode());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    @DisplayName("Deve tratar BusinessException com status 400")
    void deveTratarBusinessExceptionCom400() throws Exception {
        MvcResult result = mockMvc.perform(get("/test/business"))
                .andExpect(status().isBadRequest())
                .andExpected(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertFalse(errorResponse.isSuccess());
        assertEquals("BUSINESS_RULE_VIOLATION", errorResponse.getError().getCode());
    }

    @Test
    @DisplayName("Deve tratar ConflictException com status 409")
    void deveTratarConflictExceptionCom409() throws Exception {
        MvcResult result = mockMvc.perform(get("/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertFalse(errorResponse.isSuccess());
        assertEquals("DUPLICATE_RESOURCE", errorResponse.getError().getCode());
        assertTrue(errorResponse.getError().getMessage().contains("email"));
    }

    @Test
    @DisplayName("Deve tratar Exception genérica com status 500")
    void deveTratarExceptionGenericaCom500() throws Exception {
        MvcResult result = mockMvc.perform(get("/test/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertFalse(errorResponse.isSuccess());
        assertEquals("INTERNAL_SERVER_ERROR", errorResponse.getError().getCode());
        assertEquals("Erro interno do servidor", errorResponse.getError().getMessage());
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException com status 400")
    void deveTratarMethodArgumentNotValidExceptionCom400() throws Exception {
        String invalidJson = "{\"nome\":\"\",\"email\":\"invalid-email\"}";

        MvcResult result = mockMvc.perform(post("/test/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertFalse(errorResponse.isSuccess());
        assertEquals("Bad Request", errorResponse.getError().getMessage());
        assertNotNull(errorResponse.getError().getDetails());
    }

    @Test
    @DisplayName("Deve incluir timestamp em todas as respostas de erro")
    void deveIncluirTimestampEmTodasRespostasErro() throws Exception {
        MvcResult result = mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    @DisplayName("Deve manter estrutura consistente de erro")
    void deveManterEstruturaConsistenteErro() throws Exception {
        MvcResult result = mockMvc.perform(get("/test/business"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

        // Verifica estrutura RFC 7807
        assertNotNull(errorResponse);
        assertFalse(errorResponse.isSuccess());
        assertNotNull(errorResponse.getError());
        assertNotNull(errorResponse.getError().getCode());
        assertNotNull(errorResponse.getError().getMessage());
        assertNotNull(errorResponse.getTimestamp());
    }
}