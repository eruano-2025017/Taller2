package com.estebanruano.kinalapp.service;

import com.estebanruano.kinalapp.entity.Cliente;
import com.estebanruano.kinalapp.entity.Usuario;
import com.estebanruano.kinalapp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
//.
//Este es el servicio
//Anotacion que registra un modelo(un Bean) como un Bean en Spring
//La clase contiene la logica del negocio
@Service
//Por defecto los metodos de estas clase seran Transaccionales
@Transactional
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;


    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> ListarTodos(){
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario guardar(Usuario usuario){
        validarUsuario(usuario);
        if (usuario.getEstado()== 0)
            usuario.setEstado(1);
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCODIGO(Long codigoUsu) {
        return usuarioRepository.findById(codigoUsu);
    }


    @Override
    public Usuario actualizar(Long codigoUsu, Usuario usuario){
        if (!usuarioRepository.existsById(codigoUsu)){
            throw new RuntimeException("Cliente no se encontró con CODIGO " + codigoUsu);
        }

        usuario.setCODIGOUsuario(codigoUsu);
        validarUsuario(usuario);

        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long codigoUsu) {
        //Eliminar un cliente
        if(!usuarioRepository.existsById(codigoUsu)){
            throw new RuntimeException("El Usuario no se encontró con el codigo " +codigoUsu);
        }
        usuarioRepository.deleteById(codigoUsu);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCODIGO(Long codigoUsu) {
        //Verificar si existe el cliente
        return usuarioRepository.existsById(codigoUsu);
        //retorna true o false
    }

    //Metodo privado(solo puede utilizarse dentro de la clase)
    private void validarUsuario(Usuario usuario){
        /*
         * Validaciones del negocio: Este metodo se hará privado porque
         * es algo interno del servicio
         * */

        if (usuario.getUsername()== null || usuario.getUsername().trim().isEmpty()){
            throw new IllegalArgumentException("El username es un dato obligarotio");
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()){
            throw new IllegalArgumentException("El password es un dato obligatorio");
        }

        if (usuario.getEmail()== null || usuario.getEmail().trim().isEmpty()){
            throw new IllegalArgumentException("El email es un dato obligatorio");
        }

    }
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listPorEstado(int estado){
        return usuarioRepository.findByEstado(estado);
    }


}
