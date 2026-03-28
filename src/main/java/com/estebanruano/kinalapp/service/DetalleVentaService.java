package com.estebanruano.kinalapp.service;

import com.estebanruano.kinalapp.entity.*;
import com.estebanruano.kinalapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetalleVentaService implements IDetalleVentaService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository ) { this.detalleVentaRepository = detalleVentaRepository; }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> ListarTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        Venta venta = ventaRepository.findById(detalleVenta.getVenta().getCODIGOVenta()).orElse(null);
        Producto producto = productoRepository.findById(detalleVenta.getProducto().getCODIGOProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        detalleVenta.setVenta(venta);
        detalleVenta.setProducto(producto);

        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorCODIGO(Long codigoDeVe) {
        return detalleVentaRepository.findById(codigoDeVe);
    }


    @Override
    public DetalleVenta actualizar(Long codigoDeVe, DetalleVenta detalleVenta) {
        if(!detalleVentaRepository.existsById(codigoDeVe)){
            throw new RuntimeException("El producto no se encontró con CODIGO " + codigoDeVe);

        }

        detalleVenta.setCodigoDetalleVenta(codigoDeVe);
        validarDetalleVenta(detalleVenta);

        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public void eliminar(Long codigoDeVe) {
        if(!detalleVentaRepository.existsById(codigoDeVe)){
            throw new RuntimeException("Dettale venta no se encontró con el CODIGO " +codigoDeVe);
        }
        detalleVentaRepository.deleteById(codigoDeVe);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCODIGO(Long codigoDeVe) {
        return detalleVentaRepository.existsById(codigoDeVe);
        //retorna true o false
    }

    private void validarDetalleVenta(DetalleVenta detalleVenta){


        if (detalleVenta.getCantidad() == 0){
            throw new IllegalArgumentException("L cantidad no puede ser nulo es un dato obligatorio");
        }

        if (detalleVenta.getPrecioUnitario()== null){
            throw new IllegalArgumentException("El precio unitario no puede ser nulo");
        }

        if (detalleVenta.getSubTotal() == null){
            throw new IllegalArgumentException("El SubTotal no puede ser nulo");
        }

    }

}
