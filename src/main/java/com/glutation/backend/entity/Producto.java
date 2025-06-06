package com.glutation.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @Column(name = "producto_id")
    private Integer productoId;

    private String nombre;
    private String medida;

    @Column(name = "description")
    private String description;

    private String valor;
    private String marca;

    @Column(name = "image_name")
    private String imageName;

    @OneToMany(mappedBy = "producto")
    @JsonManagedReference("producto-pedidoproductos")
    private List<PedidoProducto> pedidoProductos;
} 