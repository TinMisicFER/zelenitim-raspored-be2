package hr.fer.progi.zelenitim.Raspored.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.progi.zelenitim.Raspored.dto.CreateEmployeeDTO;
import hr.fer.progi.zelenitim.Raspored.dto.EmployeeDTO;
import hr.fer.progi.zelenitim.Raspored.dto.GroupDTO;
import hr.fer.progi.zelenitim.Raspored.dto.TaskDTO;
import hr.fer.progi.zelenitim.Raspored.obj.Employee;
import hr.fer.progi.zelenitim.Raspored.obj.Group;
import hr.fer.progi.zelenitim.Raspored.obj.Task;
import hr.fer.progi.zelenitim.Raspored.repository.EmployeeRepository;
import hr.fer.progi.zelenitim.Raspored.service.EmployeeService;
import hr.fer.progi.zelenitim.Raspored.service.GroupService;
import hr.fer.progi.zelenitim.Raspored.service.TaskService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/employee")
public class EmployeeController {
	private final EmployeeService empService;
	private final GroupService groupService;

	@Autowired
	public EmployeeController(EmployeeService empService, GroupService groupService) {
		this.empService = empService;
		this.groupService = groupService;
	}


	@GetMapping("")
	public List<EmployeeDTO> index(@AuthenticationPrincipal User user) {
		if(!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))) {
			return new ArrayList<EmployeeDTO>();
		}

		List<EmployeeDTO> allEmployees = empService.getEmployees().stream().map(emp -> new EmployeeDTO(emp)).collect(Collectors.toList());
		return allEmployees;
	}

	@GetMapping("/getbyusername/{username}")
	public EmployeeDTO findByUsername(@PathVariable String username) {

		Optional<Employee> emp = empService.findByUsername(username);
		if(emp.isPresent()) {
			Employee employee = emp.get(); 
			EmployeeDTO eDto = new EmployeeDTO(employee);
			return eDto;
		}

		return null;

	}


	@PostMapping("")
	public ResponseEntity<Employee> registerNewEmployee(@RequestBody CreateEmployeeDTO employeeDTO, @AuthenticationPrincipal User user) {
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			System.out.println(employeeDTO);
			empService.addNewEmployee(employeeDTO.createEmployeeFromCreateEmloyeeDTO());
			return new ResponseEntity<Employee>(HttpStatus.OK);
		}
		return new ResponseEntity<Employee>(HttpStatus.FORBIDDEN);
	}

	@GetMapping("/{id}")
	public EmployeeDTO findById(@PathVariable String id) {
		int idn;
		try {
			idn = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return null;
		}

		Optional<Employee> emp = empService.getEmployeeById(idn);
		if(emp.isPresent()) {
			Employee employee = emp.get(); 
			EmployeeDTO eDto = new EmployeeDTO(employee);
			return eDto;
		}

		return null;

	}
	
	@PostMapping("/{id}/delete")
	public ResponseEntity<Employee> deleteById(@PathVariable String id, @AuthenticationPrincipal User user) {
		
		if(!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return new ResponseEntity<Employee>(HttpStatus.FORBIDDEN);
		}
		
		int idn;
		try {
			idn = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return null;
		}
	
		Optional<Employee> emp = empService.getEmployeeById(idn);
		if(emp.isEmpty()) { 
			return new ResponseEntity<Employee>(HttpStatus.BAD_REQUEST);
		}

		Employee employee = emp.get();
		List<Group> allGroups = groupService.listAll();
		Set<Employee> allLeaders=  allGroups.stream().map(group -> group.getLeader()).collect(Collectors.toSet());
		
		if(employee.getUsername().equals("direktor")) {
			return new ResponseEntity<Employee>(HttpStatus.EXPECTATION_FAILED);
		}
		
		if(allLeaders.contains(employee)) {
			return new ResponseEntity<Employee>(HttpStatus.EXPECTATION_FAILED);
		}
		if(!empService.removeEmployee(employee)) return new ResponseEntity<Employee>(HttpStatus.EXPECTATION_FAILED);
		return new ResponseEntity<Employee>(HttpStatus.OK);
		

	}

	@GetMapping("/{id}/bussyness")
	public List<TaskDTO> getBussyness(@PathVariable String id, @AuthenticationPrincipal User user) {

		int idn;
		try {
			idn = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return null;
		}

		if(!(isAdmin(user) || isLeader(user, idn) || isHimself(user, idn) )) {
			return null;
		}


		List<TaskDTO> tasks = empService.getTasksForEmployee(idn);

		return tasks;	

	}
	
	@GetMapping("/{id}/hoursWorked")
	public Integer getHoursWorkedThisMonth(@PathVariable String id, @AuthenticationPrincipal User user) {

		int idn;
		try {
			idn = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return null;
		}

		if(!(isAdmin(user) || isLeader(user, idn) || isHimself(user, idn) )) {
			return null;
		}

		Optional<Employee> optEmp = empService.getEmployeeById(idn);
		
		if(optEmp.isEmpty()) {
			return null;
		}
		
		Employee employee = optEmp.get();
		Integer numberOfHoursWorked = employee.getAssignments().stream().mapToInt(a -> a.getHoursWorked()).sum();

		return numberOfHoursWorked;	

	}
	
	@GetMapping("/{id}/groups")
	public List<GroupDTO> getMyGroups(@PathVariable String id, @AuthenticationPrincipal User user) {
		int idn;
		try {
			idn = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return null;
		}

		if(!(isAdmin(user) || isLeader(user, idn) || isHimself(user, idn) )) {
			return null;
		}

		List<GroupDTO> groups = empService.getGroupsForEmployee(idn);
		if(groups.isEmpty()) {
			return null;
		}

		return groups;	

	}

	@PostMapping("/{id}/changeRecords")
	public ResponseEntity<Employee> changeEmployeeRecords(@PathVariable String id, @RequestBody CreateEmployeeDTO employeeDTO, @AuthenticationPrincipal User user) {
		if(!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) 
			return new ResponseEntity<Employee>(HttpStatus.FORBIDDEN);

		int idn;
		try {
			idn = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return null;
		}

		Optional<Employee> optEmp = empService.getEmployeeById(idn);
		
		if(optEmp.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Employee employee = optEmp.get();
		if(employeeDTO.getName() != null) {
			 employee.setName(employeeDTO.getName());
		 }
		
		if(employeeDTO.getLastName() != null) {
			 employee.setLastName(employeeDTO.getLastName());
		 }
		
		if(employeeDTO.getEmail() != null) {
			 employee.setEmail(employeeDTO.getEmail());
		 }
		
		if(employeeDTO.getOIB() != null) {
			 employee.setOIB(employeeDTO.getOIB());
		 }
		
		if(employeeDTO.getPassword() != null) {
			 employee.setPassword(employeeDTO.getPassword());
		 }
		
		if(employeeDTO.getUsername() != null) {
			 employee.setUsername(employeeDTO.getUsername());
		 }
		
		empService.updateEmployee(employee);
		
		return new ResponseEntity<Employee>(HttpStatus.OK);
	}
	
	




	private boolean isAdmin(User user) {
		return user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	private boolean isLeader(User user, int empId) {
		Optional<Employee> optEmp = empService.getEmployeeById(empId);
		if(optEmp.isEmpty()) {
			return false;
		}
		Employee emp = optEmp.get();
		Set<Group> empMemberOf = emp.getGroupsIAmAMemberOf();

		Optional<Employee> optUserAsEmployee = empService.findByUsername(user.getUsername());

		if(optUserAsEmployee.isEmpty()) {
			return false;
		}

		Employee userAsEmployee = optUserAsEmployee.get();
		Set<Group> userLeaderOf = userAsEmployee.getLeaderOf();


		for(Group g: empMemberOf) {
			if(userLeaderOf.contains(g))
				return true;
		}

		return false;


	}

	private boolean isHimself(User user, int empId) {
		Optional<Employee> optEmp = empService.getEmployeeById(empId);
		if(optEmp.isEmpty()) {
			return false;
		}
		Employee emp = optEmp.get();


		String username = user.getUsername();
		Optional<Employee> empUser = empService.findByUsername(username);
		if(empUser.isEmpty()) {
			return false;
		}

		Employee userAsEmployee = empUser.get();

		return emp.equals(userAsEmployee);
	}



}
