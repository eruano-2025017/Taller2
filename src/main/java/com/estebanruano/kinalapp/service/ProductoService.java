package com.estebanruano.kinalapp.service;

import com.estebanruano.kinalapp.entity.Producto;
import com.estebanruano.kinalapp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto guardar(Producto producto) {
        validarProducto(producto);
        if (producto.getEstado() == 0)
            producto.setEstado(1);
        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorCODIGO(Long codigoPro) {
        return productoRepository.findById(codigoPro);
    }

    @Override
    public Producto actualizar(Long codigoPro, Producto producto) {
        if (!productoRepository.existsById(codigoPro)) {
            throw new RuntimeException("El producto no se encontró con CODIGO " + codigoPro);
        }
        producto.setCODIGOProducto(codigoPro);
        validarProducto(producto);
        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Long codigoPro) {
        Producto producto = productoRepository.findById(codigoPro)
                .orElseThrow(() -> new RuntimeException("El Producto no se encontró con el CODIGO " + codigoPro));

        producto.setEstado(0);
        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCODIGO(Long codigoPro) {
        return productoRepository.existsById(codigoPro);
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombreProducto() == null || producto.getNombreProducto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es un dato obligatorio");
        }
        if (producto.getPrecio() == null) {
            throw new IllegalArgumentException("El precio es un dato obligatorio");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listPorEstado(int estado) {
        return productoRepository.findByEstado(estado);
    }
}