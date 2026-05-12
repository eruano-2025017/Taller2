package com.estebanruano.kinalapp.controller;

import com.estebanruano.kinalapp.entity.Usuario;
import com.estebanruano.kinalapp.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{codigoUsu}")
    public ResponseEntity<Usuario> buscarPorCODIGO(@PathVariable Long codigoUsu) {
        return usuarioService.buscarPorCodigo(codigoUsu)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario) {
        try {
            return new ResponseEntity<>(usuarioService.guardar(usuario), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigoUsu}")
    public ResponseEntity<Void> eliminar(@PathVariable Long codigoUsu) {
        if (!usuarioService.existePorCodigo(codigoUsu)) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminar(codigoUsu);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{codigoUsu}")
    public ResponseEntity<?> actualizar(@PathVariable Long codigoUsu, @RequestBody Usuario usuario) {
        if (!usuarioService.existePorCodigo(codigoUsu)) {
            return ResponseEntity.notFound().build();
        }
        try {
            return ResponseEntity.ok(usuarioService.actualizar(codigoUsu, usuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Usuario>> listPorEstado(@PathVariable int estado) {
        return ResponseEntity.ok(usuarioService.listPorEstado(estado));
    }
}