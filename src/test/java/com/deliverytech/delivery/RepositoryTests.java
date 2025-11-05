package com.deliverytech.delivery;

import com.deliverytech.delivery.model.*;
import com.deliverytech.delivery.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.deliverytech.delivery.model.StatusPedido;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class RepositoryTests {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Test
    public void testBuscaClientePorEmail() {
        // Criando cliente de teste
        Cliente cliente = new Cliente();
        cliente.setNome("Teste");
        cliente.setEmail("teste@teste.com");
        cliente.setTelefone("123456789");
        cliente.setEndereco("Rua Teste, 123");
        clienteRepository.save(cliente);

        // Testando busca por email
        assertTrue(clienteRepository.findByEmail("teste@teste.com").isPresent());
        assertEquals("Teste", clienteRepository.findByEmail("teste@teste.com").get().getNome());
    }

    @Test
    public void testBuscaRestaurantePorTaxa() {
        // Criando restaurante de teste
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restaurante.setCategoria("Teste");
        restaurante.setTaxaEntrega(new BigDecimal("5.00"));
        restauranteRepository.save(restaurante);

        // Testando busca por taxa
        List<Restaurante> restaurantes = restauranteRepository.findByTaxaEntregaLessThanEqual(new BigDecimal("6.00"));
        assertFalse(restaurantes.isEmpty());
        assertTrue(restaurantes.stream().anyMatch(r -> r.getNome().equals("Restaurante Teste")));
    }

    @Test
    public void testBuscaProdutosDisponiveis() {
        // Criando restaurante
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restauranteRepository.save(restaurante);

        // Criando produto
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(new BigDecimal("10.00"));
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);
        produtoRepository.save(produto);

        // Testando busca por disponibilidade
        List<Produto> produtos = produtoRepository.findByDisponivelTrue();
        assertFalse(produtos.isEmpty());
        assertTrue(produtos.stream().anyMatch(p -> p.getNome().equals("Produto Teste")));
    }

    @Test
    public void testPedidosRecentes() {
        // Criando cliente
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setEmail("cliente@teste.com");
        clienteRepository.save(cliente);

        // Criando restaurante
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restauranteRepository.save(restaurante);

        // Criando pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setValorTotal(new BigDecimal("50.00"));
        pedidoRepository.save(pedido);

        // Testando busca dos últimos pedidos
        List<Pedido> pedidos = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
        assertFalse(pedidos.isEmpty());
        assertEquals(pedido.getId(), pedidos.get(0).getId());
    }

    @Test
    public void testBuscaPedidosPorStatus() {
        // Criando cliente
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setEmail("cliente@teste.com");
        clienteRepository.save(cliente);

        // Criando restaurante
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restauranteRepository.save(restaurante);

        // Criando pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setStatus(StatusPedido.CONFIRMADO);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setValorTotal(new BigDecimal("50.00"));
        pedidoRepository.save(pedido);

        // Testando busca por status
        List<Pedido> pedidos = pedidoRepository.findByStatus(StatusPedido.CONFIRMADO);
        assertFalse(pedidos.isEmpty());
        assertEquals(StatusPedido.CONFIRMADO, pedidos.get(0).getStatus());
    }
}