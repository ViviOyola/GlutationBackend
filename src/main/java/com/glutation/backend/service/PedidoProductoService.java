package com.glutation.backend.service;

import com.glutation.backend.entity.PedidoProducto;
import com.glutation.backend.entity.PedidoProductoId;
import com.glutation.backend.repository.PedidoProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoProductoService {

    private final PedidoProductoRepository pedidoProductoRepository;

    @Autowired
    public PedidoProductoService(PedidoProductoRepository pedidoProductoRepository) {
        this.pedidoProductoRepository = pedidoProductoRepository;
    }

    public List<PedidoProducto> getAllPedidoProductos() {
        return pedidoProductoRepository.findAll();
    }

    public Optional<PedidoProducto> getPedidoProductoById(PedidoProductoId id) {
        return pedidoProductoRepository.findById(id);
    }

    public PedidoProducto createPedidoProducto(PedidoProducto pedidoProducto) {
        // Logic for creation, ensuring Pedido and Producto exist, might be better handled in PedidoService
        return pedidoProductoRepository.save(pedidoProducto);
    }

    public Optional<PedidoProducto> updatePedidoProducto(PedidoProductoId id, PedidoProducto pedidoProductoDetails) {
        return pedidoProductoRepository.findById(id)
            .map(pedidoProducto -> {
                pedidoProducto.setCantidad(pedidoProductoDetails.getCantidad());
                // Potentially update Pedido or Producto references if that makes sense, 
                // but typically the ID (and thus Pedido/Producto refs) are immutable for a join table entry.
                return pedidoProductoRepository.save(pedidoProducto);
            });
    }

    public boolean deletePedidoProducto(PedidoProductoId id) {
        if (pedidoProductoRepository.existsById(id)) {
            pedidoProductoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Custom methods for finding by pedidoId or productoId might be useful here
    // e.g., List<PedidoProducto> findByPedidoId(Integer pedidoId);
} 