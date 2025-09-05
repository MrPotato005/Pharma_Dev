package com.pharmadevs.inventario_spring.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(
        name = "carrito_item",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_carrito", "id_producto"})
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito_item")
    private Long idCarritoItem;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_carrito")
    private Carrito carrito;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario")
    private Float precioUnitario;

    @Column(name = "total")
    private Float total;



    @PrePersist
    @PreUpdate
    private void calcularTotal() {
        float pu = (precioUnitario != null) ? precioUnitario : 0f;
        int cant = (cantidad != null) ? cantidad : 0;
        total = pu * cant;
    }

    public Long getIdCarritoItem() {
        return idCarritoItem;
    }

    public void setIdCarritoItem(Long idCarritoItem) {
        this.idCarritoItem = idCarritoItem;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }
}


















