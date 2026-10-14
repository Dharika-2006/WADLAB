package com.example.crud.controller;

import com.example.crud.service.CartService;
import com.example.crud.model.Cart;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor

public class CartController {
    private final CartService service;

    @PostMapping
    public Cart add(@RequestBody Cart c){
        return service.add(c);
    }

    @GetMapping
    public List<Cart> getAll(){
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable String id){
        service.remove(id);
    }

    @DeleteMapping
    public void clear(){
        service.clear();
    }
}
