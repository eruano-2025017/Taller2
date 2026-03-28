package com.estebanruano.kinalapp.controller;

import com.estebanruano.kinalapp.entity.Cliente;
import com.estebanruano.kinalapp.entity.Usuario;
import com.estebanruano.kinalapp.service.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//.
//este es controller
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    //Reponde a penticiones GET
    @GetMapping
    //ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Usuario>> listar(){
        List<Usuario> usuarios = usuarioService.ListarTodos();
        //delegamos al servicio
        return ResponseEntity.ok(usuarios);
        //200 OK con la lista de clientes
    }

    //{dpi} es una variable de ruta(valor a buscar)
    @GetMapping("/{codigoUsu}")
    public ResponseEntity<Usuario> buscarPorCODIGO(@PathVariable Long codigoUsu){
        //@PathVariable Toma el valor de la URL y lo asigna al dpi
        return usuarioService.buscarPorCODIGO(codigoUsu)
                //Si Optional tiene valor, devuelve 200 ok con el cliente
                .map(ResponseEntity::ok)
                //Si Optinal está vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    //POST crear un nuevo cliente
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario){
        //@RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo Cliente
        //<?> significa "tipo genérico" puede ser un CLiente o un String
        try {
            Usuario nuevoUsuario = usuarioService.guardar(usuario);
            //Intentamos guardar el cliente pero puede lanzar una excepcion
            // de IllegalArgumentException
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
            //201 CREATED(mucho más específico que el 2200 para la creación de un cliente)
        }catch(IllegalArgumentException e){
            //Si hay error de validacion
            return ResponseEntity.badRequest().body(e.getMessage());
            //400 BAD REQUEST con el mensaje de error
        }
    }

    //DELETE elimina un cliente
    @DeleteMapping("/{codigoUsu}")
    public ResponseEntity<Void> eliminar(@PathVariable Long codigoUsu){
        //ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        try {
            if (!usuarioService.existePorCODIGO(codigoUsu)){
                return ResponseEntity.notFound().build();
                //404 si no existe
            }
            usuarioService.eliminar(codigoUsu);
            return ResponseEntity.noContent().build();
            //204 NO CONTENT (se ejecutó correctamente y no devuelve cuerpo)
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }

    }

    //Actualizar cliente a través de codigo
    @PutMapping("/{codigoUsu}")
    public ResponseEntity<?> actualizar(@PathVariable Long codigoUsu, @RequestBody Usuario usuario){
        try{
            if(!usuarioService.existePorCODIGO(codigoUsu)){
                //Verificar si existe antes de poder actualizar
                //404 NOT FOUND
                return  ResponseEntity.notFound().build();
            }
            //Actualizar el cliente pero esto puede lanzar una excepcion
            Usuario usuarioActualizado = usuarioService.actualizar(codigoUsu, usuario);
            return ResponseEntity.ok(usuarioActualizado);
            //200 ok con el cliente ya actualizado
        }catch(IllegalArgumentException e){
            //Error cuando los dados son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            //Posiblemente cualquier otro error como: cliente no encontrado, etc.
            //404 NOT FOUND
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/estado/{estado}")
    //ResposeEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Usuario>> listPorEstado(@PathVariable int estado){
        List<Usuario> usuarios = usuarioService.listPorEstado(estado);
        return ResponseEntity.ok(usuarioService.listPorEstado(estado));
    }

}
