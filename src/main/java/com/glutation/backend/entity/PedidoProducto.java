package com.glutation.backend.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "pedido_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("id")
public class PedidoProducto {

    @EmbeddedId
    private PedidoProductoId id;

    @ManyToOne
    @MapsId("pedidoId") // Maps pedidoId from PedidoProductoId
    @JoinColumn(name = "id_pedido")
    @JsonBackReference("pedido-productopedido") // Matched name
    private Pedido pedido; // Este campo lo llenaremos en el servicio

    @ManyToOne
    @MapsId("productoId") // Maps productoId from PedidoProductoId
    @JoinColumn(name = "id_producto")
    @JsonBackReference("producto-pedidoproductos") // Added for Producto.pedidoProductos
    private Producto producto; // Jackson debería poder deserializar esto desde el JSON anidado

    @Column(nullable = false)
    private Integer cantidad;
} 