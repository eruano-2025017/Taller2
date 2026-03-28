package com.estebanruano.kinalapp.service;

import com.estebanruano.kinalapp.entity.Usuario;
import com.estebanruano.kinalapp.entity.Venta;

import java.util.List;
import java.util.Optional;

public interface IVentaService {

    List<Venta> ListarTodos();

    Venta guardar(Venta venta);

    Optional<Venta> buscarPorCODIGO(Long codigoVen);

    Venta actualizar(Long codigoVen, Venta venta);

    void eliminar(Long codigoVen);

    boolean existePorCODIGO(Long codigoVen);

    List<Venta> listPorEstado(int estado);
}
