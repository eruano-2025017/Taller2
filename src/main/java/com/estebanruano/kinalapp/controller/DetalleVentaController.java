package com.estebanruano.kinalapp.controller;

import com.estebanruano.kinalapp.entity.DetalleVenta;
import com.estebanruano.kinalapp.service.IDetalleVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//.
@RestController
@RequestMapping("/detallesVentas")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;

    public DetalleVentaController(IDetalleVentaService detalleVentaService){
        this.detalleVentaService = detalleVentaService;
    }


    @GetMapping
    public ResponseEntity<List<DetalleVenta>> listar(){
        List<DetalleVenta> detallesventas = detalleVentaService.ListarTodos();
        return ResponseEntity.ok(detallesventas);
    }

    @GetMapping("/{codigoDeVe}")
    public ResponseEntity<DetalleVenta> buscarPorCODIGO(@PathVariable Long codigoDeVe){
        return detalleVentaService.buscarPorCODIGO(codigoDeVe)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //POST crear un
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody DetalleVenta detalleVenta){
        try {
            DetalleVenta nuevoDetalleVenta = detalleVentaService.guardar(detalleVenta);

            return new ResponseEntity<>(nuevoDetalleVenta, HttpStatus.CREATED);
            //201 CREATED(mucho más específico que el 2200
        }catch(IllegalArgumentException e){
            //Si hay error de validacion
            return ResponseEntity.badRequest().body(e.getMessage());
            //400 BAD REQUEST con el mensaje de error
        }
    }

    @DeleteMapping("/{codigoDeVe}")
    public ResponseEntity<Void> eliminar(@PathVariable Long codigoDeVe){
        //ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        try {
            if (!detalleVentaService.existePorCODIGO(codigoDeVe)){
                return ResponseEntity.notFound().build();
                //404 si no existe
            }
            detalleVentaService.eliminar(codigoDeVe);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }

    }

    @PutMapping("/{codigoDeVe}")
    public ResponseEntity<?> actualizar(@PathVariable Long codigoDeVe, @RequestBody DetalleVenta detalleVenta){
        try{
            if(!detalleVentaService.existePorCODIGO(codigoDeVe)){

                return  ResponseEntity.notFound().build();
            }

            DetalleVenta detalleVentaActualizada = detalleVentaService.actualizar(codigoDeVe, detalleVenta);
            return ResponseEntity.ok(detalleVentaActualizada);
        }catch(IllegalArgumentException e){

            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){

            return ResponseEntity.notFound().build();
        }

    }
}
