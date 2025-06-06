package com.glutation.backend.controller;

import com.glutation.backend.entity.Pedido;
import com.glutation.backend.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*") 
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Integer id) {
        return pedidoService.getPedidoById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) {
        try {
            Pedido createdPedido = pedidoService.createPedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPedido);
        } catch (RuntimeException e) {
            System.err.println("Error en createPedido: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable Integer id, @RequestBody Pedido pedidoDetails) {
        try {
            return pedidoService.updatePedido(id, pedidoDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Integer id) {
        if (pedidoService.deletePedido(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/testjson", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> testJson(@RequestBody JsonNode jsonPayload) {
        System.out.println("JSON Recibido en /testjson: " + jsonPayload.toString());
        return ResponseEntity.ok("JSON recibido: " + jsonPayload.toString());
    }

    @PostMapping(value = "/teststring", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> testString(@RequestBody String textPayload) {
        System.out.println("Texto Recibido en /teststring: " + textPayload);
        return ResponseEntity.ok("Texto recibido: " + textPayload);
    }
} 