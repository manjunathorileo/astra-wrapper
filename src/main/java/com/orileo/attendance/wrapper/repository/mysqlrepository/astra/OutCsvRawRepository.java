package com.orileo.attendance.wrapper.repository.mysqlrepository.astra;

import com.orileo.attendance.wrapper.entity.mysql.astra.OutCsvRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@EnableJpaRepositories
@Transactional
public interface OutCsvRawRepository extends JpaRepository<OutCsvRaw,Long> {

    OutCsvRaw findByLogId(long logId);

}
