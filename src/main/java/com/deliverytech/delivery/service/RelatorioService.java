package com.deliverytech.delivery.service;

import com.deliverytech.delivery.model.StatusPedido;
import com.deliverytech.delivery.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RelatorioService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;
    
    /**
     * Gera relatório de vendas por restaurante
     */
    public List<Map<String, Object>> gerarRelatorioVendasPorRestaurante(LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime inicio = dataInicio != null ? dataInicio.atStartOfDay() : LocalDateTime.now().minusMonths(1);
        LocalDateTime fim = dataFim != null ? dataFim.atTime(23, 59, 59) : LocalDateTime.now();
        
        List<Object[]> resultados = pedidoRepository.findVendasPorRestaurante(inicio, fim);
        
        return resultados.stream().map(row -> {
            Map<String, Object> item = new HashMap<>();
            item.put("restauranteId", row[0]);
            item.put("restauranteNome", row[1]);
            item.put("totalPedidos", row[2]);
            item.put("totalVendas", row[3]);
            item.put("ticketMedio", row[4]);
            return item;
        }).collect(Collectors.toList());
    }
    
    /**
     * Gera ranking de produtos mais vendidos
     */
    public List<Map<String, Object>> gerarRankingProdutosMaisVendidos(int limite, LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime inicio = dataInicio != null ? dataInicio.atStartOfDay() : LocalDateTime.now().minusMonths(1);
        LocalDateTime fim = dataFim != null ? dataFim.atTime(23, 59, 59) : LocalDateTime.now();
        
        List<Object[]> resultados = itemPedidoRepository.findProdutosMaisVendidos(inicio, fim, limite);
        
        return resultados.stream().map(row -> {
            Map<String, Object> item = new HashMap<>();
            item.put("produtoId", row[0]);
            item.put("produtoNome", row[1]);
            item.put("restauranteNome", row[2]);
            item.put("quantidadeVendida", row[3]);
            item.put("totalVendas", row[4]);
            return item;
        }).collect(Collectors.toList());
    }
    
    /**
     * Gera ranking de clientes mais ativos
     */
    public List<Map<String, Object>> gerarRankingClientesAtivos(int limite, LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime inicio = dataInicio != null ? dataInicio.atStartOfDay() : LocalDateTime.now().minusMonths(3);
        LocalDateTime fim = dataFim != null ? dataFim.atTime(23, 59, 59) : LocalDateTime.now();
        
        List<Object[]> resultados = pedidoRepository.findClientesMaisAtivos(inicio, fim, limite);
        
        return resultados.stream().map(row -> {
            Map<String, Object> item = new HashMap<>();
            item.put("clienteId", row[0]);
            item.put("clienteNome", row[1]);
            item.put("clienteEmail", row[2]);
            item.put("totalPedidos", row[3]);
            item.put("totalGasto", row[4]);
            item.put("ticketMedio", row[5]);
            return item;
        }).collect(Collectors.toList());
    }
    
    /**
     * Gera relatório de pedidos por período
     */
    public Map<String, Object> gerarRelatorioPedidosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(23, 59, 59);
        
        Map<String, Object> relatorio = new HashMap<>();
        
        // Estatísticas gerais
        long totalPedidos = pedidoRepository.countByDataPedidoBetween(inicio, fim);
        BigDecimal totalVendas = pedidoRepository.sumTotalByDataPedidoBetween(inicio, fim);
        BigDecimal ticketMedio = totalPedidos > 0 ? totalVendas.divide(BigDecimal.valueOf(totalPedidos), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        
        relatorio.put("periodo", Map.of("inicio", dataInicio, "fim", dataFim));
        relatorio.put("totalPedidos", totalPedidos);
        relatorio.put("totalVendas", totalVendas);
        relatorio.put("ticketMedio", ticketMedio);
        
        // Pedidos por status
        Map<StatusPedido, Long> pedidosPorStatus = Arrays.stream(StatusPedido.values())
                .collect(Collectors.toMap(
                    status -> status,
                    status -> pedidoRepository.countByStatusAndDataPedidoBetween(status, inicio, fim)
                ));
        relatorio.put("pedidosPorStatus", pedidosPorStatus);
        
        // Pedidos por dia (últimos 30 dias ou período especificado)
        List<Object[]> pedidosPorDiaRaw = pedidoRepository.findPedidosPorDia(inicio, fim);
        List<Map<String, Object>> pedidosPorDia = pedidosPorDiaRaw.stream().map(row -> {
            Map<String, Object> item = new HashMap<>();
            item.put("dia", row[0]);
            item.put("quantidade", row[1]);
            item.put("total", row[2]);
            return item;
        }).collect(Collectors.toList());
        relatorio.put("pedidosPorDia", pedidosPorDia);
        
        return relatorio;
    }
    
    /**
     * Gera dashboard com métricas gerais
     */
    public Map<String, Object> gerarDashboardGeral() {
        Map<String, Object> dashboard = new HashMap<>();
        
        // Métricas do mês atual
        LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime fimMes = LocalDate.now().atTime(23, 59, 59);
        
        // Totais gerais
        dashboard.put("totalRestaurantes", restauranteRepository.count());
        dashboard.put("restaurantesAtivos", restauranteRepository.countByAtivo(true));
        dashboard.put("totalClientes", clienteRepository.count());
        dashboard.put("clientesAtivos", clienteRepository.countByAtivo(true));
        dashboard.put("totalProdutos", produtoRepository.count());
        dashboard.put("produtosDisponiveis", produtoRepository.countByDisponivelTrue());
        
        // Métricas do mês
        long pedidosMes = pedidoRepository.countByDataPedidoBetween(inicioMes, fimMes);
        BigDecimal vendasMes = pedidoRepository.sumTotalByDataPedidoBetween(inicioMes, fimMes);
        
        dashboard.put("pedidosMes", pedidosMes);
        dashboard.put("vendasMes", vendasMes != null ? vendasMes : BigDecimal.ZERO);
        dashboard.put("ticketMedioMes", pedidosMes > 0 ? 
            (vendasMes != null ? vendasMes.divide(BigDecimal.valueOf(pedidosMes), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO) : 
            BigDecimal.ZERO);
        
        // Status dos pedidos hoje
        LocalDateTime inicioHoje = LocalDate.now().atStartOfDay();
        LocalDateTime fimHoje = LocalDate.now().atTime(23, 59, 59);
        
        Map<String, Long> pedidosHoje = new HashMap<>();
        pedidosHoje.put("pendentes", pedidoRepository.countByStatusAndDataPedidoBetween(StatusPedido.PENDENTE, inicioHoje, fimHoje));
        pedidosHoje.put("confirmados", pedidoRepository.countByStatusAndDataPedidoBetween(StatusPedido.CONFIRMADO, inicioHoje, fimHoje));
        pedidosHoje.put("preparando", pedidoRepository.countByStatusAndDataPedidoBetween(StatusPedido.PREPARANDO, inicioHoje, fimHoje));
        pedidosHoje.put("prontos", pedidoRepository.countByStatusAndDataPedidoBetween(StatusPedido.PRONTO, inicioHoje, fimHoje));
        pedidosHoje.put("entregues", pedidoRepository.countByStatusAndDataPedidoBetween(StatusPedido.ENTREGUE, inicioHoje, fimHoje));
        
        dashboard.put("pedidosHoje", pedidosHoje);
        
        // Top 5 restaurantes do mês
        List<Object[]> topRestaurantes = pedidoRepository.findVendasPorRestaurante(inicioMes, fimMes)
                .stream().limit(5).collect(Collectors.toList());
        
        List<Map<String, Object>> topRestaurantesFormatado = topRestaurantes.stream().map(row -> {
            Map<String, Object> item = new HashMap<>();
            item.put("nome", row[1]);
            item.put("pedidos", row[2]);
            item.put("vendas", row[3]);
            return item;
        }).collect(Collectors.toList());
        
        dashboard.put("topRestaurantes", topRestaurantesFormatado);
        
        return dashboard;
    }
}