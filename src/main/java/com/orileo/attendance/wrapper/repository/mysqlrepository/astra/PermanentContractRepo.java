package com.orileo.attendance.wrapper.repository.mysqlrepository.astra;

import com.orileo.attendance.wrapper.entity.mysql.astra.EmpPermanentContract;
import com.orileo.attendance.wrapper.entity.mysql.astra.EmployeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.transaction.Transactional;
import java.util.List;

@EnableJpaRepositories
@Transactional
public interface PermanentContractRepo extends JpaRepository<EmpPermanentContract, Long> {

    EmpPermanentContract findByEmployeeCode(String employeeCode);

    List<EmpPermanentContract> findByStatus(boolean status);

    List<EmpPermanentContract> findByEmployeeType(EmployeeType employeeType);

    List<EmpPermanentContract> findByContractCompany(String companyName);

    EmpPermanentContract findByCardId(String cardId);
}
