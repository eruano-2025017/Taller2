package com.estebanruano.kinalapp.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
//.s
@Entity
@Table(name = "detalleVenta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_detalle_venta")
    private Long codigoDetalleVenta;
    @Column(nullable = false)
    private int cantidad;
    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;
    @Column(name = "sub_total",precision = 10, scale = 2, nullable = false)
    private BigDecimal subTotal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Productos_codigo_producto", nullable = false)
    private Producto producto;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Ventas_codigo_venta", nullable = false)
    private Venta venta;

    public DetalleVenta() {

    }

    public DetalleVenta(Long codigoDetalleVenta, int cantidad, BigDecimal precioUnitario, BigDecimal subTotal, Producto producto, Venta venta) {
        this.codigoDetalleVenta = codigoDetalleVenta;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subTotal = subTotal;
        this.producto = producto;
        this.venta = venta;
    }

    public Long getCodigoDetalleVenta() {
        return codigoDetalleVenta;
    }

    public void setCodigoDetalleVenta(Long codigoDetalleVenta) {
        this.codigoDetalleVenta = codigoDetalleVenta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubTotal() {return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }
}
