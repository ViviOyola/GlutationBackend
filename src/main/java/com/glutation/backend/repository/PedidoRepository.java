package com.glutation.backend.repository;

import com.glutation.backend.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
} 