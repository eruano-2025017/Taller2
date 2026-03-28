package com.estebanruano.kinalapp.controller;

import com.estebanruano.kinalapp.entity.Venta;
import com.estebanruano.kinalapp.service.IVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//.
@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final IVentaService ventaService;

    public VentaController(IVentaService ventaService){
        this.ventaService = ventaService;
    }

    //Reponde a penticiones GET
    @GetMapping
    //ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Venta>> listar(){
        List<Venta> ventas = ventaService.ListarTodos();
        //delegamos al servicio
        return ResponseEntity.ok(ventas);
        //200 OK con la list
    }

    @GetMapping("/{codigoVen}")
    public ResponseEntity<Venta> buscarPorCODIGO(@PathVariable Long codigoVen){
        //@PathVariable Toma el valor de la URL
        return ventaService.buscarPorCODIGO(codigoVen)
                //Si Optional tiene valor, devuelve 200 ok
                .map(ResponseEntity::ok)
                //Si Optinal está vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    //POST crear un
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Venta venta){
        try {
            Venta nuevaVenta = ventaService.guardar(venta);
            //Intentamos guardar el cliente pero puede lanzar una excepcion
            // de IllegalArgumentException
            return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
            //201 CREATED(mucho más específico que el 2200
        }catch(IllegalArgumentException e){
            //Si hay error de validacion
            return ResponseEntity.badRequest().body(e.getMessage());
            //400 BAD REQUEST con el mensaje de error
        }
    }

    @DeleteMapping("/{codigoVen}")
    public ResponseEntity<Void> eliminar(@PathVariable Long codigoVen){
        //ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        try {
            if (!ventaService.existePorCODIGO(codigoVen)){
                return ResponseEntity.notFound().build();
                //404 si no existe
            }
            ventaService.eliminar(codigoVen);
            return ResponseEntity.noContent().build();
            //204 NO CONTENT (se ejecutó correctamente y no devuelve cuerpo)
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }

    }

    //Actualizar cliente a través de codigo
    @PutMapping("/{codigoVen}")
    public ResponseEntity<?> actualizar(@PathVariable Long codigoVen, @RequestBody Venta venta){
        try{
            if(!ventaService.existePorCODIGO(codigoVen)){
                //Verificar si existe antes de poder actualizar
                //404 NOT FOUND
                return  ResponseEntity.notFound().build();
            }
            //Actualizar el cliente pero esto puede lanzar una excepcion
            Venta ventaActualizada = ventaService.actualizar(codigoVen, venta);
            return ResponseEntity.ok(ventaActualizada);
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
    public ResponseEntity<List<Venta>> listPorEstado(@PathVariable int estado){
        List<Venta> ventas = ventaService.listPorEstado(estado);
        return ResponseEntity.ok(ventaService.listPorEstado(estado));
    }
}
