package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.model.Pedido;
import com.deliverytech.delivery.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.criar(pedido));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.listarPorCliente(clienteId));
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, status));
    }
    
    @GetMapping("/periodo")
    public ResponseEntity<List<Pedido>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(pedidoService.buscarPorPeriodo(inicio, fim));
    }
    
    @GetMapping("/cliente/{clienteId}/historico")
    public ResponseEntity<List<Pedido>> historicoCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.buscarPedidosClienteOrdenadosPorData(clienteId));
    }
}