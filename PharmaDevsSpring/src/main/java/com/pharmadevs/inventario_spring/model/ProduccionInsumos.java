package com.pharmadevs.inventario_spring.model;

import jakarta.persistence.*;

@Entity
@Table(name = "produccion_insumos")
public class ProduccionInsumos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produccion_insumo")
    private Integer idProduccionInsumo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produccion")
    private Produccion produccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto insumoProducto;

    @Column(name = "cantidad_usada")
    private Integer cantidadUsada;

    // === Getters y Setters ===

    public Integer getIdProduccionInsumo() {
        return idProduccionInsumo;
    }

    public void setIdProduccionInsumo(Integer idProduccionInsumo) {
        this.idProduccionInsumo = idProduccionInsumo;
    }

    public Produccion getProduccion() {
        return produccion;
    }

    public void setProduccion(Produccion produccion) {
        this.produccion = produccion;
    }

    public Producto getInsumoProducto() {
        return insumoProducto;
    }

    public void setInsumoProducto(Producto insumoProducto) {
        this.insumoProducto = insumoProducto;
    }

    public Integer getCantidadUsada() {
        return cantidadUsada;
    }

    public void setCantidadUsada(Integer cantidadUsada) {
        this.cantidadUsada = cantidadUsada;
    }
}