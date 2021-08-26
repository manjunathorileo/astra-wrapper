package com.orileo.attendance.wrapper.repository.mysqlrepository.astra;


import com.orileo.attendance.wrapper.entity.mysql.astra.VisitorPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.transaction.Transactional;

@EnableJpaRepositories
@Transactional
public interface VisitorPassRepository extends JpaRepository<VisitorPass,Long> {

    VisitorPass findByMobileNumber(String mobileNumber);
}
