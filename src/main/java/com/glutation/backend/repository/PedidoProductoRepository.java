package com.glutation.backend.repository;

import com.glutation.backend.entity.PedidoProducto;
import com.glutation.backend.entity.PedidoProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoProductoRepository extends JpaRepository<PedidoProducto, PedidoProductoId> {
} 