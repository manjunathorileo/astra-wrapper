package com.orileo.attendance.wrapper.repository.mssqlrepository.astra;

import com.orileo.attendance.wrapper.entity.mssql.astra.Trans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@EnableJpaRepositories
@Transactional
public interface TransRepository extends JpaRepository<Trans, Long> {

    @Query("select t from Trans t where CONVERT(date, t.entryTime) = :l ORDER BY t.loggedOn DESC")
    List<Trans> findByLoggedOn(@Param("l") Date loggedOn);


    @Query("select t from Trans t where CONVERT(date, t.entryTime) = :l and t.employeeCode = :employeeCode")
    List<Trans> findByMarkedOnAndEmployeeCode(@Param("l") Date l, @Param("employeeCode") String employeeCode);
}
