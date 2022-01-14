package hr.fer.progi.zelenitim.Raspored.dto;

import hr.fer.progi.zelenitim.Raspored.obj.Employee;

public class EmployeeDTO {
	private Integer id;
	private String name;
	private String lastName;
	private String email;
	private String OIB;
	private String username;


	public EmployeeDTO(Integer id, String name, String lastName, String email, String oIB,
			String username) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		OIB = oIB;
		this.username = username;
	}


	public EmployeeDTO(Employee employee) {
		this.id = employee.getId();
		this.name = employee.getName();
		this.lastName = employee.getLastName();
		this.email = employee.getEmail();
		this.OIB = employee.getOIB();
		this.username = employee.getUsername();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOIB() {
		return OIB;
	}
	public void setOIB(String oIB) {
		OIB = oIB;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}



}
