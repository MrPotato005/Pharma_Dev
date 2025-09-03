package com.pharmadevs.inventario_spring.dao;

import com.pharmadevs.inventario_spring.model.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlmacenRepositorio extends JpaRepository<Almacen, Integer> {
    Optional<Almacen> findByProductoIdProducto(Integer idProductoFinal);
}
