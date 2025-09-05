package com.pharmadevs.inventario_spring.service;


import com.pharmadevs.inventario_spring.dao.CarritoItemRepositorio;
import com.pharmadevs.inventario_spring.dao.CarritoRepositorio;
import com.pharmadevs.inventario_spring.dao.ClienteRepositorio;
import com.pharmadevs.inventario_spring.dao.ProductoRepositorio;
import com.pharmadevs.inventario_spring.model.Carrito;
import com.pharmadevs.inventario_spring.model.CarritoItem;
import com.pharmadevs.inventario_spring.model.Cliente;
import com.pharmadevs.inventario_spring.model.Producto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarritoServiceImpl implements CarritoService {
    private final CarritoRepositorio carritoRepositorio;
    private final CarritoItemRepositorio carritoItemRepositorio;
    private final ClienteRepositorio clienteRepositorio;
    private final ProductoRepositorio productoRepositorio;
    private static final BigDecimal IVA = BigDecimal.valueOf(0.15);
    public CarritoServiceImpl(CarritoRepositorio carritoRepositorio,
                              CarritoItemRepositorio carritoItemRepositorio,
                              ClienteRepositorio clienteRepositorio,
                              ProductoRepositorio productoRepositorio)
                               {
        this.carritoRepositorio = carritoRepositorio;
        this.carritoItemRepositorio = carritoItemRepositorio;
        this.clienteRepositorio = clienteRepositorio;
        this.productoRepositorio = productoRepositorio;

    }


    @Override
    @Transactional
    public Carrito getOrCreateByClienteId(int clienteId, String token) {
        Cliente cliente = clienteRepositorio.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clienteId));

        Optional<Carrito> carritoOpt = carritoRepositorio.findByCliente(cliente);
        if (carritoOpt.isPresent()) return carritoOpt.get();

        Carrito carrito = new Carrito();
        carrito.setCliente(cliente);
        carrito.setToken(token != null ? token : UUID.randomUUID().toString());
        carrito.recomputarTotales(IVA.floatValue());
        return carritoRepositorio.save(carrito);
    }

    @Override
    @Transactional
    public Carrito addItem(int clienteId, int productoId, int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad debe ser mayor a 0");

        Carrito carrito = getOrCreateByClienteId(clienteId, null);
        Producto producto = productoRepositorio.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + productoId));

        Optional<CarritoItem> itemOpt = carritoItemRepositorio.findByCarritoAndProducto(carrito, producto);
        if (itemOpt.isPresent()) {
            CarritoItem item = itemOpt.get();
            item.setCantidad(item.getCantidad() + cantidad);
            carritoItemRepositorio.save(item);
        } else {
            CarritoItem item = new CarritoItem();
            item.setCarrito(carrito);
            item.setProducto(producto);
            item.setCantidad(cantidad);
            carrito.getItems().add(item);
        }

        carrito.recomputarTotales(IVA.floatValue());
        return carritoRepositorio.save(carrito);
    }


    @Override
    @Transactional
    public Carrito updateItemCantidad(int clienteId, long carritoItemId, int nuevaCantidad) {
        Carrito carrito = getOrCreateByClienteId(clienteId, null);
        CarritoItem item = carritoItemRepositorio.findById(carritoItemId)
                .orElseThrow(() -> new IllegalArgumentException("Item no encontrado: " + carritoItemId));

        if (nuevaCantidad <= 0) {
            carrito.getItems().remove(item);
            carritoItemRepositorio.delete(item);
        } else {
            item.setCantidad(nuevaCantidad);
            carritoItemRepositorio.save(item);
        }

        carrito.recomputarTotales(IVA.floatValue());
        return carritoRepositorio.save(carrito);
    }

    @Override
    @Transactional
    public void removeItem(int clienteId, long carritoItemId) {
        updateItemCantidad(clienteId, carritoItemId, 0);
    }


    @Override
    @Transactional
    public void clear(int clienteId) {
        Carrito carrito = getOrCreateByClienteId(clienteId, null);
        carrito.getItems().clear();
        carrito.setSubtotal(0f);
        carrito.setDescuento(0f);
        carrito.setImpuestos(0f);
        carrito.setTotal(0f);
        carritoRepositorio.save(carrito);
    }

    @Override
    @Transactional
    public Carrito getByClienteId(int clienteId) {
        Cliente cliente = clienteRepositorio.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clienteId));
        return carritoRepositorio.findByCliente(cliente)
                .orElseGet(() -> {
                    Carrito c = new Carrito();
                    c.setCliente(cliente);
                    return c;
                });
    }


    @Override
    @Transactional
    public Carrito getOrCreateByToken(String token) {
    try {
        if (token == null || token.isEmpty()) token = UUID.randomUUID().toString();

        String finalToken = token;
        return carritoRepositorio.findByToken(token)
                .orElseGet(() -> {
                    Carrito c = new Carrito();
                    c.setToken(finalToken);
                    c.setItems(new ArrayList<>());
                    c.setSubtotal(0f);
                    c.setDescuento(0f);
                    c.setImpuestos(0f);
                    c.setTotal(0f);
                    return carritoRepositorio.save(c);
                });
    } catch (Exception e) {
        e.printStackTrace();
        throw e;
    }
}

    @Override
    @Transactional
    public Carrito addItem(String token, int productoId, int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad debe ser mayor a 0");

        Carrito carrito = getOrCreateByToken(token);
        Producto producto = productoRepositorio.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + productoId));

        Optional<CarritoItem> itemOpt = carritoItemRepositorio.findByCarritoAndProducto(carrito, producto);
        if (itemOpt.isPresent()) {
            CarritoItem item = itemOpt.get();
            item.setCantidad(item.getCantidad() + cantidad);
            carritoItemRepositorio.save(item);
        } else {
            CarritoItem item = new CarritoItem();
            item.setCarrito(carrito);
            item.setProducto(producto);
            item.setCantidad(cantidad);
            carrito.getItems().add(item);
        }

        carrito.recomputarTotales(IVA.floatValue());
        return carritoRepositorio.save(carrito);
    }
    @Override
    @Transactional
    public Carrito updateItemCantidad(String token, long carritoItemId, int nuevaCantidad) {
        Carrito carrito = getOrCreateByToken(token);
        CarritoItem item = carritoItemRepositorio.findById(carritoItemId)
                .orElseThrow(() -> new IllegalArgumentException("Item no encontrado: " + carritoItemId));

        if (nuevaCantidad <= 0) {
            carrito.getItems().remove(item);
            carritoItemRepositorio.delete(item);
        } else {
            item.setCantidad(nuevaCantidad);
            carritoItemRepositorio.save(item);
        }

        carrito.recomputarTotales(IVA.floatValue());
        return carritoRepositorio.save(carrito);
    }


    @Override
    @Transactional
    public void removeItem(String token, long carritoItemId) {
        updateItemCantidad(token, carritoItemId, 0);
    }

    @Override
    @Transactional
    public void clearByToken(String token) {
        Carrito carrito = getOrCreateByToken(token);
        carrito.getItems().clear();
        carrito.setSubtotal(0f);
        carrito.setDescuento(0f);
        carrito.setImpuestos(0f);
        carrito.setTotal(0f);
        carritoRepositorio.save(carrito);
    }
    @Override
    @Transactional
    public Carrito getByToken(String token) {
        return carritoRepositorio.findByToken(token)
                .orElseGet(() -> {
                    Carrito c = new Carrito();
                    c.setToken(token);
                    c.setItems(new ArrayList<>());
                    c.setSubtotal(0f);
                    c.setDescuento(0f);
                    c.setImpuestos(0f);
                    c.setTotal(0f);
                    return c;
                });
    }
}


