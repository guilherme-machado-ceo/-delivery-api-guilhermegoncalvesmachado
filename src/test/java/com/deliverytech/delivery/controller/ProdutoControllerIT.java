package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ProdutoDTO;
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
class ProdutoControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void deveBuscarProdutosPorCategoria() throws Exception {
        mockMvc.perform(get("/api/produtos/categoria/Pizza"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    @Test
    void deveBuscarProdutosPorNome() throws Exception {
        mockMvc.perform(get("/api/produtos/buscar")
                .param("nome", "pizza"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    @Test
    void deveRetornarErroAoBuscarProdutoInexistente() throws Exception {
        mockMvc.perform(get("/api/produtos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("ENTITY_NOT_FOUND"));
    }
    
    @Test
    void deveValidarPrecoPositivoAoCriarProduto() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Produto Teste");
        dto.setCategoria("Teste");
        dto.setPreco(new BigDecimal("-10.00")); // Preço negativo
        dto.setRestauranteId(1L);
        
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}