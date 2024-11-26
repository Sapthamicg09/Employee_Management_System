package com.sap.employee_management.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee 
{
	@Id
	private int Emp_id;
	@Column
	private String Emp_name;
	@Column
	private double Salary;
	@Column
	private long Phone;
	@Column
	private String Email;
	@Column
	private String Type;
	
	
	public int getEmp_id() {
		return Emp_id;
	}
	public void setEmp_id(int emp_id) {
		Emp_id = emp_id;
	}
	public String getEmp_name() {
		return Emp_name;
	}
	public void setEmp_name(String emp_name) {
		Emp_name = emp_name;
	}
	public double getSalary() {
		return Salary;
	}
	public void setSalary(double salary) {
		Salary = salary;
	}
	public long getPhone() {
		return Phone;
	}
	public void setPhone(long phone) {
		Phone = phone;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	
	
	

}
