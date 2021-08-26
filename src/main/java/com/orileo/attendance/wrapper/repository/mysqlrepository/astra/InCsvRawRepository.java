package com.orileo.attendance.wrapper.repository.mysqlrepository.astra;

import com.orileo.attendance.wrapper.entity.mysql.astra.InCsvRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@EnableJpaRepositories
@Transactional
public interface InCsvRawRepository extends JpaRepository<InCsvRaw,Long> {

    InCsvRaw findByLogId(long logId);

}
