package com.example.personmanagement.controller;

import com.example.personmanagement.model.Person;
import com.example.personmanagement.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService service;

    @PostMapping
    public Person add(@RequestBody Person p){
        return service.add(p);
    }

    @GetMapping
    public List<Person> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Person getOne(@PathVariable String id){
        return service.getOne(id);
    }

    @PutMapping("/{id}")
    public Person update(@PathVariable String id,@RequestBody Person p){
        return service.update(id,p);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        service.delete(id);
    }

}
