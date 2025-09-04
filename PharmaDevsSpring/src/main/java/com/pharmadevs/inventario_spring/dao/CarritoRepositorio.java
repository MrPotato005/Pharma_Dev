package com.pharmadevs.inventario_spring.dao;

import com.pharmadevs.inventario_spring.model.Carrito;
import com.pharmadevs.inventario_spring.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepositorio extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByToken(String token);
    Optional<Carrito> findByCliente(Cliente cliente);
}
