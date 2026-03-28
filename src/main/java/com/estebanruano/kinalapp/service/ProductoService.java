package com.estebanruano.kinalapp.service;


import com.estebanruano.kinalapp.entity.Cliente;
import com.estebanruano.kinalapp.entity.Producto;
import com.estebanruano.kinalapp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
//.
//Este es el servicio
@Service
@Transactional
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;

    /*
     * Constructor: Este se ejecuta al crear el objeto
     * Parámetros: Spring pasa el repositorio automáticamente y a esto se le conoce
     * como Inyección de Dependencias
     * Asignamos el repositorio a nuestra variable de clase
     * */

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /*
     * @Override: Indicar que estamos implementando un metodo de la interfaz
     * */
    @Override
    /*
     * readOnly = true: Lo que hace es optimizar la consulta, no bloquea la BD
     * */
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
        /*
         * Llama a l mértodo findAll() del repositorio de Spring Data JPA
         * este metodo hace exactamente el Select * from Clientes
         * */
    }

    @Override
    public Producto guardar(Producto producto) {
        /*
         * Metodo de guardar crea un Cliente
         * Acá es donde colocamos la l+ogica del negocio Antes de guardar
         * Primero validamos el dato
         * */
        validarProducto(producto);
        if (producto.getEstado() == 0)
            producto.setEstado(1);
        return productoRepository.save(producto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorCODIGO(Long codigoPro) {
        //Buscar un cliente por DPI
        return productoRepository.findById(codigoPro);
        //Optional nos evitaa el NullPointerException
    }


    @Override
    public Producto actualizar(Long codigoPro, Producto producto) {
        //Actualiza un cliente existente
        if(!productoRepository.existsById(codigoPro)){
            throw new RuntimeException("El producto no se encontró con CODIGO " + codigoPro);
            //Si no existe, se lanza una excepción (error controlado)
        }
        /*
         * 1. Asegurar que el DPI del objeto coincida con el de la URL
         * 2. por seguridad usamos el DPI de la URL y no el que viene en el JSON
         * */
        producto.setCODIGOProducto(codigoPro);
        validarProducto(producto);

        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Long codigoPro) {
        //Eliminar un cliente
        if(!productoRepository.existsById(codigoPro)){
            throw new RuntimeException("El Producto no se encontró con el CODIGO " +codigoPro);
        }
        productoRepository.deleteById(codigoPro);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCODIGO(Long codigoPro) {
        //
        return productoRepository.existsById(codigoPro);
        //retorna true o false
    }

    //Metodo privado(solo puede utilizarse dentro de la clase)
    private void validarProducto(Producto producto){
        /*
         * Validaciones del negocio: Este metodo se hará privado porque
         * es algo interno del servicio
         * */

        if (producto.getNombreProducto()== null || producto.getNombreProducto().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre es un dato obligarotio");
        }

        if (producto.getPrecio() == null){
            throw new IllegalArgumentException("El precio es un dato obligatorio");
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listPorEstado(int estado){
        return productoRepository.findByEstado(estado);
    }
}
