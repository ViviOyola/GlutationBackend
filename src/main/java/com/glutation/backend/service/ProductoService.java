package com.glutation.backend.service;

import com.glutation.backend.entity.Producto;
import com.glutation.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> getProductoById(Integer id) {
        return productoRepository.findById(id);
    }

    public Producto createProducto(Producto producto) {
        // Add any validation or business logic before saving
        return productoRepository.save(producto);
    }

    public Optional<Producto> updateProducto(Integer id, Producto productoDetails) {
        return productoRepository.findById(id)
            .map(producto -> {
                producto.setNombre(productoDetails.getNombre());
                producto.setMedida(productoDetails.getMedida());
                producto.setDescription(productoDetails.getDescription());
                producto.setValor(productoDetails.getValor());
                producto.setMarca(productoDetails.getMarca());
                producto.setImageName(productoDetails.getImageName());
                return productoRepository.save(producto);
            });
    }

    public boolean deleteProducto(Integer id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 