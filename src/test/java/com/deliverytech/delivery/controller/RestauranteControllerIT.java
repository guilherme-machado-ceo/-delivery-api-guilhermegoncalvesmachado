package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.RestauranteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class RestauranteControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void deveCriarRestauranteComSucesso() throws Exception {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setNome("Pizzaria Teste");
        dto.setCategoria("Pizza");
        dto.setEndereco("Rua Teste, 123");
        dto.setTaxaEntrega(new BigDecimal("5.00"));
        dto.setAvaliacao(4.5);
        
        mockMvc.perform(post("/api/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nome").value("Pizzaria Teste"))
                .andExpect(jsonPath("$.data.categoria").value("Pizza"))
                .andExpect(jsonPath("$.data.ativo").value(true));
    }
    
    @Test
    void deveListarRestaurantesComPaginacao() throws Exception {
        mockMvc.perform(get("/api/restaurantes")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "nome"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.links").exists());
    }
    
    @Test
    void deveRetornarErroAoBuscarRestauranteInexistente() throws Exception {
        mockMvc.perform(get("/api/restaurantes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("ENTITY_NOT_FOUND"));
    }
    
    @Test
    void deveValidarDadosObrigatoriosAoCriarRestaurante() throws Exception {
        RestauranteDTO dto = new RestauranteDTO();
        // Não preencher campos obrigatórios
        
        mockMvc.perform(post("/api/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));
    }
    
    @Test
    void deveFiltrarRestaurantesPorCategoria() throws Exception {
        mockMvc.perform(get("/api/restaurantes")
                .param("categoria", "Pizza")
                .param("ativo", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}