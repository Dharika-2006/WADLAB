package com.example.crud.repository;

import com.example.crud.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
            User findByUsername(String username);
}
