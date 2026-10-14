package com.example.crud.controller;

import com.example.crud.service.UserService;
import com.example.crud.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
@CrossOrigin(origins = "http://localhost:5173")

public class UserController {
    private final UserService service;

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return service.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        if(service.login(user)){
            return "Login Successful";
        }
        else{
            return "Invalid Login";
        }
    }
}
