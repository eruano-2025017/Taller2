package com.estebanruano.kinalapp.service;

import com.estebanruano.kinalapp.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    //Interfaz: Es un contrato que dice QUÉ métodos debe tener
    //cualquier servicio de Clientes, No tiene
    //implementación, solo la definición de los métodos

    //Metodo que devuelve una lista de todos los clientes
    List<Cliente> listarTodos();
    //List<Cliente> lo que hace es devolver una lista
    //de objetos de la entidad Clientes

    //Metodo que guarda un Cliente en la BD
    Cliente guardar(Cliente cliente);
    //Parámetros - Recibe un objeto Cliente con los datos a guardar

    //Optional - Contenedor que puede o no tener un valor
    //evita el error de NullPointerException
    Optional<Cliente> buscarPorDPI(String dpi);

    //Metodo que actualiza un Cliente
    Cliente actualizar(String dpi, Cliente cliente);
    //Parámetros - dpi: DPI del Cliente a actualizar
    //Cliente cliente: Objeto con los datos nuevos
    //Retona un objeto de tipo Cliente ya actualizado

    //Metodo de tipo void para eliminar a un Cliente
    //void: no retorna ningún dato
    //Elimina un Cliente por su DPI
    void eliminar(String dpi);

    //boolean -  Retorna true si existe, false si no existe
    boolean existePorDPI(String dpi);

    //Metodo listar por estado
    List<Cliente> listPorEstado(int estado);

}
