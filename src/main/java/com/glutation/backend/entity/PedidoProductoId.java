package com.glutation.backend.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // Important for composite keys
public class PedidoProductoId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_pedido")
    private Integer pedidoId;

    @Column(name = "id_producto")
    private Integer productoId;
} 