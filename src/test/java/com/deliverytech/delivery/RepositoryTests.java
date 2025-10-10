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
        // Criando cliente de teste com dados válidos
        Cliente cliente = new Cliente();
        cliente.setNome("Teste Cliente");
        cliente.setEmail("teste@teste.com");
        cliente.setTelefone("11999999999"); // 11 dígitos válidos
        cliente.setEndereco("Rua Teste, 123 - São Paulo/SP");
        clienteRepository.save(cliente);

        // Testando busca por email
        assertTrue(clienteRepository.findByEmail("teste@teste.com").isPresent());
        assertEquals("Teste Cliente", clienteRepository.findByEmail("teste@teste.com").get().getNome());
    }

    @Test
    public void testBuscaRestaurantePorTaxa() {
        // Criando restaurante de teste com dados válidos
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restaurante.setCategoria("Italiana");
        restaurante.setEndereco("Rua dos Restaurantes, 456 - São Paulo/SP");
        restaurante.setTaxaEntrega(new BigDecimal("5.00"));
        restaurante.setAvaliacao(4.5);
        restauranteRepository.save(restaurante);

        // Testando busca por taxa
        List<Restaurante> restaurantes = restauranteRepository.findByTaxaEntregaLessThanEqual(new BigDecimal("6.00"));
        assertFalse(restaurantes.isEmpty());
        assertTrue(restaurantes.stream().anyMatch(r -> r.getNome().equals("Restaurante Teste")));
    }

    @Test
    public void testBuscaProdutosDisponiveis() {
        // Criando restaurante com dados válidos
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restaurante.setCategoria("Lanches");
        restaurante.setEndereco("Rua dos Lanches, 789 - São Paulo/SP");
        restaurante.setTaxaEntrega(new BigDecimal("3.50"));
        restaurante.setAvaliacao(4.0);
        restauranteRepository.save(restaurante);

        // Criando produto com dados válidos
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição do produto teste");
        produto.setPreco(new BigDecimal("10.00"));
        produto.setCategoria("Lanche");
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
        // Criando cliente com dados válidos
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setEmail("cliente@teste.com");
        cliente.setTelefone("11888888888");
        cliente.setEndereco("Rua do Cliente, 321 - São Paulo/SP");
        clienteRepository.save(cliente);

        // Criando restaurante com dados válidos
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restaurante.setCategoria("Pizzaria");
        restaurante.setEndereco("Rua das Pizzas, 654 - São Paulo/SP");
        restaurante.setTaxaEntrega(new BigDecimal("4.00"));
        restaurante.setAvaliacao(4.8);
        restauranteRepository.save(restaurante);

        // Criando pedido com dados válidos
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setValorTotal(new BigDecimal("50.00"));
        pedido.setEnderecoEntrega("Rua do Cliente, 321 - São Paulo/SP");
        pedido.setCepEntrega("01234-567");
        pedido.setTaxaEntrega(new BigDecimal("5.00"));
        pedido.setTotal(new BigDecimal("55.00"));
        pedido.setObservacoes("Pedido de teste");
        pedidoRepository.save(pedido);

        // Testando busca dos últimos pedidos
        List<Pedido> pedidos = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
        assertFalse(pedidos.isEmpty());
        assertEquals(pedido.getId(), pedidos.get(0).getId());
    }

    @Test
    public void testBuscaPedidosPorStatus() {
        // Criando cliente com dados válidos
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Status Teste");
        cliente.setEmail("clientestatus@teste.com");
        cliente.setTelefone("11777777777");
        cliente.setEndereco("Rua do Status, 987 - São Paulo/SP");
        clienteRepository.save(cliente);

        // Criando restaurante com dados válidos
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Status Teste");
        restaurante.setCategoria("Japonesa");
        restaurante.setEndereco("Rua do Sushi, 123 - São Paulo/SP");
        restaurante.setTaxaEntrega(new BigDecimal("6.00"));
        restaurante.setAvaliacao(4.2);
        restauranteRepository.save(restaurante);

        // Criando pedido com dados válidos
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setStatus(StatusPedido.CONFIRMADO);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setValorTotal(new BigDecimal("50.00"));
        pedido.setEnderecoEntrega("Rua do Status, 987 - São Paulo/SP");
        pedido.setCepEntrega("01234-890");
        pedido.setTaxaEntrega(new BigDecimal("6.00"));
        pedido.setTotal(new BigDecimal("56.00"));
        pedido.setObservacoes("Teste de status");
        pedidoRepository.save(pedido);

        // Testando busca por status
        List<Pedido> pedidos = pedidoRepository.findByStatus(StatusPedido.CONFIRMADO);
        assertFalse(pedidos.isEmpty());
        assertEquals(StatusPedido.CONFIRMADO, pedidos.get(0).getStatus());
    }
}