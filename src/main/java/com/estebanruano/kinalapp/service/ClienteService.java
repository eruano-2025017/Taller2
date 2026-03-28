package com.estebanruano.kinalapp.service;

import com.estebanruano.kinalapp.entity.Cliente;
import com.estebanruano.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//Anotación que registra un Bean como un Bean de Spring
//Que la clase contiene la lógica del negocio
@Service
//Por defecto todos los métodos de esta clase serán
//transaccionales
//Una transacción es que puede o no ocurrir algo
@Transactional
public class ClienteService implements IClienteService {
    /* private: solo accesible dentro de la clase
       ClienteRepository: Es el repositorio para acceder a DB
       Inyección de Depencias Spring nos da el repositorio

    */
    private final ClienteRepository clienteRepository;

    /*
     * Constructor: Este se ejecuta al crear el objeto
     * Parámetros: Spring pasa el repositorio automáticamente y a esto se le conoce
     * como Inyección de Dependencias
     * Asignamos el repositorio a nuestra variable de clase
     * */

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /*
     * @Override: Indicar que estamos implementando un metodo de la interfaz
     * */
    @Override
    /*
     * readOnly = true: Lo que hace es optimizar la consulta, no bloquea la BD
     * */
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
        /*
         * Llama a l mértodo findAll() del repositorio de Spring Data JPA
         * este metodo hace exactamente el Select * from Clientes
         * */
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        /*
         * Metodo de guardar crea un Cliente
         * Acá es donde colocamos la l+ogica del negocio Antes de guardar
         * Primero validamos el dato
         * */
        validarCliente(cliente);
        if (cliente.getEstado() == 0)
            cliente.setEstado(1);
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(String dpi) {
        //Buscar un cliente por DPI
        return clienteRepository.findById(dpi);
        //Optional nos evitaa el NullPointerException
    }


    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {
        //Actualiza un cliente existente
        if(!clienteRepository.existsById(dpi)){
            throw new RuntimeException("Cliente no se encontró con DPI " + dpi);
            //Si no existe, se lanza una excepción (error controlado)
        }
        /*
         * 1. Asegurar que el DPI del objeto coincida con el de la URL
         * 2. por seguridad usamos el DPI de la URL y no el que viene en el JSON
         * */
        cliente.setDpiCliente(dpi);
        validarCliente(cliente);

        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(String dpi) {
        //Eliminar un cliente
        if(!clienteRepository.existsById(dpi)){
            throw new RuntimeException("El Cliente no se encontró con el DPI " +dpi);
        }
        clienteRepository.deleteById(dpi);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDPI(String dpi) {
        //Verificar si existe el cliente
        return clienteRepository.existsById(dpi);
        //retorna true o false
    }

    //Metodo privado(solo puede utilizarse dentro de la clase)
    private void validarCliente(Cliente cliente){
        /*
         * Validaciones del negocio: Este metodo se hará privado porque
         * es algo interno del servicio
         * */
        if (cliente.getDpiCliente() == null || cliente.getDpiCliente().trim().isEmpty()){
            //Si el DPI es null o esta vacio después de quitar espacios
            //Lanza una excepcion con un mensaje
            throw new IllegalArgumentException("El DPI es un dato obligatorio");
        }

        if (cliente.getNombreCliente()== null || cliente.getNombreCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre es un dato obligarotio");
        }

        if (cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El apellido es un dato obligatorio");
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listPorEstado(int estado){
        return clienteRepository.findByEstado(estado);
    }
}
