package com.orileo.attendance.wrapper.entity.mysql.astra;
/*
 * @author Ashvini B
 */

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name="employee")
public class Employee implements Serializable{

	private static final long serialVersionUID = -3781887609609178736L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name="reference_Id")
	private String referenceId;

	@Column(name="employee_code")
	private String employeeCode;

	@Column(name="first_name")
	private String firstName;

	@Column(name="middle_name")
	private String middleName;

	@Column(name="last_name")
	private String lastName;

	@Column
	private Boolean status;

	public Employee(){}
}