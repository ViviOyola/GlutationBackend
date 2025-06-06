package com.glutation.backend.repository;

import com.glutation.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
} 