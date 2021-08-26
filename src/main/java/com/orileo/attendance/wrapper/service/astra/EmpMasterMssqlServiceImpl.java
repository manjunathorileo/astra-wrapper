package com.orileo.attendance.wrapper.service.astra;

import com.orileo.attendance.wrapper.entity.mssql.astra.EmpMaster;
import com.orileo.attendance.wrapper.repository.mssqlrepository.astra.EmpMasterMssqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpMasterMssqlServiceImpl implements EmpMasterMssqlService {
    @Autowired
    EmpMasterMssqlRepository empMasterMssqlRepository;

    @Override
    public void save(EmpMaster empMaster) {
        empMasterMssqlRepository.save(empMaster);
    }

    @Override
    public List<EmpMaster> getEmpEntries() {
        return empMasterMssqlRepository.findAll();
    }

    @Override
    public EmpMaster getEmp(String EmpId) {
        return empMasterMssqlRepository.findByEmpId(EmpId);
    }
}
