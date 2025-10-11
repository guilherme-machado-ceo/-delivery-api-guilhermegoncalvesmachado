package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.dto.RestauranteResponseDTO;
import com.deliverytech.delivery.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {
    
    @Autowired
    private RestauranteService restauranteService;
    
    @PostMapping
    public ResponseEntity<RestauranteResponseDTO> criar(@Valid @RequestBody RestauranteDTO restauranteDTO) {
        RestauranteResponseDTO response = restauranteService.criar(restauranteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<RestauranteResponseDTO>> listarTodos(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean ativo) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.listarTodos(busca, categoria, ativo);
        return ResponseEntity.ok(restaurantes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> buscarPorId(@PathVariable Long id) {
        RestauranteResponseDTO restaurante = restauranteService.buscarPorId(id);
        return ResponseEntity.ok(restaurante);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> atualizar(@PathVariable Long id, 
                                                           @Valid @RequestBody RestauranteDTO restauranteDTO) {
        RestauranteResponseDTO response = restauranteService.atualizar(id, restauranteDTO);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<RestauranteResponseDTO> alterarStatus(@PathVariable Long id) {
        RestauranteResponseDTO response = restauranteService.alterarStatus(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(restaurantes);
    }
    
    @GetMapping("/busca")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorNome(nome);
        return ResponseEntity.ok(restaurantes);
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<RestauranteResponseDTO>> listarAtivos() {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarRestaurantesDisponiveis();
        return ResponseEntity.ok(restaurantes);
    }
}