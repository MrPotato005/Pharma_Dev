package com.pharmadevs.inventario_spring.dto;

public class ProduccionRequestDTO {

    private Integer productoId;
    private Integer cantidadProducir;
    private String lote;

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidadProducir() {
        return cantidadProducir;
    }

    public void setCantidadProducir(Integer cantidadProducir) {
        this.cantidadProducir = cantidadProducir;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }
}
