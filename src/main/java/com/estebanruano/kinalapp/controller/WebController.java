package com.estebanruano.kinalapp.controller;

import com.estebanruano.kinalapp.entity.Cliente;
import com.estebanruano.kinalapp.entity.DetalleVenta;
import com.estebanruano.kinalapp.entity.Producto;
import com.estebanruano.kinalapp.entity.Usuario;
import com.estebanruano.kinalapp.entity.Venta;
import com.estebanruano.kinalapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vista")
public class WebController {

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private IVentaService ventaService;

    @Autowired
    private IDetalleVentaService detalleVentaService;

    private void verificarPermisoActualizacion(boolean esActualizacion) {
        if (esActualizacion) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (!isAdmin) {
                throw new AccessDeniedException("No tiene permisos para actualizar registros.");
            }
        }
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalClientes", clienteService.listarTodos().size());
        model.addAttribute("totalProductos", productoService.listarTodos().size());
        model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
        model.addAttribute("totalVentas", ventaService.ListarTodos().size());
        return "index";
    }

    // ==================== CLIENTES ====================
    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("titulo", "Lista de Clientes");
        return "clientes/lista";
    }

    @GetMapping("/clientes/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("accion", "Crear Cliente");
        return "clientes/formulario";
    }

    @GetMapping("/clientes/ver/{dpi}")
    public String verCliente(@PathVariable String dpi, Model model) {
        Cliente cliente = clienteService.buscarPorDPI(dpi)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);
        return "clientes/ver";
    }

    @GetMapping("/clientes/editar/{dpi}")
    public String editarCliente(@PathVariable String dpi, Model model) {
        Cliente cliente = clienteService.buscarPorDPI(dpi)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);
        model.addAttribute("accion", "Editar Cliente");
        return "clientes/formulario";
    }

    @PostMapping("/clientes/guardar")
    public String guardarCliente(@RequestParam(required = false) String dpiCliente,
                                 @RequestParam String nombreCliente,
                                 @RequestParam String apellidoCliente,
                                 @RequestParam String direccion,
                                 @RequestParam(defaultValue = "1") int estado,
                                 RedirectAttributes redirectAttributes) {
        try {
            boolean existe = dpiCliente != null && clienteService.existePorDPI(dpiCliente);
            verificarPermisoActualizacion(existe);

            Cliente cliente = new Cliente();
            cliente.setDpiCliente(dpiCliente);
            cliente.setNombreCliente(nombreCliente);
            cliente.setApellidoCliente(apellidoCliente);
            cliente.setDireccion(direccion);
            cliente.setEstado(estado);

            if (existe) {
                clienteService.actualizar(dpiCliente, cliente);
                redirectAttributes.addFlashAttribute("mensaje", "Cliente actualizado exitosamente");
            } else {
                clienteService.guardar(cliente);
                redirectAttributes.addFlashAttribute("mensaje", "Cliente guardado exitosamente");
            }
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/vista/clientes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/vista/clientes";
    }

    @GetMapping("/clientes/eliminar/{dpi}")
    public String eliminarCliente(@PathVariable String dpi, RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminar(dpi);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Cliente desactivado correctamente. No se elimina físicamente porque puede estar asociado a ventas existentes.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/vista/clientes";
    }

    // ==================== PRODUCTOS ====================
    @GetMapping("/productos")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("titulo", "Lista de Productos");
        return "productos/lista";
    }

    @GetMapping("/productos/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("accion", "Crear Producto");
        return "productos/formulario";
    }

    @GetMapping("/productos/ver/{codigo}")
    public String verProducto(@PathVariable Long codigo, Model model) {
        Producto producto = productoService.buscarPorCODIGO(codigo)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto", producto);
        return "productos/ver";
    }

    @GetMapping("/productos/editar/{codigo}")
    public String editarProducto(@PathVariable Long codigo, Model model) {
        Producto producto = productoService.buscarPorCODIGO(codigo)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto", producto);
        model.addAttribute("accion", "Editar Producto");
        return "productos/formulario";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(@RequestParam(required = false) Long codigoProducto,
                                  @RequestParam String nombreProducto,
                                  @RequestParam BigDecimal precio,
                                  @RequestParam Integer stock,
                                  @RequestParam(defaultValue = "1") Integer estado,
                                  RedirectAttributes redirectAttributes) {
        try {
            boolean existe = codigoProducto != null && productoService.existePorCODIGO(codigoProducto);
            verificarPermisoActualizacion(existe);

            Producto producto = new Producto();
            producto.setCODIGOProducto(codigoProducto);
            producto.setNombreProducto(nombreProducto);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setEstado(estado);

            if (existe) {
                productoService.actualizar(codigoProducto, producto);
                redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
            } else {
                productoService.guardar(producto);
                redirectAttributes.addFlashAttribute("mensaje", "Producto guardado exitosamente");
            }
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/vista/productos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/vista/productos";
    }

    @GetMapping("/productos/eliminar/{codigo}")
    public String eliminarProducto(@PathVariable Long codigo, RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminar(codigo);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Producto desactivado correctamente. No se elimina físicamente porque puede estar asociado a ventas existentes.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/vista/productos";
    }

    // ==================== USUARIOS ====================
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("titulo", "Lista de Usuarios");
        return "usuarios/lista";
    }

    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("accion", "Crear Usuario");
        return "usuarios/formulario";
    }

    @GetMapping("/usuarios/ver/{codigo}")
    public String verUsuario(@PathVariable Long codigo, Model model) {
        Usuario usuario = usuarioService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "usuarios/ver";
    }

    @GetMapping("/usuarios/editar/{codigo}")
    public String editarUsuario(@PathVariable Long codigo, Model model) {
        Usuario usuario = usuarioService.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        model.addAttribute("accion", "Editar Usuario");
        return "usuarios/formulario";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@RequestParam(required = false) Long codigoUsuario,
                                 @RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String email,
                                 @RequestParam String rol,
                                 @RequestParam(defaultValue = "1") int estado,
                                 RedirectAttributes redirectAttributes) {
        try {
            boolean existe = codigoUsuario != null && codigoUsuario > 0 && usuarioService.existePorCodigo(codigoUsuario);
            verificarPermisoActualizacion(existe);

            Usuario usuario = new Usuario();
            usuario.setCODIGOUsuario(codigoUsuario);
            usuario.setUsername(username);
            usuario.setPassword(password);
            usuario.setEmail(email);
            usuario.setRol(rol);
            usuario.setEstado(estado);

            if (existe) {
                usuarioService.actualizar(codigoUsuario, usuario);
                redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
            } else {
                usuarioService.guardar(usuario);
                redirectAttributes.addFlashAttribute("mensaje", "Usuario guardado exitosamente");
            }
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/vista/usuarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/vista/usuarios";
    }

    @GetMapping("/usuarios/eliminar/{codigo}")
    public String eliminarUsuario(@PathVariable Long codigo, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminar(codigo);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/vista/usuarios";
    }

    // ==================== VENTAS ====================
    @GetMapping("/ventas")
    public String listarVentas(Model model) {
        List<Venta> ventas = ventaService.ListarTodos();
        // Limpiar relaciones rotas para evitar errores en la vista
        for (Venta v : ventas) {
            try { if (v.getCliente() != null) v.getCliente().getNombreCliente(); }
            catch (Exception e) { v.setCliente(null); }
            try { if (v.getUsuario() != null) v.getUsuario().getUsername(); }
            catch (Exception e) { v.setUsuario(null); }
        }
        model.addAttribute("ventas", ventas);
        model.addAttribute("titulo", "Lista de Ventas");
        return "ventas/lista";
    }

    @GetMapping("/ventas/nuevo")
    public String nuevaVenta(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("accion", "Crear Venta");
        return "ventas/formulario";
    }

    @GetMapping("/ventas/ver/{codigo}")
    public String verVenta(@PathVariable Long codigo, Model model) {
        Venta venta = ventaService.buscarPorCODIGO(codigo)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        List<DetalleVenta> detalles = detalleVentaService.ListarTodos().stream()
                .filter(d -> d.getVenta() != null && d.getVenta().getCODIGOVenta().equals(codigo))
                .collect(Collectors.toList());

        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);
        return "ventas/ver";
    }

    @GetMapping("/ventas/editar/{codigo}")
    public String editarVenta(@PathVariable Long codigo, Model model) {
        Venta venta = ventaService.buscarPorCODIGO(codigo)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("accion", "Editar Venta");
        return "ventas/formulario";
    }

    @PostMapping("/ventas/guardar")
    public String guardarVenta(@RequestParam(required = false) Long codigoVenta,
                               @RequestParam String fechaVenta,
                               @RequestParam BigDecimal total,
                               @RequestParam String clienteDpiCliente,
                               @RequestParam Long usuarioCodigoUsuario,
                               @RequestParam(defaultValue = "1") int estado,
                               RedirectAttributes redirectAttributes) {
        try {
            boolean existe = codigoVenta != null && codigoVenta > 0 && ventaService.existePorCODIGO(codigoVenta);
            verificarPermisoActualizacion(existe);

            Cliente cliente = clienteService.buscarPorDPI(clienteDpiCliente)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            Usuario usuario = usuarioService.buscarPorCodigo(usuarioCodigoUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Venta venta = new Venta();
            venta.setCODIGOVenta(codigoVenta);
            venta.setFechaVenta(LocalDate.parse(fechaVenta));
            venta.setTotal(total);
            venta.setCliente(cliente);
            venta.setUsuario(usuario);
            venta.setEstado(estado);

            if (existe) {
                ventaService.actualizar(codigoVenta, venta);
                redirectAttributes.addFlashAttribute("mensaje", "Venta actualizada exitosamente");
            } else {
                ventaService.guardar(venta);
                redirectAttributes.addFlashAttribute("mensaje", "Venta guardada exitosamente");
            }
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/vista/ventas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/vista/ventas";
    }

    @GetMapping("/ventas/eliminar/{codigo}")
    public String eliminarVenta(@PathVariable Long codigo, RedirectAttributes redirectAttributes) {
        try {
            ventaService.eliminar(codigo);
            redirectAttributes.addFlashAttribute("mensaje", "Venta eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/vista/ventas";
    }

    // ==================== DETALLE VENTA ====================
    @GetMapping("/detalleVentas")
    public String listarDetalleVentas(Model model) {
        model.addAttribute("detalles", detalleVentaService.ListarTodos());
        model.addAttribute("titulo", "Lista de Detalle Ventas");
        return "detalleVenta/lista";
    }

    @GetMapping("/detalleVentas/nuevo")
    public String nuevoDetalleVenta(Model model) {
        model.addAttribute("detalleVenta", new DetalleVenta());
        model.addAttribute("ventas", ventaService.ListarTodos());
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("accion", "Agregar Producto a Venta");
        return "detalleVenta/formulario";
    }

    @GetMapping("/detalleVentas/ver/{codigo}")
    public String verDetalleVenta(@PathVariable Long codigo, Model model) {
        DetalleVenta detalle = detalleVentaService.buscarPorCODIGO(codigo)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
        model.addAttribute("detalle", detalle);
        return "detalleVenta/ver";
    }

    @GetMapping("/detalleVentas/editar/{codigo}")
    public String editarDetalleVenta(@PathVariable Long codigo, Model model) {
        DetalleVenta detalle = detalleVentaService.buscarPorCODIGO(codigo)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
        model.addAttribute("detalleVenta", detalle);
        model.addAttribute("ventas", ventaService.ListarTodos());
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("accion", "Editar Detalle Venta");
        return "detalleVenta/formulario";
    }

    @PostMapping("/detalleVentas/guardar")
    public String guardarDetalleVenta(@RequestParam(required = false) Long codigoDetalleVenta,
                                      @RequestParam Long ventaCodigoVenta,
                                      @RequestParam Long productoCodigoProducto,
                                      @RequestParam int cantidad,
                                      @RequestParam BigDecimal precioUnitario,
                                      @RequestParam(required = false) BigDecimal subTotal,
                                      RedirectAttributes redirectAttributes) {
        try {
            boolean existe = codigoDetalleVenta != null && codigoDetalleVenta > 0 && detalleVentaService.existePorCODIGO(codigoDetalleVenta);
            verificarPermisoActualizacion(existe);

            Venta venta = ventaService.buscarPorCODIGO(ventaCodigoVenta)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
            Producto producto = productoService.buscarPorCODIGO(productoCodigoProducto)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            BigDecimal subtotal = (subTotal != null) ? subTotal : precioUnitario.multiply(BigDecimal.valueOf(cantidad));

            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setCodigoDetalleVenta(codigoDetalleVenta);
            detalleVenta.setVenta(venta);
            detalleVenta.setProducto(producto);
            detalleVenta.setCantidad(cantidad);
            detalleVenta.setPrecioUnitario(precioUnitario);
            detalleVenta.setSubTotal(subtotal);

            if (existe) {
                detalleVentaService.actualizar(codigoDetalleVenta, detalleVenta);
                redirectAttributes.addFlashAttribute("mensaje", "Detalle actualizado exitosamente");
            } else {
                detalleVentaService.guardar(detalleVenta);
                redirectAttributes.addFlashAttribute("mensaje", "Producto agregado a la venta exitosamente");
            }
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/vista/detalleVentas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/vista/detalleVentas";
    }

    @GetMapping("/detalleVentas/eliminar/{codigo}")
    public String eliminarDetalleVenta(@PathVariable Long codigo, RedirectAttributes redirectAttributes) {
        try {
            detalleVentaService.eliminar(codigo);
            redirectAttributes.addFlashAttribute("mensaje", "Detalle de venta eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/vista/detalleVentas";
    }
}