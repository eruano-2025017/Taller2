package com.estebanruano.kinalapp.repository;

import com.estebanruano.kinalapp.entity.Usuario;
import com.estebanruano.kinalapp.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByEstado(int estado);
}
