package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ApiResponse;
import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.dto.ProdutoResponseDTO;
import com.deliverytech.delivery.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Operações relacionadas aos produtos dos restaurantes")
public class ProdutoController {
    
    @Autowired
    private ProdutoService produtoService;
    
    @PostMapping
    @Operation(summary = "Cadastrar produto", description = "Cria um novo produto para um restaurante")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> criar(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do produto a ser criado", required = true
            ) ProdutoDTO produtoDTO) {
        ProdutoResponseDTO response = produtoService.cadastrarProduto(produtoDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Produto criado com sucesso"));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna os dados de um produto específico")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Produto encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> buscarPorId(
            @Parameter(description = "ID do produto", required = true)
            @PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.buscarProdutoPorId(id);
        return ResponseEntity.ok(ApiResponse.success(produto));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> atualizar(
            @Parameter(description = "ID do produto", required = true)
            @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados atualizados do produto", required = true
            ) ProdutoDTO produtoDTO) {
        ProdutoResponseDTO response = produtoService.atualizarProduto(id, produtoDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Produto atualizado com sucesso"));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover produto", description = "Remove um produto do sistema (soft delete - marca como indisponível)")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Produto removido com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> remover(
            @Parameter(description = "ID do produto", required = true)
            @PathVariable Long id) {
        ProdutoResponseDTO response = produtoService.alterarDisponibilidade(id, false);
        return ResponseEntity.ok(ApiResponse.success(response, "Produto removido com sucesso"));
    }
    
    @PatchMapping("/{id}/disponibilidade")
    @Operation(summary = "Alterar disponibilidade", description = "Alterna a disponibilidade de um produto")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Disponibilidade alterada com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> alterarDisponibilidade(
            @Parameter(description = "ID do produto", required = true)
            @PathVariable Long id) {
        // Buscar produto atual para alternar disponibilidade
        ProdutoResponseDTO produtoAtual = produtoService.buscarProdutoPorId(id);
        boolean novaDisponibilidade = !produtoAtual.getDisponivel();
        
        ProdutoResponseDTO response = produtoService.alterarDisponibilidade(id, novaDisponibilidade);
        String mensagem = novaDisponibilidade ? "Produto disponibilizado" : "Produto indisponibilizado";
        return ResponseEntity.ok(ApiResponse.success(response, mensagem));
    }
    
    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Listar produtos do restaurante", description = "Lista todos os produtos disponíveis de um restaurante")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de produtos do restaurante"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> listarPorRestaurante(
            @Parameter(description = "ID do restaurante", required = true)
            @PathVariable Long restauranteId) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorRestaurante(restauranteId);
        return ResponseEntity.ok(ApiResponse.success(produtos));
    }
    
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar por categoria", description = "Lista produtos de uma categoria específica")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de produtos da categoria"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Categoria inválida")
    })
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> buscarPorCategoria(
            @Parameter(description = "Nome da categoria", required = true)
            @PathVariable String categoria) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorCategoria(categoria);
        return ResponseEntity.ok(ApiResponse.success(produtos));
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar produtos por nome", description = "Busca produtos que contenham o nome informado")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de produtos encontrados"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Nome inválido")
    })
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> buscarPorNome(
            @Parameter(description = "Nome ou parte do nome do produto", required = true)
            @RequestParam String nome) {
        // Implementar busca por nome no service
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorNome(nome);
        return ResponseEntity.ok(ApiResponse.success(produtos));
    }
}