package com.orileo.attendance.wrapper.entity.mysql.astra;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "in_csv_raw")
public class InCsvRaw {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "sso_id")
    private String ssoId;
    @Column(name = "type")
    private String type;
    @Column(name = "access_date")
    private Date accessDate;
    @Column(name = "b_id")
    private String bId;
    @UpdateTimestamp
    @Column(name = "recorded_on")
    private Date recordedOn;
    @Column(name = "c_id")
    private String cId;
    @Column(name = "log_id")
    private long logId;
    @Column(name = "processed")
    private boolean processed;


}
