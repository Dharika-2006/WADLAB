package com.example.crud.service;

import com.example.crud.repository.ProductRepository;
import com.example.crud.model.Product;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repo;

    public Product add(Product p){
        return repo.save(p);
    }

    public List<Product> getAll(){
        return repo.findAll();
    }

    public Product getOne(String id){
        return repo.findById(id).orElse(null);
    }

    public Product update(String id,Product p){
        p.setId(id);
        return repo.save(p);
    }
    public void delete(String id){
        repo.deleteById(id);
    }
}
