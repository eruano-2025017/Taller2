package com.estebanruano.kinalapp.service;

import com.estebanruano.kinalapp.entity.Usuario;
import com.estebanruano.kinalapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario guardar(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        validarUsuario(usuario);
        if (usuario.getEstado() == 0) {
            usuario.setEstado(1);
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCodigo(Long codigoUsu) {
        return usuarioRepository.findById(codigoUsu);
    }

    public Usuario actualizar(Long codigoUsu, Usuario usuario) {
        if (!usuarioRepository.existsById(codigoUsu)) {
            throw new RuntimeException("Usuario no se encontró con CODIGO " + codigoUsu);
        }

        Usuario existente = usuarioRepository.findById(codigoUsu).get();
        existente.setUsername(usuario.getUsername());
        existente.setEmail(usuario.getEmail());
        existente.setRol(usuario.getRol());
        existente.setEstado(usuario.getEstado());

        if (usuario.getPassword() != null && !usuario.getPassword().trim().isEmpty()) {
            existente.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        validarUsuario(existente);
        return usuarioRepository.save(existente);
    }

    public void eliminar(Long codigoUsu) {
        if (!usuarioRepository.existsById(codigoUsu)) {
            throw new RuntimeException("El Usuario no se encontró con el codigo " + codigoUsu);
        }
        usuarioRepository.deleteById(codigoUsu);
    }

    @Transactional(readOnly = true)
    public boolean existePorCodigo(Long codigoUsu) {
        return usuarioRepository.existsById(codigoUsu);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listPorEstado(int estado) {
        return usuarioRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El username es un dato obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("El password es un dato obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es un dato obligatorio");
        }
    }
}