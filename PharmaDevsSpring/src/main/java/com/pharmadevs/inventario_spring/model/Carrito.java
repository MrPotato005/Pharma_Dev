package com.pharmadevs.inventario_spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long idCarrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @Column(name = "token", unique = true)
    private String token;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CarritoItem> items = new ArrayList<>();

    @Column(name = "subtotal")
    private Float subtotal = 0f;

    @Column(name = "descuento")
    private Float descuento = 0f;

    @Column(name = "impuestos")
    private Float impuestos = 0f;

    @Column(name = "total")
    private Float total = 0f;

    @Column(name = "actualizando_en")
    private LocalDateTime actualizandoEn;

    // === Lógica de cálculo ===

    public void recomputarTotales(Float tasaIva) {
        subtotal = 0f;

        for (CarritoItem item : items) {
            Float pu = item.getPrecioUnitario() != null ? item.getPrecioUnitario() : 0f;
            int cant = item.getCantidad() != null ? item.getCantidad() : 0;
            Float itemTotal = item.getTotal() != null ? item.getTotal() : pu * cant;
            subtotal += itemTotal;
        }

        if (descuento == null) descuento = 0f;
        Float base = subtotal - descuento;
        if (base < 0f) base = 0f;

        impuestos = (tasaIva != null ? base * tasaIva : 0f);
        total = base + impuestos;

        actualizandoEn = LocalDateTime.now();
    }

    public void recomprobacionTotalesCompat() {
        if (subtotal == null) subtotal = 0f;
        if (descuento == null) descuento = 0f;
        if (total == null) total = 0f;
    }

    // === Getters y Setters ===

    public Long getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(Long idCarrito) {
        this.idCarrito = idCarrito;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<CarritoItem> getItems() {
        return items;
    }

    public void setItems(List<CarritoItem> items) {
        this.items = items;
    }

    public Float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }

    public Float getDescuento() {
        return descuento;
    }

    public void setDescuento(Float descuento) {
        this.descuento = descuento;
    }

    public Float getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(Float impuestos) {
        this.impuestos = impuestos;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public LocalDateTime getActualizandoEn() {
        return actualizandoEn;
    }

    public void setActualizandoEn(LocalDateTime actualizandoEn) {
        this.actualizandoEn = actualizandoEn;
    }
}
