package com.example.crud.service;

import lombok.RequiredArgsConstructor;
import com.example.crud.repository.CartRepository;
import com.example.crud.model.Cart;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository repo;

    public Cart add(Cart c){
        return repo.save(c);
    }
    public List<Cart> getAll(){
        return repo.findAll();
    }
    public void remove(String id){
        repo.deleteById(id);
    }
    public void clear(){
        repo.deleteAll();
    }
}
