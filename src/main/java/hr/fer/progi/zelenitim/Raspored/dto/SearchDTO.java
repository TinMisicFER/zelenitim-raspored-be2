package hr.fer.progi.zelenitim.Raspored.dto;

import java.util.List;

public class SearchDTO {
	
	private List<GroupDTO> groups;
	private List<EmployeeDTO> employees;
	
	public SearchDTO() {}
	
	public SearchDTO(List<GroupDTO> groups, List<EmployeeDTO> employees) {
		this.groups = groups;
		this.employees= employees;
	}
	
	public List<GroupDTO> getGroups() {
		return groups;
	}
	public void setGroups(List<GroupDTO> groups) {
		this.groups = groups;
	}
	public List<EmployeeDTO> getEmployees() {
		return employees;
	}
	public void setEmployees(List<EmployeeDTO> employees) {
		this.employees = employees;
	}
	
	
}
