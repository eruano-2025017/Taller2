package com.estebanruano.kinalapp.service;

import com.estebanruano.kinalapp.entity.Cliente;
import com.estebanruano.kinalapp.entity.Usuario;

import java.util.List;
import java.util.Optional;
//.
//Esta es la intefaz

public interface IUsuarioService {

    List<Usuario> ListarTodos();

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorCODIGO(Long codigoUsu);

    Usuario actualizar(Long codigoUsu, Usuario usuario);

    void eliminar(Long codigoUsu);

    boolean existePorCODIGO(Long codigoUsu);

    List<Usuario> listPorEstado(int estado);
}
