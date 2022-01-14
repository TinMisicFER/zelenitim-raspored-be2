package hr.fer.progi.zelenitim.Raspored.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.progi.zelenitim.Raspored.dto.IntegerDTO;
import hr.fer.progi.zelenitim.Raspored.dto.UserDTO;
import hr.fer.progi.zelenitim.Raspored.obj.Employee;
import hr.fer.progi.zelenitim.Raspored.obj.Group;
import hr.fer.progi.zelenitim.Raspored.service.EmployeeService;
import hr.fer.progi.zelenitim.Raspored.service.GroupService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/user")
public class UserController {
	private final EmployeeService empService;
	private final GroupService groupService;

	@Autowired
	public UserController(EmployeeService empService, GroupService groupService) {
		this.empService = empService;
		this.groupService = groupService;
	}

    @PostMapping("")
    public UserDTO getCurrentUser(@RequestBody IntegerDTO iDTO,@AuthenticationPrincipal User user) {
    	if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return new UserDTO(user.getUsername(),true);
		}    	
    	
    	int id = iDTO.getNumber();
    	if(id<0)
    		return new UserDTO(user.getUsername(),false);
		
		Optional<Group> grpOpt = groupService.findById(id);
		if(grpOpt.isEmpty()) {
			return new UserDTO(user.getUsername(),false);
		}
		Group grupa = grpOpt.get();
		
		String username = user.getUsername();
		Optional<Employee> emp = empService.findByUsername(username);
		if(emp.isEmpty()) {
			return new UserDTO(user.getUsername(),false);
		}
		Employee maybeLeader = emp.get();
		if(!maybeLeader.equals(grupa.getLeader()))
			return new UserDTO(user.getUsername(),false);;
		
		return new UserDTO(user.getUsername(),true);
    }
}
