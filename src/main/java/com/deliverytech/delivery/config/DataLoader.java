package com.deliverytech.delivery.config;

import com.deliverytech.delivery.model.*;
import com.deliverytech.delivery.repository.*;
import com.deliverytech.delivery.model.StatusPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
@Profile("!prod")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) {
        // Criando clientes
        Cliente cliente1 = new Cliente();
        cliente1.setNome("João Silva");
        cliente1.setEmail("joao@email.com");
        cliente1.setTelefone("11999999999");
        cliente1.setEndereco("Rua A, 123");
        
        Cliente cliente2 = new Cliente();
        cliente2.setNome("Maria Santos");
        cliente2.setEmail("maria@email.com");
        cliente2.setTelefone("11988888888");
        cliente2.setEndereco("Rua B, 456");
        
        Cliente cliente3 = new Cliente();
        cliente3.setNome("Pedro Souza");
        cliente3.setEmail("pedro@email.com");
        cliente3.setTelefone("11977777777");
        cliente3.setEndereco("Rua C, 789");

        clienteRepository.saveAll(Arrays.asList(cliente1, cliente2, cliente3));
        
        // Criando restaurantes
        Restaurante restaurante1 = new Restaurante();
        restaurante1.setNome("Pizzaria do João");
        restaurante1.setCategoria("Italiana");
        restaurante1.setEndereco("Rua X, 100");
        restaurante1.setTaxaEntrega(new BigDecimal("5.00"));
        restaurante1.setAvaliacao(4.5);
        
        Restaurante restaurante2 = new Restaurante();
        restaurante2.setNome("Hamburgueria Popular");
        restaurante2.setCategoria("Lanches");
        restaurante2.setEndereco("Rua Y, 200");
        restaurante2.setTaxaEntrega(new BigDecimal("3.00"));
        restaurante2.setAvaliacao(4.8);

        restauranteRepository.saveAll(Arrays.asList(restaurante1, restaurante2));
        
        // Criando produtos
        Produto produto1 = new Produto();
        produto1.setNome("Pizza Margherita");
        produto1.setDescricao("Molho, mussarela, tomate e manjericão");
        produto1.setPreco(new BigDecimal("45.00"));
        produto1.setCategoria("Pizza");
        produto1.setRestaurante(restaurante1);
        
        Produto produto2 = new Produto();
        produto2.setNome("Pizza Calabresa");
        produto2.setDescricao("Molho, mussarela e calabresa");
        produto2.setPreco(new BigDecimal("40.00"));
        produto2.setCategoria("Pizza");
        produto2.setRestaurante(restaurante1);
        
        Produto produto3 = new Produto();
        produto3.setNome("Hambúrguer Clássico");
        produto3.setDescricao("Pão, hambúrguer, queijo, alface e tomate");
        produto3.setPreco(new BigDecimal("25.00"));
        produto3.setCategoria("Lanche");
        produto3.setRestaurante(restaurante2);
        
        Produto produto4 = new Produto();
        produto4.setNome("Batata Frita");
        produto4.setDescricao("Porção de batata frita crocante");
        produto4.setPreco(new BigDecimal("15.00"));
        produto4.setCategoria("Acompanhamento");
        produto4.setRestaurante(restaurante2);
        
        Produto produto5 = new Produto();
        produto5.setNome("Refrigerante");
        produto5.setDescricao("Lata 350ml");
        produto5.setPreco(new BigDecimal("6.00"));
        produto5.setCategoria("Bebida");
        produto5.setRestaurante(restaurante2);

        produtoRepository.saveAll(Arrays.asList(produto1, produto2, produto3, produto4, produto5));
        
        // Criando pedidos
        Pedido pedido1 = new Pedido();
        pedido1.setCliente(cliente1);
        pedido1.setRestaurante(restaurante1);
        pedido1.setStatus(StatusPedido.CONFIRMADO);
        pedido1.setValorTotal(new BigDecimal("45.00"));
        pedido1.setEnderecoCoberto("Rua A, 123 - São Paulo/SP");
        pedido1.setObservacoes("Sem cebola");
        pedido1.setDataPedido(LocalDateTime.now());
        
        Pedido pedido2 = new Pedido();
        pedido2.setCliente(cliente2);
        pedido2.setRestaurante(restaurante2);
        pedido2.setStatus(StatusPedido.ENTREGUE);
        pedido2.setValorTotal(new BigDecimal("46.00"));
        pedido2.setEnderecoCoberto("Rua B, 456 - São Paulo/SP");
        pedido2.setObservacoes("Entrega rápida");
        pedido2.setDataPedido(LocalDateTime.now().minusHours(2));

        pedidoRepository.saveAll(Arrays.asList(pedido1, pedido2));
        
        // Testando consultas
        System.out.println("\n=== Teste de Consultas ===");
        
        System.out.println("\nClientes ativos:");
        clienteRepository.findByAtivoTrue().forEach(c -> System.out.println(c.getNome()));
        
        System.out.println("\nRestaurantes com taxa <= R$5.00:");
        restauranteRepository.findByTaxaEntregaLessThanEqual(new BigDecimal("5.00"))
            .forEach(r -> System.out.println(r.getNome()));
        
        System.out.println("\nProdutos disponíveis:");
        produtoRepository.findByDisponivelTrue().forEach(p -> System.out.println(p.getNome()));
        
        System.out.println("\nÚltimos 10 pedidos:");
        pedidoRepository.findTop10ByOrderByDataPedidoDesc()
            .forEach(p -> System.out.println("Pedido: " + p.getId() + " - Cliente: " + p.getCliente().getNome()));
    }
}