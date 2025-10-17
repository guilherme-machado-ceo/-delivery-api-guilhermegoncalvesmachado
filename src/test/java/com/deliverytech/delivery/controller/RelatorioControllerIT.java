package com.deliverytech.delivery.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class RelatorioControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void deveGerarDashboardGeral() throws Exception {
        mockMvc.perform(get("/api/relatorios/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalRestaurantes").exists())
                .andExpect(jsonPath("$.data.totalClientes").exists())
                .andExpect(jsonPath("$.data.pedidosMes").exists());
    }
    
    @Test
    void deveGerarRelatorioVendasPorRestaurante() throws Exception {
        mockMvc.perform(get("/api/relatorios/vendas-por-restaurante"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    @Test
    void deveGerarRankingProdutosMaisVendidos() throws Exception {
        mockMvc.perform(get("/api/relatorios/produtos-mais-vendidos")
                .param("limite", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    @Test
    void deveGerarRelatorioPedidosPorPeriodo() throws Exception {
        mockMvc.perform(get("/api/relatorios/pedidos-por-periodo")
                .param("dataInicio", "2024-01-01")
                .param("dataFim", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalPedidos").exists())
                .andExpect(jsonPath("$.data.totalVendas").exists());
    }
    
    @Test
    void deveValidarParametrosObrigatoriosEmRelatorioPorPeriodo() throws Exception {
        mockMvc.perform(get("/api/relatorios/pedidos-por-periodo"))
                .andExpect(status().isBadRequest());
    }
}