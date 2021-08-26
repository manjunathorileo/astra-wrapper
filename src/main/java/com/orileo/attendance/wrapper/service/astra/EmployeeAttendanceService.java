package com.orileo.attendance.wrapper.service.astra;


import com.orileo.attendance.wrapper.entity.mysql.astra.Employee;
import com.orileo.attendance.wrapper.entity.mysql.astra.EmployeeAttendance;

import java.util.Date;
import java.util.List;

public interface EmployeeAttendanceService {

    EmployeeAttendance createEmployeeAttendance(EmployeeAttendance employeeAttendance);

    EmployeeAttendance getTodayMarkedEmployeeAttendance(Date todayDate, Employee employee/*, AttendanceStatus attendanceStatus*/);

    List<EmployeeAttendance> getAllEmployeeAttendance();
}
