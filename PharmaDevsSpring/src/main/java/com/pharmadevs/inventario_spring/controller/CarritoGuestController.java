package com.pharmadevs.inventario_spring.controller;


import com.pharmadevs.inventario_spring.model.Carrito;
import com.pharmadevs.inventario_spring.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/guest/cart")
public class CarritoGuestController  {
 private final CarritoService carritoService;

@Autowired
    public CarritoGuestController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }


    @PostMapping
    public ResponseEntity<Carrito> createOrGet(@RequestParam(required = false)String token){

        if(token == null || token.isEmpty()){
            token= UUID.randomUUID().toString();

        }
        return ResponseEntity.ok(carritoService.getOrCreateByToken(token));
    }

    @PostMapping("/items")
    public ResponseEntity<Carrito> addItem(@RequestParam String token,
                                           @RequestBody Map<String, Integer> body) {
        int productoId = body.getOrDefault("productoId", 0);
        int cantidad = body.getOrDefault("cantidad", 1); // por defecto 1
        return ResponseEntity.ok(carritoService.addItem(token, productoId, cantidad));
    }


    @PutMapping("/items/{carritoItemId}")
                public ResponseEntity<Carrito> update(@RequestParam String token,
                                                      @PathVariable long carritoItemId,
                                                        @RequestBody Map<String, Integer> body
        ){
            int cantidad = body.getOrDefault("cantidad", 0);
            return ResponseEntity.ok(carritoService.updateItemCantidad(token,carritoItemId,cantidad));

        }

        @DeleteMapping("/items/{carritoItemId}")
                public ResponseEntity<Void> remove(@RequestParam String token,@PathVariable long carritoItemId){
            carritoService.removeItem(token, carritoItemId);
            return ResponseEntity.noContent().build();

        }


        @DeleteMapping("/clear")
        public ResponseEntity<Void> clear (@RequestParam String token){

            carritoService.clearByToken(token);
            return  ResponseEntity.noContent().build();

        }
















}
