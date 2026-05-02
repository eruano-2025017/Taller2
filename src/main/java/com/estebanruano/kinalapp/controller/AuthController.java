package com.estebanruano.kinalapp.controller;

import com.estebanruano.kinalapp.entity.Usuario;
import com.estebanruano.kinalapp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String root() {
        return "redirect:/vista/";
    }

    @GetMapping("/vista/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/vista/register")
    public String mostrarRegister() {
        return "register";
    }

    @PostMapping("/vista/register")
    public String procesarRegister(@RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam String email,
                                   @RequestParam(defaultValue = "USER") String rol,
                                   RedirectAttributes redirectAttributes) {
        try {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername(username);
            nuevoUsuario.setPassword(password);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setRol(rol);
            nuevoUsuario.setEstado(1);

            usuarioService.guardar(nuevoUsuario);
            redirectAttributes.addFlashAttribute("mensaje", "¡Usuario registrado exitosamente! Ahora puedes iniciar sesión.");
            return "redirect:/vista/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
            return "redirect:/vista/register";
        }
    }
}