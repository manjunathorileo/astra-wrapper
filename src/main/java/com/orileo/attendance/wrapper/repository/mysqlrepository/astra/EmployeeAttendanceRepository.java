package com.orileo.attendance.wrapper.repository.mysqlrepository.astra;

import com.orileo.attendance.wrapper.entity.mysql.astra.Employee;
import com.orileo.attendance.wrapper.entity.mysql.astra.EmployeeAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EmployeeAttendanceRepository extends JpaRepository<EmployeeAttendance,Long> {

    List<EmployeeAttendance> findByMarkedOnAndEmployee(Date todayDate, Employee employee/*, AttendanceStatus attendanceStatus*/);

   }
