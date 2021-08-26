package com.orileo.attendance.wrapper.entity.mssql.astra;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Table(name="EmpMaster")
public class EmpMaster {

    @Id
    @Column(name = "EmpId")
    private String empId;

    @Column(name = "CardId")
    private String cardId;

    @Column(name ="EmpName")
    private String empname;

    @Column(name = "DumpFingerNo")
    private String dumpFingerNo;
}
