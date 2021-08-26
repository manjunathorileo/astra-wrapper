package com.orileo.attendance.wrapper.repository.mysqlrepository.astra;

import com.orileo.attendance.wrapper.entity.mysql.astra.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByReferenceId(String referenceId);

    Employee findByEmployeeCode(String employeeCode);
}