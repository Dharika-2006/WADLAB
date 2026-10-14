package com.example.crud.service;

import com.example.crud.model.Order;
import com.example.crud.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repo;

    public Order place(Order o){
        o.setStatus("Placed");
        return repo.save(o);
    }
    public List<Order> getAll(){
        return repo.findAll();
    }
    public void cancel(String id){
        Order o = repo.findById(id).orElseThrow();
        repo.deleteById(id);
        o.setStatus("Cancelled");
        repo.save(o);
    }
}
