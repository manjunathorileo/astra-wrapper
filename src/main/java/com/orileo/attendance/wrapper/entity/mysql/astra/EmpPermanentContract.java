package com.orileo.attendance.wrapper.entity.mysql.astra;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "emp_permanent_contract")
public class EmpPermanentContract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_code", length = 25)
    private String employeeCode;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

//    private EmployeeType permanentType;

    @Column(name = "epf")
    private boolean epf;

    @Column(name = "date_of_joining")
    private Date dateOfJoining;

    @Column(name = "date_of_leaving")
    private Date dateOfLeaving;

    @Column(name = "job_title", length = 50)
    private String jobTitle;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "age", length = 4)
    private String age;

    @Column(name = "blood_group",length = 40)
    private String bloodGroup;

    @Column(name = "adhar_number",length = 50, unique = true)
    private String adharNumber;

    @Column(name = "phone_number",length = 15)
    private String phoneNumber;

    @Column(name = "emergency_phone_number",length = 15)
    private String emergencyPhoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_type")
    private EmployeeType employeeType;

    @Column(name = "valid")
    private boolean valid;

    @Column(name = "status")
    private boolean status;

    @Column(name = "safety_vest")
    private boolean safetyVest;

    @Column(name = "gender",length = 10)
    private String gender;

    @Column(name = "religion")
    private String religion;

    @Column(name = "caste")
    private String caste;

    @Column(name = "sub_caste")
    private String subCaste;

    @Column(name = "marital_status",length = 210)
    private String maritalStatus;

    @Column(name = "image_path",length = 600)
    private String imagePath;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mother_name")
    private String motherName;

    @Column(name = "wife_name")
    private String wifeName;

    @Column(name = "pan_number",length = 10)
    private String panNumber;

    @Column(name = "permanent_address")
    private String permanentAddress;

    @Column(name = "current_address")
    private String currentAddress;

    //-----For Contract Employee--------


    @Column(name = "contract_company")
    private String contractCompany;

    @Column(name = "role")
    private String role;


    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "in_time")
    private long inTime;

    @Column(name = "out_time")
    private long outTime;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @CreationTimestamp
    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "site_id")
    private long siteId;

    @Column(name = "access_id")
    private String accessId;

    @Column(name = "email")
    private String email;

    @Column(name = "card_id")
    private String cardId;

}
