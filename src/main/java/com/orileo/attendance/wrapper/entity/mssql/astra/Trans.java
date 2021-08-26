package com.orileo.attendance.wrapper.entity.mssql.astra;

import lombok.Getter;
import lombok.Setter;
import org.apache.el.stream.StreamELResolverImpl;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "TransactionData")
public class Trans {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "MACAddress")
    private String macAddress;

    @Column(name = "CardId")
    private String cardId;

    @Column(name = "EmployeeId")
    private String employeeCode;

    @Column(name = "entryDtTime")
    private Date loggedOn;

    @Column(name = "EmployeeName")
    private String employeeName;

    @Column(name = "AccessCode")
    private String accessCode;

    @Column(name = "transDtTime")
    private Date entryTime;

    @Column(name = "Inout")
    private String inOut;

    @Column(name = "GateNo")
    private String gateNo;

}
