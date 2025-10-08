package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.model.Produto;
import com.deliverytech.delivery.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    
    @Autowired
    private ProdutoService produtoService;
    
    @PostMapping("/restaurante/{restauranteId}")
    public ResponseEntity<Produto> criar(@PathVariable Long restauranteId, @RequestBody Produto produto) {
        return ResponseEntity.ok(produtoService.cadastrar(produto, restauranteId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }
    
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<Produto>> listarPorRestaurante(@PathVariable Long restauranteId) {
        return ResponseEntity.ok(produtoService.listarPorRestaurante(restauranteId));
    }
    
    @GetMapping("/restaurante/{restauranteId}/categoria/{categoria}")
    public ResponseEntity<List<Produto>> listarPorRestauranteECategoria(
            @PathVariable Long restauranteId,
            @PathVariable String categoria) {
        return ResponseEntity.ok(produtoService.listarPorRestauranteECategoria(restauranteId, categoria));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        return ResponseEntity.ok(produtoService.atualizar(id, produto));
    }
    
    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<Void> alterarDisponibilidade(@PathVariable Long id, @RequestParam boolean disponivel) {
        produtoService.alterarDisponibilidade(id, disponivel);
        return ResponseEntity.noContent().build();
    }
}