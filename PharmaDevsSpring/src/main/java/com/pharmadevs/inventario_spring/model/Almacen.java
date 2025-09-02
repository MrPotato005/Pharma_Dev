package com.pharmadevs.inventario_spring.model;

import jakarta.persistence.*;

@Entity
@Table(name = "almacen")

public class Almacen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Almacen")
    private int idAlmacen;

    @Column(name = "Stock")
    private Integer stock;

    @Column(name = "Ubicacion")
    private String ubicacion;

    @Column(name = "imagen")
    private String imagen;

    @ManyToOne
    @JoinColumn(name = "ID_Producto")
    private Producto producto;

    public Almacen() {
    }

    public Almacen(int idAlmacen, Integer stock, String ubicacion, String imagen, Producto producto) {
        this.idAlmacen = idAlmacen;
        this.stock = stock;
        this.ubicacion = ubicacion;
        this.imagen = imagen;
        this.producto = producto;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String toString() {
        return "Almacen{" +
                "idAlmacen=" + idAlmacen +
                ", stock='" + stock + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", imagen='" + imagen + '\'' +
                ", producto=" + producto +
                '}';
    }
}