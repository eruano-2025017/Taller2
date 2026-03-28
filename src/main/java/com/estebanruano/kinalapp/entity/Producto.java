package com.estebanruano.kinalapp.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
//.
//Esta es la entidad producto
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_producto")
    private Long CODIGOProducto;
    @Column(nullable = false)
    private String nombreProducto;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;
    @Column(nullable = false)
    private int stock;
    @Column(nullable = false)
    private int estado;

    public Producto() {

    }

    public Producto(Long CODIGOProducto, String nombreProducto, BigDecimal precio, Integer stock, Integer estado) {
        this.CODIGOProducto = CODIGOProducto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
    }

    public Long getCODIGOProducto() {
        return CODIGOProducto;
    }

    public void setCODIGOProducto(Long CODIGOProducto) {
        this.CODIGOProducto = CODIGOProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
