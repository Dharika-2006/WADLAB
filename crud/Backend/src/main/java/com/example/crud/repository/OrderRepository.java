package com.example.crud.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.crud.model.Order;

public interface OrderRepository extends MongoRepository<Order,String> {
}
