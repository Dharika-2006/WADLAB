package com.example.crud.controller;

import com.example.crud.model.Order;
import com.example.crud.service.OrderService;
import com.example.crud.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")

public class OrderController {
    private final OrderService service;

    @PostMapping
    public Order place(@RequestBody Order o){
        return service.place(o);
    }

    @GetMapping
    public List<Order> getAll(){
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable String id){
        service.cancel(id);
    }


}
