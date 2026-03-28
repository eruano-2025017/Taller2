package com.estebanruano.kinalapp.service;

import com.estebanruano.kinalapp.entity.DetalleVenta;

import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {

    List<DetalleVenta> ListarTodos();

    DetalleVenta guardar(DetalleVenta detalleVenta);

    Optional<DetalleVenta> buscarPorCODIGO(Long codigoDeVe);


    DetalleVenta actualizar(Long codigoDeVe, DetalleVenta detalleVenta);

    void eliminar(Long codigoDeVe);

    boolean existePorCODIGO(Long codigoDeVe);

}
