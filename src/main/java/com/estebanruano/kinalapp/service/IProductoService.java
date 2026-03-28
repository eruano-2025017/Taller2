package com.estebanruano.kinalapp.service;

import com.estebanruano.kinalapp.entity.Producto;

import java.util.List;
import java.util.Optional;
//.
//Esta es la interfaz
public interface IProductoService {

    List<Producto> listarTodos();

    Producto guardar(Producto producto);

    Optional<Producto> buscarPorCODIGO(Long codigoPro);

    Producto actualizar(Long codigoPro, Producto producto);

    void eliminar(Long codigoPro);

    boolean existePorCODIGO(Long codigoPro);

    List<Producto> listPorEstado(int estado);

}
