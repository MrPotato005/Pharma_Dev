package com.pharmadevs.inventario_spring.dao;

import com.pharmadevs.inventario_spring.model.ProduccionInsumos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduccionInsumosRepositorio extends JpaRepository<ProduccionInsumos, Integer> {
    List<ProduccionInsumos> findByProduccion_IdProduccion(Integer idProduccion);
    List<ProduccionInsumos> findByInsumoProductoIdProducto(Integer idInsumoProducto);
}
