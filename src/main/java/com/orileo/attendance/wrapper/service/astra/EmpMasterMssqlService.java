package com.orileo.attendance.wrapper.service.astra;

import com.orileo.attendance.wrapper.entity.mssql.astra.EmpMaster;

import java.util.List;

public interface EmpMasterMssqlService {

    void save(EmpMaster empMaster);

    List<EmpMaster> getEmpEntries();

    EmpMaster getEmp(String empId);

}
