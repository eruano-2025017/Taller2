package com.estebanruano.kinalapp.controller;

import com.estebanruano.kinalapp.entity.Usuario;
import com.estebanruano.kinalapp.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    // ESTE ES EL ÚNICO MÉTODO PARA LA RAÍZ PRINCIPAL
    @GetMapping("/")
    public String raiz() {
        return "redirect:/vista/login";
    }

    @GetMapping("/vista/login")
    public String mostrarLogin(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/vista/";
        }
        return "login";
    }

    @PostMapping("/vista/login")
    public String procesarLogin(@RequestParam String username,
                                @RequestParam String password,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        Usuario usuario = usuarioService.autenticar(username, password);

        if (usuario != null) {
            session.setAttribute("usuarioLogueado", usuario);
            session.setAttribute("rol", usuario.getRol());
            return "redirect:/vista/";
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario o contraseña incorrectos");
            return "redirect:/vista/login";
        }
    }

    @GetMapping("/vista/register")
    public String mostrarRegister(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("/vista/register")
    public String procesarRegister(@RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam String email,
                                   @RequestParam(required = false, defaultValue = "USER") String rol,
                                   RedirectAttributes redirectAttributes) {

        try {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername(username);
            nuevoUsuario.setPassword(password);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setRol(rol);
            nuevoUsuario.setEstado(1);

            usuarioService.guardar(nuevoUsuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado exitosamente. Ahora puedes iniciar sesión.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/vista/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
            return "redirect:/vista/register";
        }
    }

    @GetMapping("/vista/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/vista/login";
    }
}