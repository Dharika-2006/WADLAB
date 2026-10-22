package com.example.employeeleavemanagement.repository;

import com.example.employeeleavemanagement.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Optional<Employee> findByEmpid(String empid);
    List<Employee> findByEmpidIn(List<String> empids);
    List<Employee> findByDeptIgnoreCase(String dept);
}
