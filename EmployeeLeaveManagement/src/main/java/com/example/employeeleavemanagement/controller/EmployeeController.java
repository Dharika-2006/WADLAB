package com.example.employeeleavemanagement.controller;

import com.example.employeeleavemanagement.model.Employee;
import com.example.employeeleavemanagement.model.Leave;
import com.example.employeeleavemanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeController {
    private final EmployeeService serv;
    @PostMapping("/employees")
    public Employee add(@RequestBody Employee e){
        return serv.add(e);
    }
    @GetMapping("/employees")
    public List<Employee> getEmployees(){
        return serv.getEmployees();
    }
    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable String id){
        return serv.getEmployee(id);
    }
    @PutMapping("/employees/{id}")
    public Employee update(@RequestBody Employee e,@PathVariable String id){
        return serv.update(id,e);
    }
    @DeleteMapping("/employees/{id}")
    public void delete(@PathVariable String id){
        serv.delete(id);
    }

    //Leave
    @PostMapping("/leave")
    public Leave applyLeave(@RequestBody Leave l){
        return serv.applyLeave(l);
    }

    @GetMapping("/leave")
    public List<Leave> getLeaves(){
        return serv.getAllLeaves();
    }

    @GetMapping("/leave/{id}")
    public Leave getleave(@PathVariable String id){
        return serv.getLeave(id);
    }

    @GetMapping("/leave/employee/{empid}")
    public List<Leave> getLeavesByEmployee(@PathVariable String empid){
        return serv.getLeaveByEmployee(empid);
    }

    @PutMapping("/leave/{id}")
    public Leave updateLeave(@RequestBody Leave l,@PathVariable String id){
        return serv.updateLeave(id,l);
    }
    @DeleteMapping("/leaves/{id}")
    public void Delete(@PathVariable String id){
        serv.deleteLeave(id);
    }

    //Queries
    @GetMapping("/employees/byleave/{type}")
    public List<Employee> byLeaveType(@PathVariable String type){
        return serv.getEmployeeByLeaveType(type);
    }

    @GetMapping("/employees/pending")
    public List<Employee> bypendingLeaves(){
        return serv.getEmployeesWithPendingLeaves();
    }

    @GetMapping("/employees/bydept/{dept}")
    public List<Employee> bydept(@PathVariable String dept){
        return serv.getEmployeeByDepartment(dept);
    }

    //Approval
    @PutMapping("/leaves/{id}/approve")
    public Leave approve(@PathVariable String id){
        return serv.approveLeave(id);
    }

    @PutMapping("/leaves/{id}/reject")
    public Leave reject(@PathVariable String id){
        return serv.rejectLeave(id);
    }

}
