package com.estebanruano.kinalapp.service;

import com.estebanruano.kinalapp.entity.Cliente;
import com.estebanruano.kinalapp.entity.Usuario;
import com.estebanruano.kinalapp.entity.Venta;
import com.estebanruano.kinalapp.repository.ClienteRepository;
import com.estebanruano.kinalapp.repository.UsuarioRepository;
import com.estebanruano.kinalapp.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//.
//este es el service
@Service
@Transactional
public class VentaService implements IVentaService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private final VentaRepository ventaRepository;

    /*
     * Constructor: Este se ejecuta al crear el objeto
     * Parámetros: Spring pasa el repositorio automáticamente y a esto se le conoce
     * como Inyección de Dependencias
     * Asignamos el repositorio a nuestra variable de clase
     * */

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    /*
     * @Override: Indicar que estamos implementando un metodo de la interfaz
     * */
    @Override

    @Transactional(readOnly = true)
    public List<Venta> ListarTodos() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta guardar(Venta venta) {
        Cliente cliente = clienteRepository.findById(venta.getCliente().getDpiCliente()).orElse(null);
        Usuario usuario = usuarioRepository.findById(venta.getUsuario().getCODIGOUsuario()).orElse(null);

        venta.setCliente(cliente);
        venta.setUsuario(usuario);

        if (venta.getEstado() == 0)
            venta.setEstado(1);
        return ventaRepository.save(venta);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarPorCODIGO(Long codigoVen) {
        return ventaRepository.findById(codigoVen);
        //Optional nos evitaa el NullPointerException
    }


    @Override
    public Venta actualizar(Long codigoVen, Venta venta) {
        //Actualiza un cliente existente
        if(!ventaRepository.existsById(codigoVen)){
            throw new RuntimeException("El producto no se encontró con CODIGO " + codigoVen);
            //Si no existe, se lanza una excepción (error controlado)
        }

        venta.setCODIGOVenta(codigoVen);
        validarVenta(venta);

        return ventaRepository.save(venta);
    }

    @Override
    public void eliminar(Long codigoVen) {
        //Eliminar una Venta
        if(!ventaRepository.existsById(codigoVen)){
            throw new RuntimeException("La venta no se encontró con el CODIGO " +codigoVen);
        }
        ventaRepository.deleteById(codigoVen);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCODIGO(Long codigoVen) {
        return ventaRepository.existsById(codigoVen);
        //retorna true o false
    }

    //Metodo privado(solo puede utilizarse dentro de la clase)
    private void validarVenta(Venta venta){
        /*
         * Validaciones del negocio: Este metodo se hará privado porque
         * es algo interno del servicio
         * */


        if (venta.getFechaVenta()== null){
            throw new IllegalArgumentException("La fecha es un dato obligarotio");
        }

        if (venta.getTotal() == null){
            throw new IllegalArgumentException("El Total no puede ser nulo es un dato obligatorio");
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listPorEstado(int estado){
        return ventaRepository.findByEstado(estado);
    }
}
