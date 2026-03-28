package com.estebanruano.kinalapp.repository;


import com.estebanruano.kinalapp.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//Este es el repositorio
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByEstado(int estado);

}
