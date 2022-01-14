package hr.fer.progi.zelenitim.Raspored.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import hr.fer.progi.zelenitim.Raspored.dto.EmployeeDTO;
import hr.fer.progi.zelenitim.Raspored.dto.GroupDTO;
import hr.fer.progi.zelenitim.Raspored.dto.SearchDTO;
import hr.fer.progi.zelenitim.Raspored.obj.Assignment;
import hr.fer.progi.zelenitim.Raspored.obj.Employee;
import hr.fer.progi.zelenitim.Raspored.obj.Group;
import hr.fer.progi.zelenitim.Raspored.obj.Task;
import hr.fer.progi.zelenitim.Raspored.service.EmployeeService;
import hr.fer.progi.zelenitim.Raspored.service.GroupService;
import hr.fer.progi.zelenitim.Raspored.service.TaskService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*") //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
public class SearchController {
	private final TaskService taskService;
	private final GroupService groupService;
	private final EmployeeService empService;

	@Autowired
	public SearchController(TaskService taskService,GroupService groupService,EmployeeService empService) {
		this.taskService = taskService;
		this.groupService = groupService;
		this.empService = empService;
	}
	
	@GetMapping("/search")
	public SearchDTO search(@RequestParam String q) {
		List<Group> grps = groupService.like(q);
		List<Employee> emps = empService.like(q);
		
		List<GroupDTO> gDTOs = new ArrayList<>();
		for(Group one:grps) {
			gDTOs.add(new GroupDTO(one));
		}
		
		List<EmployeeDTO> eDTOs = new ArrayList<>();
		for(Employee one:emps) {
			eDTOs.add(new EmployeeDTO(one));
		}
		
		return new SearchDTO(gDTOs,eDTOs);
	}

	@GetMapping("/totalHours")
	public int getTotalHours(@AuthenticationPrincipal User user) {
		int total = 0;
		
		if(!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return -1;
		}
		
		List<Group> groups = groupService.listAll();
		for(Group grp: groups) {
			int groupId = grp.getId();
		
			List<Task> tasks = taskService.listByGroup(groupId);
			
			for(Task tsk: tasks) {
				Set<Assignment> assigs = tsk.getAssignments();
				for(Assignment assig:assigs) {
					total+=assig.getHoursWorked();
				}
			}
		}
		
		return total;
	}
	
	@GetMapping("/capital")
	public double getCapital(@AuthenticationPrincipal User user) {
		if(!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return Double.NaN;
		}
		
		double capital = 0.0;
		
		List<Group> groups = groupService.listAll();
		for(Group grp: groups) {
			int totalHours = 0;
			int expectedHours = 0;
			double pricePerHour = grp.getActivity().getPricePerHour();
			
			int groupId = grp.getId();
		
			List<Task> tasks = taskService.listByGroup(groupId);
			
			for(Task tsk: tasks) {
				expectedHours += tsk.getEstimatedNumberOfHours();
				
				Set<Assignment> assigs = tsk.getAssignments();
				for(Assignment assig:assigs) {
					totalHours += assig.getHoursWorked();
				}
			}
			
			double delta = expectedHours - totalHours;
			
			if(!grp.isActive() || (grp.isActive() && delta<0)) //calculate for finished groups and groups that jave exceeded expected number of hours
				capital += delta * pricePerHour;
		}
		
		return capital;
	}
	

}
