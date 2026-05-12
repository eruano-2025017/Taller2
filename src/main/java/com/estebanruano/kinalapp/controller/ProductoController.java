package com.estebanruano.kinalapp.controller;


import com.estebanruano.kinalapp.entity.Producto;
import com.estebanruano.kinalapp.service.IProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//.
//Este es el controller
@RestController
@RequestMapping("/productos")

public class ProductoController {

    //Inyectamos el SERVICIO y NO el repositorio
    //El controlador solo debe de tene conexion con el Servicio
    private final IProductoService productoService;

    //Como buena práctica la Inyección de dependencias deber hacerse por el constructor
    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    //Reponde a penticiones GET
    @GetMapping
    //ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Producto>> listar(){
        List<Producto> productos = productoService.listarTodos();
        //delegamos al servicio
        return ResponseEntity.ok(productos);
        //200 OK
    }

    //{dpi} es una variable de ruta(valor a buscar)
    @GetMapping("/{codigoPro}")
    public ResponseEntity<Producto> buscarPorCODIGO(@PathVariable Long codigoPro){
        //@PathVariable Toma el valor de la URL
        return productoService.buscarPorCODIGO(codigoPro)
                //Si Optional tiene valor, devuelve 200 ok
                .map(ResponseEntity::ok)
                //Si Optinal está vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    //POST crear un nuevo Producto
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Producto producto){
        //@RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo Producto
        //<?> significa "tipo genérico" puede ser un Producto o un String
        try {
            Producto nuevoProducto = productoService.guardar(producto);
            //Intentamos guardar el cliente pero puede lanzar una excepcion
            // de IllegalArgumentException
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
            //201 CREATED(mucho más específico que el 2200 para la creación de un producto)
        }catch(IllegalArgumentException e){
            //Si hay error de validacion
            return ResponseEntity.badRequest().body(e.getMessage());
            //400 BAD REQUEST con el mensaje de error
        }
    }

    //DELETE elimina un cliente
    @DeleteMapping("/{codigoPro}")
    public ResponseEntity<Void> eliminar(@PathVariable Long codigoPro){
        //ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        try {
            if (!productoService.existePorCODIGO(codigoPro)){
                return ResponseEntity.notFound().build();
                //404 si no existe
            }
            productoService.eliminar(codigoPro);
            return ResponseEntity.noContent().build();
            //204 NO CONTENT (se ejecutó correctamente y no devuelve cuerpo)
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }

    }

    //Actualizar cliente a través de CODIGO
    @PutMapping("/{codigoPro}")
    public ResponseEntity<?> actualizar(@PathVariable Long codigoPro, @RequestBody Producto producto){
        try{
            if(!productoService.existePorCODIGO(codigoPro)){
                //Verificar si existe antes de poder actualizar
                //404 NOT FOUND
                return  ResponseEntity.notFound().build();
            }
            //Actualizar el cliente pero esto puede lanzar una excepcion
            Producto productoActualizado = productoService.actualizar(codigoPro, producto);
            return ResponseEntity.ok(productoActualizado);
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
    public ResponseEntity<List<Producto>> listPorEstado(@PathVariable int estado){
        List<Producto> productos = productoService.listPorEstado(estado);
        return ResponseEntity.ok(productoService.listPorEstado(estado));
    }
}
