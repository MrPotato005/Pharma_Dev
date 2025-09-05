package com.pharmadevs.inventario_spring.dao;

import com.pharmadevs.inventario_spring.model.Carrito;
import com.pharmadevs.inventario_spring.model.CarritoItem;
import com.pharmadevs.inventario_spring.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoItemRepositorio extends JpaRepository<CarritoItem, Long> {



    Optional<CarritoItem>findByCarritoAndProducto(Carrito carrito, Producto producto);
    List<CarritoItem> findByCarrito(Carrito carrito);

}
