package com.example.employeeleavemanagement.repository;

import com.example.employeeleavemanagement.model.Leave;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LeaveRepository extends MongoRepository<Leave,String> {
    List<Leave> findByLeaveTypeIgnoreCaseAndStatusIgnoreCase(String leaveType,String status);
    List<Leave> findByStatusIgnoreCase(String status);
    List<Leave> findByEmpid(String empid);
}
