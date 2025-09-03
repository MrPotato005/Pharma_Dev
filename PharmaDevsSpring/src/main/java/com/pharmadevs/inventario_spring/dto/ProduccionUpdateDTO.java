package com.pharmadevs.inventario_spring.dto;

public class ProduccionUpdateDTO {

    private Integer cantidadProducida;
    private String lote;

    public Integer getCantidadProducida() {
        return cantidadProducida;
    }
    public void setCantidadProducida(Integer cantidadProducida) {
        this.cantidadProducida = cantidadProducida;
    }
    public String getLote() {
        return lote;
    }
    public void setLote(String lote) {
        this.lote = lote;
    }
}
