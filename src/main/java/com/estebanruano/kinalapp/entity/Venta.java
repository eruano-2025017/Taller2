package com.estebanruano.kinalapp.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
//.
@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_venta")
    private Long CODIGOVenta;
    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal total;
    @Column(nullable = false)
    private int estado;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Clientes_dpi_cliente", referencedColumnName = "dpi_cliente", nullable = false)
    private Cliente cliente;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Usuarios_codigo_usuario", referencedColumnName = "codigo_usuario", nullable = false)
    private Usuario usuario;

    public Venta() {

    }

    public Venta(Long CODIGOVenta, LocalDate fechaVenta, BigDecimal total, int estado, Cliente cliente, Usuario usuario) {
        this.CODIGOVenta = CODIGOVenta;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.estado = estado;
        this.cliente = cliente;
        this.usuario = usuario;
    }

    public Long getCODIGOVenta() {
        return CODIGOVenta;
    }

    public void setCODIGOVenta(Long CODIGOVenta) {
        this.CODIGOVenta = CODIGOVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
