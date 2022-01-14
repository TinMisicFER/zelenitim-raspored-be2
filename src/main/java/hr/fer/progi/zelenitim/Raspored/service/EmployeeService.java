package hr.fer.progi.zelenitim.Raspored.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hr.fer.progi.zelenitim.Raspored.dto.GroupDTO;
import hr.fer.progi.zelenitim.Raspored.dto.TaskDTO;
import hr.fer.progi.zelenitim.Raspored.obj.Employee;
import hr.fer.progi.zelenitim.Raspored.obj.Group;
import hr.fer.progi.zelenitim.Raspored.repository.EmployeeRepository;
import hr.fer.progi.zelenitim.Raspored.repository.GroupRepository;


@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository empRepo;
	
	@Autowired
	private GroupRepository groupRepo;
	

	public List<Employee> getEmployees() {
		return empRepo.findAll();
	}
	
	public List<Employee> like(String query) {
		return empRepo.likeQuery("%" + query + "%");
	}

	public void addNewEmployee(Employee employee) {
		empRepo.save(employee);
	}

	public boolean removeEmployee(Employee employee) {
		if(employee != null) {
			for(Group g: employee.getGroupsIAmAMemberOf()) {
				if(employee.equals(g.getLeader()))
					return false;
				
				HashSet<Employee> employees = new HashSet<>(g.getMembers());
				employees.remove(employee);
				g.setMembers(employees);
				
				groupRepo.save(g);
			}
			empRepo.delete(employee);
			return true;
		} else {
			return false;
		}

	}
	
	
	/**
	 * not implemented
	 * @param emp
	 * @return
	 */
	public int getNumberOfWorkedHours(Employee emp) {
		return 0;
	}
	
	/**
	 * not implemented
	 * @param hoursWorked
	 * @return
	 */
	
	public boolean enterNumberOfHoursWorkedToday(int hoursWorked) {
		return true;
	}
	
	public Employee getEmployeeByName(String name) {
		return null;
	}
	
	public Employee getEmployeeByLastName(String lastName) {
		return null;
	}

	public Optional<Employee> findByUsername(String username) {
	    Assert.notNull(username, "Username must be given");
	    return empRepo.findByUsername(username);
	  }

	public Optional<Employee> getEmployeeById(int id) {
		return empRepo.findById(id);
	}

	public List<TaskDTO> getTasksForEmployee(int id) {
		Optional<Employee> emp = empRepo.findById(id);
		if(emp.isEmpty()) return null;
		List<TaskDTO> tasksForEmp = emp.get().getAssignments().stream().map(assignment -> new TaskDTO(assignment.getTask())).collect(Collectors.toList());
		return tasksForEmp;
	}

	public List<GroupDTO> getGroupsForEmployee(int id) {
		List<Group> allGroups = groupRepo.findAll();
		List<GroupDTO> employeeGroups = allGroups.stream().filter(
				g -> g.getMembers().stream()
				.map(mem -> mem.getId())
				.collect(Collectors.toSet())
				.contains(id))
				.map(g -> new GroupDTO(g))
		.collect(Collectors.toList());
		return employeeGroups;
	}

	public void updateEmployee(Employee employee) {
		empRepo.updateEmployee(employee.getId(), employee.getName(), employee.getLastName(), employee.getEmail(), employee.getOIB(), employee.getUsername(), employee.getPassword());
		
	}

	public List<Group> getGroupsForEmployee(String username) {
		List<Group> allGroups = groupRepo.findAll();
		List<Group> employeeGroups = allGroups.stream().filter(
				g -> g.getMembers().stream()
				.map(mem -> mem.getUsername())
				.collect(Collectors.toSet())
				.contains(username))
		.collect(Collectors.toList());
		return employeeGroups;
	}
}
