package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ApiResponse;
import com.deliverytech.delivery.dto.PagedResponse;
import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.dto.RestauranteResponseDTO;
import com.deliverytech.delivery.dto.TaxaEntregaResponse;
import com.deliverytech.delivery.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
@Tag(name = "Restaurantes", description = "Operações relacionadas aos restaurantes")
@SecurityRequirement(name = "bearerAuth") // Aplica segurança a todos os endpoints no Swagger
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cadastrar restaurante", description = "Cria um novo restaurante no sistema. Requer perfil de ADMIN.")
    public ResponseEntity<ApiResponse<RestauranteResponseDTO>> criar(@Valid @RequestBody RestauranteDTO restauranteDTO) {
        RestauranteResponseDTO response = restauranteService.criar(restauranteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "Restaurante criado com sucesso"));
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    @Operation(summary = "Listar restaurantes", description = "Endpoint público para listar restaurantes com filtros e paginação.")
    public ResponseEntity<PagedResponse<RestauranteResponseDTO>> listarTodos(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable,
            HttpServletRequest request) {
        Page<RestauranteResponseDTO> restaurantesPage = restauranteService.listarTodosPaginado(busca, categoria, ativo, pageable);
        PagedResponse<RestauranteResponseDTO> response = PagedResponse.of(restaurantesPage, request.getRequestURL().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Buscar restaurante por ID", description = "Endpoint público para retornar os dados de um restaurante específico.")
    public ResponseEntity<ApiResponse<RestauranteResponseDTO>> buscarPorId(@PathVariable Long id) {
        RestauranteResponseDTO restaurante = restauranteService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.success(restaurante));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('RESTAURANTE') and @restauranteService.isOwner(#id))")
    @Operation(summary = "Atualizar restaurante", description = "Atualiza um restaurante. ADMIN pode atualizar qualquer um, RESTAURANTE apenas o seu.")
    public ResponseEntity<ApiResponse<RestauranteResponseDTO>> atualizar(@PathVariable Long id, @Valid @RequestBody RestauranteDTO restauranteDTO) {
        RestauranteResponseDTO response = restauranteService.atualizar(id, restauranteDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Restaurante atualizado com sucesso"));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Alterar status do restaurante", description = "Ativa ou desativa um restaurante. Requer perfil de ADMIN.")
    public ResponseEntity<ApiResponse<RestauranteResponseDTO>> alterarStatus(@PathVariable Long id) {
        RestauranteResponseDTO response = restauranteService.alterarStatus(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Status alterado com sucesso"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar restaurante", description = "Deleta um restaurante do sistema. Requer perfil de ADMIN.")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        // A lógica de deleção seria implementada no service
        restauranteService.deletarRestaurante(id); // Supondo que este método exista
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categoria/{categoria}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Buscar por categoria", description = "Endpoint público para listar restaurantes de uma categoria específica.")
    public ResponseEntity<ApiResponse<List<RestauranteResponseDTO>>> buscarPorCategoria(@PathVariable String categoria) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(ApiResponse.success(restaurantes));
    }

    @GetMapping("/busca")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Buscar por nome", description = "Endpoint público para buscar restaurantes que contenham o nome informado.")
    public ResponseEntity<ApiResponse<List<RestauranteResponseDTO>>> buscarPorNome(@RequestParam String nome) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorNome(nome);
        return ResponseEntity.ok(ApiResponse.success(restaurantes));
    }

    @GetMapping("/ativos")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Listar restaurantes ativos", description = "Endpoint público para listar apenas restaurantes que estão ativos.")
    public ResponseEntity<ApiResponse<List<RestauranteResponseDTO>>> listarAtivos() {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarRestaurantesDisponiveis();
        return ResponseEntity.ok(ApiResponse.success(restaurantes));
    }

    @GetMapping("/{id}/taxa-entrega/{cep}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Calcular taxa de entrega", description = "Endpoint público para calcular a taxa de entrega de um restaurante para um CEP.")
    public ResponseEntity<ApiResponse<TaxaEntregaResponse>> calcularTaxaEntrega(@PathVariable Long id, @PathVariable String cep) {
        TaxaEntregaResponse taxaResponse = restauranteService.calcularTaxaEntregaCompleta(id, cep);
        return ResponseEntity.ok(ApiResponse.success(taxaResponse, "Taxa de entrega calculada com sucesso"));
    }

    @GetMapping("/proximos/{cep}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Buscar restaurantes próximos", description = "Endpoint público para listar restaurantes próximos a um CEP.")
    public ResponseEntity<ApiResponse<List<RestauranteResponseDTO>>> buscarRestaurantesProximos(@PathVariable String cep) {
        List<RestauranteResponseDTO> restaurantesProximos = restauranteService.buscarRestaurantesProximos(cep);
        return ResponseEntity.ok(ApiResponse.success(restaurantesProximos, String.format("Encontrados %d restaurantes próximos ao CEP %s", restaurantesProximos.size(), cep)));
    }
}
