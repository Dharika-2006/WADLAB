package com.example.employeeleavemanagement.service;

import com.example.employeeleavemanagement.model.Employee;
import com.example.employeeleavemanagement.model.Leave;
import com.example.employeeleavemanagement.repository.EmployeeRepository;
import com.example.employeeleavemanagement.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository emprepo;
    private final LeaveRepository leaverepo;

    public Employee add(Employee e){
        return emprepo.save(e);
    }
    public List<Employee> getEmployees(){
        return emprepo.findAll();
    }
    public Employee getEmployee(String id){
        return emprepo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee not found"));
    }
    public Employee update(String id,Employee e){
        getEmployee(id);
        e.setId(id);
        return emprepo.save(e);
    }
    public void delete(String id){
        getEmployee(id);
        emprepo.deleteById(id);
    }

    public Leave applyLeave(Leave leave){
        emprepo.findByEmpid(leave.getEmpid()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee not found"));
        if(leave.getRequestedDays()<=0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Leave must be greater than 0");
        }
        leave.setStatus("Pending");
        return leaverepo.save(leave);
    }
    public List<Leave> getAllLeaves(){
        return leaverepo.findAll();
    }
    public Leave getLeave(String id){
        return leaverepo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Leave not found"));

    }
    public List<Leave> getLeaveByEmployee(String empid){
        return leaverepo.findByEmpid(empid);
    }
    public Leave updateLeave(String id,Leave updated){
        Leave old = getLeave(id);
        if(!"Pending".equalsIgnoreCase(old.getStatus())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only pending leaves can be edited.");
        }
        if (updated.getRequestedDays() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Requested days must be greater than zero");
        }
        emprepo.findByEmpid(updated.getEmpid()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee not found."));
        updated.setId(id);
        updated.setStatus("Pending");

        return leaverepo.save(updated);
    }
    public void deleteLeave(String id){
        Leave leave = getLeave(id);

        if(!"Pending".equalsIgnoreCase(leave.getStatus())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only pending leaves can be deleted");
        }
        leaverepo.deleteById(id);
    }
    public List<Employee> getEmployeeByLeaveType(String type){
        List<Leave> leaves = leaverepo.findByLeaveTypeIgnoreCaseAndStatusIgnoreCase(type,"Approved");
        List<String> empIds = leaves.stream().map(Leave::getEmpid).distinct().toList();
        return emprepo.findByEmpidIn(empIds);

    }
    public List<Employee> getEmployeesWithPendingLeaves(){
        List<Leave> leaves = leaverepo.findByStatusIgnoreCase("Pending");
        List<String> empids = leaves.stream().map(Leave::getEmpid).distinct().toList();
        return emprepo.findByEmpidIn(empids);
    }
    public List<Employee> getEmployeeByDepartment(String Department){
        return emprepo.findByDeptIgnoreCase(Department);
    }
    public Leave approveLeave(String leaveId){
        Leave leave = getLeave(leaveId);
        if (!"Pending".equalsIgnoreCase(leave.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Leave is not pending");
        }
        Employee emp=emprepo.findByEmpid(leave.getEmpid()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee not found"));

        int bal = emp.getLeavebal();
        int days=leave.getRequestedDays();

        if(bal<=0 || bal<days){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Insufficient Balance");

        }
        emp.setLeavebal(bal-days);
        emprepo.save(emp);

        leave.setStatus("Approved");
        return leaverepo.save(leave);
    }
    public Leave rejectLeave(String leaveId) {

        Leave leave = getLeave(leaveId);

        if (!"PENDING".equalsIgnoreCase(leave.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Leave is not pending");
        }

        leave.setStatus("REJECTED");

        // No balance deduction for rejected leave
        return leaverepo.save(leave);
    }
}
