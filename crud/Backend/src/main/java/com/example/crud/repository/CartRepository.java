package com.example.crud.repository;

import com.example.crud.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart,String> {

}
