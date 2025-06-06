package com.glutation.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pedido_id")
    private Integer pedidoId;

    @Column(name = "pedido_fecha", nullable = false)
    private LocalDateTime pedidoFecha;

    @ManyToOne
    @JoinColumn(name = "pedido_usuario", nullable = false)
    @JsonBackReference("user-pedidos")
    private User user;

    @Column(name = "valor_total", nullable = false)
    private String valorTotal; // Consider using BigDecimal for monetary values

    @OneToMany(mappedBy = "pedido", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
    @JsonManagedReference("pedido-productopedido")
    private List<PedidoProducto> pedidoProductos = new ArrayList<>();
} 