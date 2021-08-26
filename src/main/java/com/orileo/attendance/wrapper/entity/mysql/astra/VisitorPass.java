package com.orileo.attendance.wrapper.entity.mysql.astra;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "visitor_pass")
public class VisitorPass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "visit_type")
    private String visitType;

    @Column(name = "visitor_type")
    private String visitorType;

    @Column(name = "visitor_organization")
    private String visitorOrganization;

    @Column(name = "visitor_location")
    private String visitorLocation;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "item_carried")
    private String itemCarried;

    @CreationTimestamp
    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "purpose")
    private String purpose;

    //-----------new Req-----------
    @Column(name = "allowed_or_denied")
    private boolean allowedOrDenied;

    @Column(name = "rfid")
    private String rfid;


}

