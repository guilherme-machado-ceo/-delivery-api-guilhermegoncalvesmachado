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
<<<<<<< HEAD
@Tag(name = "Produtos", description = "Operações relacionadas aos produtos")
=======
@Tag(name = "Produtos", description = "Operações relacionadas aos produtos dos restaurantes")
@SecurityRequirement(name = "bearerAuth")
>>>>>>> 1c33e88b00fd58aa6bf4585048c49bbf656f9156
public class ProdutoController {
    
    @Autowired
    private ProdutoService produtoService;
    
    @PostMapping
<<<<<<< HEAD
    @Operation(summary = "Cadastrar produto", description = "Cria um novo produto no sistema")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> criar(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do produto a ser criado", required = true) ProdutoDTO produtoDTO) {
        ProdutoResponseDTO response = produtoService.criar(produtoDTO);
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
            @Parameter(description = "ID do produto", required = true) @PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.buscarPorIdResponse(id);
=======
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
>>>>>>> 1c33e88b00fd58aa6bf4585048c49bbf656f9156
        return ResponseEntity.ok(ApiResponse.success(produto));
    }
    
    @PutMapping("/{id}")
<<<<<<< HEAD
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> atualizar(
            @Parameter(description = "ID do produto", required = true) @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados atualizados do produto", required = true) ProdutoDTO produtoDTO) {
        ProdutoResponseDTO response = produtoService.atualizar(id, produtoDTO);
=======
    @PreAuthorize("hasRole('ADMIN') or @produtoService.isOwner(#id)")
    @Operation(summary = "Atualizar produto", description = "Atualiza um produto. ADMIN pode atualizar qualquer um, RESTAURANTE apenas o seu.")
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoDTO produtoDTO) {
        ProdutoResponseDTO response = produtoService.atualizarProduto(id, produtoDTO);
>>>>>>> 1c33e88b00fd58aa6bf4585048c49bbf656f9156
        return ResponseEntity.ok(ApiResponse.success(response, "Produto atualizado com sucesso"));
    }
    
    @DeleteMapping("/{id}")
<<<<<<< HEAD
    @Operation(summary = "Remover produto", description = "Remove um produto do sistema")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Produto não pode ser removido (possui pedidos)")
    })
    public ResponseEntity<Void> remover(
            @Parameter(description = "ID do produto", required = true) @PathVariable Long id) {
        produtoService.remover(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/disponibilidade")
    @Operation(summary = "Alterar disponibilidade", description = "Altera a disponibilidade de um produto")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Disponibilidade alterada com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> alterarDisponibilidade(
            @Parameter(description = "ID do produto", required = true) @PathVariable Long id) {
        ProdutoResponseDTO response = produtoService.alterarDisponibilidade(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Disponibilidade alterada com sucesso"));
    }
    
    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Listar produtos do restaurante", description = "Lista todos os produtos de um restaurante específico")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de produtos retornada"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> listarPorRestaurante(
            @Parameter(description = "ID do restaurante", required = true) @PathVariable Long restauranteId,
            @Parameter(description = "Filtrar apenas produtos disponíveis") @RequestParam(required = false) Boolean disponivel) {
        List<ProdutoResponseDTO> produtos = produtoService.listarPorRestaurante(restauranteId, disponivel);
=======
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
>>>>>>> 1c33e88b00fd58aa6bf4585048c49bbf656f9156
        return ResponseEntity.ok(ApiResponse.success(produtos));
    }
    
    @GetMapping("/categoria/{categoria}")
<<<<<<< HEAD
    @Operation(summary = "Buscar por categoria", description = "Lista produtos de uma categoria específica")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de produtos da categoria"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Categoria inválida")
    })
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> buscarPorCategoria(
            @Parameter(description = "Nome da categoria", required = true) @PathVariable String categoria) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarPorCategoria(categoria);
=======
    @PreAuthorize("permitAll()")
    @Operation(summary = "Buscar por categoria", description = "Endpoint público para listar produtos de uma categoria específica.")
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> buscarPorCategoria(@PathVariable String categoria) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorCategoria(categoria);
>>>>>>> 1c33e88b00fd58aa6bf4585048c49bbf656f9156
        return ResponseEntity.ok(ApiResponse.success(produtos));
    }
    
    @GetMapping("/buscar")
<<<<<<< HEAD
    @Operation(summary = "Buscar por nome", description = "Busca produtos que contenham o nome informado")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de produtos encontrados"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Nome inválido")
    })
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> buscarPorNome(
            @Parameter(description = "Nome ou parte do nome do produto", required = true) @RequestParam String nome) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarPorNome(nome);
=======
    @PreAuthorize("permitAll()")
    @Operation(summary = "Buscar produtos por nome", description = "Endpoint público para buscar produtos que contenham o nome informado.")
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> buscarPorNome(@RequestParam String nome) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorNome(nome);
>>>>>>> 1c33e88b00fd58aa6bf4585048c49bbf656f9156
        return ResponseEntity.ok(ApiResponse.success(produtos));
    }
}
