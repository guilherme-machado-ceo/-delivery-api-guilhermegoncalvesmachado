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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
@Tag(name = "Restaurantes", description = "Operações relacionadas aos restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @PostMapping
    @Operation(summary = "Cadastrar restaurante", description = "Cria um novo restaurante no sistema")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Restaurante já existe")
    })
    public ResponseEntity<ApiResponse<RestauranteResponseDTO>> criar(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do restaurante a ser criado", required = true) RestauranteDTO restauranteDTO) {
        RestauranteResponseDTO response = restauranteService.criar(restauranteDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Restaurante criado com sucesso"));
    }

    @GetMapping
    @Operation(summary = "Listar restaurantes", description = "Lista restaurantes com filtros opcionais e paginação")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    public ResponseEntity<PagedResponse<RestauranteResponseDTO>> listarTodos(
            @Parameter(description = "Texto para busca em nome, categoria ou endereço") @RequestParam(required = false) String busca,
            @Parameter(description = "Filtrar por categoria específica") @RequestParam(required = false) String categoria,
            @Parameter(description = "Filtrar por status ativo/inativo") @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable,
            HttpServletRequest request) {

        Page<RestauranteResponseDTO> restaurantesPage = restauranteService.listarTodosPaginado(busca, categoria, ativo,
                pageable);
        PagedResponse<RestauranteResponseDTO> response = PagedResponse.of(restaurantesPage,
                request.getRequestURL().toString());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID", description = "Retorna os dados de um restaurante específico")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponse<RestauranteResponseDTO>> buscarPorId(
            @Parameter(description = "ID do restaurante", required = true) @PathVariable Long id) {
        RestauranteResponseDTO restaurante = restauranteService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.success(restaurante));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante existente")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponse<RestauranteResponseDTO>> atualizar(
            @Parameter(description = "ID do restaurante", required = true) @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados atualizados do restaurante", required = true) RestauranteDTO restauranteDTO) {
        RestauranteResponseDTO response = restauranteService.atualizar(id, restauranteDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Restaurante atualizado com sucesso"));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status do restaurante", description = "Ativa ou desativa um restaurante")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponse<RestauranteResponseDTO>> alterarStatus(
            @Parameter(description = "ID do restaurante", required = true) @PathVariable Long id) {
        RestauranteResponseDTO response = restauranteService.alterarStatus(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Status alterado com sucesso"));
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar por categoria", description = "Lista restaurantes de uma categoria específica")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de restaurantes da categoria"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Categoria inválida")
    })
    public ResponseEntity<ApiResponse<List<RestauranteResponseDTO>>> buscarPorCategoria(
            @Parameter(description = "Nome da categoria", required = true) @PathVariable String categoria) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(ApiResponse.success(restaurantes));
    }

    @GetMapping("/busca")
    @Operation(summary = "Buscar por nome", description = "Busca restaurantes que contenham o nome informado")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de restaurantes encontrados"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Nome inválido")
    })
    public ResponseEntity<ApiResponse<List<RestauranteResponseDTO>>> buscarPorNome(
            @Parameter(description = "Nome ou parte do nome do restaurante", required = true) @RequestParam String nome) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorNome(nome);
        return ResponseEntity.ok(ApiResponse.success(restaurantes));
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar restaurantes ativos", description = "Lista apenas restaurantes que estão ativos")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de restaurantes ativos")
    })
    public ResponseEntity<ApiResponse<List<RestauranteResponseDTO>>> listarAtivos() {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarRestaurantesDisponiveis();
        return ResponseEntity.ok(ApiResponse.success(restaurantes));
    }

    @GetMapping("/{id}/taxa-entrega/{cep}")
    @Operation(summary = "Calcular taxa de entrega", description = "Calcula a taxa de entrega de um restaurante para um CEP específico")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Taxa de entrega calculada com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "CEP inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponse<TaxaEntregaResponse>> calcularTaxaEntrega(
            @Parameter(description = "ID do restaurante", required = true) @PathVariable Long id,
            @Parameter(description = "CEP de destino (formato: 12345-678 ou 12345678)", required = true) @PathVariable String cep) {
        TaxaEntregaResponse taxaResponse = restauranteService.calcularTaxaEntregaCompleta(id, cep);
        return ResponseEntity.ok(ApiResponse.success(taxaResponse, "Taxa de entrega calculada com sucesso"));
    }

    @GetMapping("/proximos/{cep}")
    @Operation(summary = "Buscar restaurantes próximos", description = "Lista restaurantes próximos a um CEP específico, ordenados por distância")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de restaurantes próximos retornada com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "CEP inválido")
    })
    public ResponseEntity<ApiResponse<List<RestauranteResponseDTO>>> buscarRestaurantesProximos(
            @Parameter(description = "CEP de referência (formato: 12345-678 ou 12345678)", required = true) @PathVariable String cep) {
        List<RestauranteResponseDTO> restaurantesProximos = restauranteService.buscarRestaurantesProximos(cep);
        return ResponseEntity.ok(ApiResponse.success(restaurantesProximos,
                String.format("Encontrados %d restaurantes próximos ao CEP %s", restaurantesProximos.size(), cep)));
    }
}