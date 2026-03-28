package com.estebanruano.kinalapp.repository;

import com.estebanruano.kinalapp.entity.Cliente;
import com.estebanruano.kinalapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//Este es el repositorio de Usuario
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByEstado(int estado);
}
