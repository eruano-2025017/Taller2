package com.estebanruano.kinalapp.service;

import com.estebanruano.kinalapp.entity.Cliente;
import com.estebanruano.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        validarCliente(cliente);
        if (cliente.getEstado() == 0)
            cliente.setEstado(1);
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(String dpi) {
        return clienteRepository.findById(dpi);
    }

    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {
        if (!clienteRepository.existsById(dpi)) {
            throw new RuntimeException("Cliente no se encontró con DPI " + dpi);
        }
        cliente.setDpiCliente(dpi);
        validarCliente(cliente);
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(String dpi) {
        Cliente cliente = clienteRepository.findById(dpi)
                .orElseThrow(() -> new RuntimeException("El Cliente no se encontró con el DPI " + dpi));

        cliente.setEstado(0);
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDPI(String dpi) {
        return clienteRepository.existsById(dpi);
    }

    private void validarCliente(Cliente cliente) {
        if (cliente.getDpiCliente() == null || cliente.getDpiCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El DPI es un dato obligatorio");
        }
        if (cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es un dato obligatorio");
        }
        if (cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es un dato obligatorio");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listPorEstado(int estado) {
        return clienteRepository.findByEstado(estado);
    }
}