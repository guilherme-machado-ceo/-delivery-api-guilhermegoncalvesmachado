package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {
    
    @Autowired
    private RestauranteService restauranteService;
    
    @PostMapping
    public ResponseEntity<Restaurante> criar(@RequestBody Restaurante restaurante) {
        return ResponseEntity.ok(restauranteService.cadastrar(restaurante));
    }
    
    @GetMapping
    public ResponseEntity<List<Restaurante>> listarTodos() {
        return ResponseEntity.ok(restauranteService.listarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.buscarPorId(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Restaurante> atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante) {
        return ResponseEntity.ok(restauranteService.atualizar(id, restaurante));
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> alterarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
        restauranteService.alterarStatus(id, ativo);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Restaurante>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(restauranteService.buscarPorCategoria(categoria));
    }
    
    @GetMapping("/busca")
    public ResponseEntity<List<Restaurante>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(restauranteService.buscarPorNome(nome));
    }
    
    @GetMapping("/avaliacoes")
    public ResponseEntity<List<Restaurante>> listarPorAvaliacao() {
        return ResponseEntity.ok(restauranteService.listarPorAvaliacao());
    }
}