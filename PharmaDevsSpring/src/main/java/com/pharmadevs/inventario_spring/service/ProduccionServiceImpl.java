package com.pharmadevs.inventario_spring.service;


import com.pharmadevs.inventario_spring.dao.*;
import com.pharmadevs.inventario_spring.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProduccionServiceImpl implements ProduccionService {
    @Autowired
    ProduccionRepositorio produccionRepositorio;


    @Autowired
    private ProduccionInsumosRepositorio produccionInsumosRepositorio;

    @Autowired
    private BodegaRepositorio bodegaRepositorio;

    @Autowired
    private AlmacenRepositorio almacenRepositorio;

@Autowired
private ProductoRepositorio productoRepositorio;





    @Override
    public List<Produccion> findAll(){
        return produccionRepositorio.findAll();
    }

    @Override
    public Produccion findById(int id){
        return produccionRepositorio.findById(id).orElse(null);
    }


    @Override
    public Produccion findOne(int id){
        Optional<Produccion> produccion = produccionRepositorio.findById(id);
        return produccion.orElse(null);
    }

    @Override
    public Produccion save(Produccion produccion){
        return produccionRepositorio.save(produccion);
    }

    @Override
    public Produccion update(int id, Produccion produccionNuevo){
        Produccion produccionExistente = findOne(id);

        if (produccionExistente == null){
            return null;
        }


        produccionExistente.setFechaproduccion(produccionNuevo.getfechaproduccion());
        produccionExistente.setFechavencimiento(produccionNuevo.getFechavencimiento());
        produccionExistente.setLote(produccionNuevo.getLote());
        produccionExistente.setCantidadproducida(produccionNuevo.getCantidadproducida());
        produccionExistente.setImagen(produccionNuevo.getImagen());


        return produccionRepositorio.save(produccionExistente);
    }

    @Override
    public void delete(int id){
        if (produccionRepositorio.existsById(id)){
            produccionRepositorio.deleteById(id);
        }
    }


    @Transactional
    public void procesarProduccion(Integer idProductoFinal, Integer cantidadProducir) throws Exception {

        // 1. Obtener la "receta" (insumos necesarios) para el producto final
        List<
                ProduccionInsumos> insumosNecesarios = produccionInsumosRepositorio.findByInsumoProductoId(idProductoFinal);

        if (insumosNecesarios.isEmpty()) {
            throw new Exception("No se encontró la receta para el producto con ID: " + idProductoFinal);
        }

        // 2. Verificar el stock de cada insumo en la bodega
        for (ProduccionInsumos insumo : insumosNecesarios) {
            Integer cantidadNecesaria = insumo.getCantidadUsada() * cantidadProducir;
            Optional<Bodega> bodegaItem = bodegaRepositorio.findByProducto(insumo.getInsumoProducto());

            if (bodegaItem.isEmpty() || bodegaItem.get().getCantidad() < cantidadNecesaria) {
                String nombreInsumo = insumo.getInsumoProducto() != null ? insumo.getInsumoProducto().getNombre() : "Insumo desconocido";
                throw new Exception("Stock insuficiente en bodega para el insumo: " + nombreInsumo);
            }
        }

        // 3. Descontar el stock de la bodega
        for (ProduccionInsumos insumo : insumosNecesarios) {
            Integer cantidadADescontar = insumo.getCantidadUsada() * cantidadProducir;
            Bodega bodega = bodegaRepositorio.findByProducto(insumo.getInsumoProducto()).get();
            bodega.setCantidad(bodega.getCantidad() - cantidadADescontar);
            bodegaRepositorio.save(bodega);
        }

        // 4. Registrar el producto terminado en el almacén
        Optional<Almacen> almacenProductoFinal = almacenRepositorio.findByProductoId(idProductoFinal);

        if (almacenProductoFinal.isPresent()) {
            Almacen almacen = almacenProductoFinal.get();
            almacen.setStock(almacen.getStock() + cantidadProducir);
            almacenRepositorio.save(almacen);
        } else {
            Almacen nuevoAlmacen = new Almacen();
            Producto productoFinal = productoRepositorio.findById(idProductoFinal).orElseThrow(() -> new Exception("Producto final no encontrado."));
            nuevoAlmacen.setProducto(productoFinal);
            nuevoAlmacen.setStock(cantidadProducir);
            almacenRepositorio.save(nuevoAlmacen);
        }

        Produccion produccion = new Produccion();
        Producto productoFinal = productoRepositorio.findById(idProductoFinal).orElseThrow(() -> new Exception("Producto final no encontrado."));
        produccion.setProducto(productoFinal);
        produccion.setCantidadproducida(cantidadProducir);
        produccion.setFechaproduccion(LocalDateTime.now());

        produccionRepositorio.save(produccion);
    }

}