package com.example.crud.service;

import com.example.crud.model.User;
import com.example.crud.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;

    public User register(User user){
        return repo.save(user);
    }

    public boolean login(User user){
        User found = repo.findByUsername(user.getUsername());

        return found!=null && found.getPassword().equals(user.getPassword());
    }
}
