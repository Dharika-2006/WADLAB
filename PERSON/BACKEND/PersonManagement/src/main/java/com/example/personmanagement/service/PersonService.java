package com.example.personmanagement.service;

import com.example.personmanagement.model.Person;
import com.example.personmanagement.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PersonService {
    private final PersonRepository repo;

    public Person add(Person p){
        return repo.save(p);
    }

    public Person update(String id,Person p){
        Person curr= repo.findById(id).orElse(null);
        curr.setName(p.getName());
        return repo.save(curr);
    }

    public List<Person> getAll(){
        return repo.findAll();
    }
    public Person getOne(String id){
        return repo.findById(id).orElse(null);
    }
    public void delete(String id){
        repo.deleteById(id);
    }

}
