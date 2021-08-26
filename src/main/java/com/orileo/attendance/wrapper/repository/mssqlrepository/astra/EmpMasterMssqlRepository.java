package com.orileo.attendance.wrapper.repository.mssqlrepository.astra;

import com.orileo.attendance.wrapper.entity.mssql.astra.EmpMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.transaction.Transactional;

@EnableJpaRepositories
@Transactional
public interface EmpMasterMssqlRepository extends JpaRepository<EmpMaster, Long> {

    EmpMaster findByEmpId(String EmpId);
}
