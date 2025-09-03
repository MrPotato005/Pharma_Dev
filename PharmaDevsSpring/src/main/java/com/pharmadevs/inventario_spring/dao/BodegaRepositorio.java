package com.pharmadevs.inventario_spring.dao;

import com.pharmadevs.inventario_spring.model.Bodega;
import com.pharmadevs.inventario_spring.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BodegaRepositorio extends JpaRepository<Bodega, Integer> {
    Optional<Bodega> findByProducto(Producto insumoProducto);
}


