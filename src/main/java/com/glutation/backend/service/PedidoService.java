package com.glutation.backend.service;

import com.glutation.backend.entity.Pedido;
import com.glutation.backend.entity.User;
import com.glutation.backend.entity.Producto;
import com.glutation.backend.entity.PedidoProducto;
import com.glutation.backend.entity.PedidoProductoId;
import com.glutation.backend.repository.PedidoRepository;
import com.glutation.backend.repository.UserRepository;
import com.glutation.backend.repository.ProductoRepository;
import com.glutation.backend.repository.PedidoProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UserRepository userRepository;
    private final ProductoRepository productoRepository;
    private final PedidoProductoRepository pedidoProductoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, 
                         UserRepository userRepository, 
                         ProductoRepository productoRepository,
                         PedidoProductoRepository pedidoProductoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.userRepository = userRepository;
        this.productoRepository = productoRepository;
        this.pedidoProductoRepository = pedidoProductoRepository;
    }

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> getPedidoById(Integer id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido createPedido(Pedido pedidoRequest) {
        User user = userRepository.findById(pedidoRequest.getUser().getId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setUser(user);
        nuevoPedido.setPedidoFecha(pedidoRequest.getPedidoFecha() != null ? pedidoRequest.getPedidoFecha() : LocalDateTime.now());
        nuevoPedido.setValorTotal(pedidoRequest.getValorTotal());
        // IMPORTANTE: No establecer la lista de pedidoProductos aquí todavía

        Pedido savedPedido = pedidoRepository.save(nuevoPedido); // Guardar pedido SIN la lista de productos

        List<PedidoProducto> productosGuardados = new ArrayList<>();
        if (pedidoRequest.getPedidoProductos() != null && !pedidoRequest.getPedidoProductos().isEmpty()) {
            for (PedidoProducto ppDTO : pedidoRequest.getPedidoProductos()) { // Iterar sobre los DTOs/objetos del request
                Producto producto = productoRepository.findById(ppDTO.getProducto().getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto not found: " + ppDTO.getProducto().getProductoId()));

                PedidoProducto pedidoProductoEntidad = new PedidoProducto(); // Crear NUEVA entidad
                pedidoProductoEntidad.setId(new PedidoProductoId(savedPedido.getPedidoId(), producto.getProductoId()));
                pedidoProductoEntidad.setPedido(savedPedido); // Asignar el pedido recién guardado
                pedidoProductoEntidad.setProducto(producto);
                pedidoProductoEntidad.setCantidad(ppDTO.getCantidad());
                
                pedidoProductoRepository.save(pedidoProductoEntidad); // Guardar la nueva entidad
                productosGuardados.add(pedidoProductoEntidad); // Agregar a una lista para la respuesta
            }
        }
        
        savedPedido.setPedidoProductos(productosGuardados); // Asignar la lista de entidades persistidas al pedido para la respuesta
        return savedPedido; 
    }

    @Transactional
    public Optional<Pedido> updatePedido(Integer id, Pedido pedidoDetails) {
        return pedidoRepository.findById(id)
            .map(pedido -> {
                // Update user if changed
                if (pedidoDetails.getUser() != null && pedidoDetails.getUser().getId() != null) {
                    User user = userRepository.findById(pedidoDetails.getUser().getId())
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + pedidoDetails.getUser().getId()));
                    pedido.setUser(user);
                }
                pedido.setPedidoFecha(pedidoDetails.getPedidoFecha() != null ? pedidoDetails.getPedidoFecha() : pedido.getPedidoFecha());
                pedido.setValorTotal(pedidoDetails.getValorTotal());

                // Clear existing PedidoProductos and add new/updated ones
                // This is a simple approach; a more sophisticated diff might be needed for performance
                if (pedidoDetails.getPedidoProductos() != null) {
                    // Remove old associations not present in the new list
                    pedido.getPedidoProductos().removeIf(existingPP -> 
                        pedidoDetails.getPedidoProductos().stream().noneMatch(newPP -> 
                            newPP.getId() != null && newPP.getId().equals(existingPP.getId())
                        )
                    );
                    // Add or update new associations
                    pedidoDetails.getPedidoProductos().forEach(ppDetails -> {
                        Producto producto = productoRepository.findById(ppDetails.getProducto().getProductoId())
                            .orElseThrow(() -> new RuntimeException("Producto not found with id: " + ppDetails.getProducto().getProductoId()));
                        
                        PedidoProductoId ppId = new PedidoProductoId(pedido.getPedidoId(), producto.getProductoId());
                        
                        PedidoProducto pp = pedido.getPedidoProductos().stream()
                            .filter(currentPP -> currentPP.getId().equals(ppId))
                            .findFirst()
                            .orElseGet(() -> {
                                PedidoProducto newPP = new PedidoProducto();
                                newPP.setId(ppId);
                                newPP.setPedido(pedido);
                                newPP.setProducto(producto);
                                return newPP;
                            });
                        pp.setCantidad(ppDetails.getCantidad());
                        if (!pedido.getPedidoProductos().contains(pp)) {
                           pedido.getPedidoProductos().add(pp); // Add if new
                        }
                         pedidoProductoRepository.save(pp); // Ensure changes are persisted
                    });
                }
                return pedidoRepository.save(pedido);
            });
    }

    @Transactional
    public boolean deletePedido(Integer id) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(id);
        if (pedidoOptional.isPresent()) {
            Pedido pedido = pedidoOptional.get();
            // Manually delete associated PedidoProducto entries because of CascadeType issues with composite keys sometimes
            pedidoProductoRepository.deleteAll(pedido.getPedidoProductos());
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 