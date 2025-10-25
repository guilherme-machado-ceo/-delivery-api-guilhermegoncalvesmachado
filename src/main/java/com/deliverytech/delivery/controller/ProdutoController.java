package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ApiResponse;
import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.dto.ProdutoResponseDTO;
import com.deliverytech.delivery.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Operações relacionadas aos produtos dos restaurantes")
@SecurityRequirement(name = "bearerAuth")
public class ProdutoController {
    
    @Autowired
    private ProdutoService produtoService;
    
    @PostMapping
    @PreAuthorize("hasRole('RESTAURANTE') or hasRole('ADMIN')")
    @Operation(summary = "Cadastrar produto", description = "Cria um novo produto. Requer perfil de RESTAURANTE ou ADMIN.")
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> criar(@Valid @RequestBody ProdutoDTO produtoDTO) {
        ProdutoResponseDTO response = produtoService.cadastrarProduto(produtoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "Produto criado com sucesso"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Buscar produto por ID", description = "Endpoint público para retornar os dados de um produto específico.")
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> buscarPorId(@PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.buscarProdutoPorId(id);
        return ResponseEntity.ok(ApiResponse.success(produto));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @produtoService.isOwner(#id)")
    @Operation(summary = "Atualizar produto", description = "Atualiza um produto. ADMIN pode atualizar qualquer um, RESTAURANTE apenas o seu.")
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoDTO produtoDTO) {
        ProdutoResponseDTO response = produtoService.atualizarProduto(id, produtoDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Produto atualizado com sucesso"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @produtoService.isOwner(#id)")
    @Operation(summary = "Remover produto", description = "Remove um produto (soft delete). ADMIN pode remover qualquer um, RESTAURANTE apenas o seu.")
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> remover(@PathVariable Long id) {
        ProdutoResponseDTO response = produtoService.alterarDisponibilidade(id, false);
        return ResponseEntity.ok(ApiResponse.success(response, "Produto removido com sucesso"));
    }
    
    @PatchMapping("/{id}/disponibilidade")
    @PreAuthorize("hasRole('ADMIN') or @produtoService.isOwner(#id)")
    @Operation(summary = "Alterar disponibilidade", description = "Alterna a disponibilidade de um produto. ADMIN pode alterar qualquer um, RESTAURANTE apenas o seu.")
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> alterarDisponibilidade(@PathVariable Long id) {
        ProdutoResponseDTO produtoAtual = produtoService.buscarProdutoPorId(id);
        boolean novaDisponibilidade = !produtoAtual.getDisponivel();
        ProdutoResponseDTO response = produtoService.alterarDisponibilidade(id, novaDisponibilidade);
        String mensagem = novaDisponibilidade ? "Produto disponibilizado" : "Produto indisponibilizado";
        return ResponseEntity.ok(ApiResponse.success(response, mensagem));
    }
    
    @GetMapping("/restaurante/{restauranteId}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Listar produtos do restaurante", description = "Endpoint público para listar todos os produtos disponíveis de um restaurante.")
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> listarPorRestaurante(@PathVariable Long restauranteId) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorRestaurante(restauranteId);
        return ResponseEntity.ok(ApiResponse.success(produtos));
    }
    
    @GetMapping("/categoria/{categoria}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Buscar por categoria", description = "Endpoint público para listar produtos de uma categoria específica.")
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> buscarPorCategoria(@PathVariable String categoria) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorCategoria(categoria);
        return ResponseEntity.ok(ApiResponse.success(produtos));
    }
    
    @GetMapping("/buscar")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Buscar produtos por nome", description = "Endpoint público para buscar produtos que contenham o nome informado.")
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> buscarPorNome(@RequestParam String nome) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorNome(nome);
        return ResponseEntity.ok(ApiResponse.success(produtos));
    }
}
