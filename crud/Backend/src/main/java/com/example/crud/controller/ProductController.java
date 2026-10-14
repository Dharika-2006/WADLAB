package com.example.crud.controller;

import com.example.crud.model.Product;
import com.example.crud.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins= "http://localhost:5173")

public class ProductController {
    private final ProductService service;

    @PostMapping
    public Product add(@RequestBody Product p){
        return service.add(p);
    }

    @GetMapping
    public List<Product> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Product getOne(@PathVariable String id){
        return service.getOne(id);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable String id,@RequestBody Product p){
        return service.update(id,p);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        service.delete(id);
    }


}
