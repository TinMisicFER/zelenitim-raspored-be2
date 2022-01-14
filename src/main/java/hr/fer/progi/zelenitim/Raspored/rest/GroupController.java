package hr.fer.progi.zelenitim.Raspored.rest;

import java.util.*;

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

import hr.fer.progi.zelenitim.Raspored.obj.Group;
import hr.fer.progi.zelenitim.Raspored.obj.Task;
import hr.fer.progi.zelenitim.Raspored.dto.CreateGroupDTO;
import hr.fer.progi.zelenitim.Raspored.dto.EmployeeDTO;
import hr.fer.progi.zelenitim.Raspored.dto.UsernameDTO;
import hr.fer.progi.zelenitim.Raspored.dto.GroupDTO;
import hr.fer.progi.zelenitim.Raspored.dto.TaskDTO;
import hr.fer.progi.zelenitim.Raspored.obj.Assignment;
import hr.fer.progi.zelenitim.Raspored.obj.Employee;
import hr.fer.progi.zelenitim.Raspored.service.EmployeeService;
import hr.fer.progi.zelenitim.Raspored.service.GroupService;
import hr.fer.progi.zelenitim.Raspored.service.TaskService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/group")
public class GroupController {
	private final GroupService groupService;
	private final TaskService taskService;
	private final EmployeeService empService;


	@Autowired
	public GroupController(GroupService groupService,TaskService taskService,EmployeeService empService) {
		this.groupService = groupService;
		this.taskService = taskService;
		this.empService = empService;
	}

	@GetMapping("")
	public List<GroupDTO> usersGroups(@AuthenticationPrincipal User user){
		List<Group> grps;
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			grps = groupService.listAll();
		}else {
			grps = empService.getGroupsForEmployee(user.getUsername());
		}

		List<GroupDTO> gDTOs = new ArrayList<>();
		for(Group g:grps) {
			GroupDTO gdto = new GroupDTO(g);
			gDTOs.add(gdto);
		}

		return gDTOs;
	}

	@GetMapping("/{id}")
	public GroupDTO index(@PathVariable String id) {
		int idn;
		try {
			idn = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return null;
		}
		Optional<Group> grp =  groupService.findById(idn);
		if(grp.isPresent()) {
			Group grupa =  grp.get();
			GroupDTO gDTO = new GroupDTO(grupa);

			return gDTO;
		}
		return null;
	}

	@PostMapping("")
	public ResponseEntity<Group> createGroup(@RequestBody CreateGroupDTO cgDTO,@AuthenticationPrincipal User user) { //args: (@RequestBody CreateGroupDTO dto, @AuthenticationPrincipal Employee user)
		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			System.out.println("Creating new group: "+cgDTO.getLeaderUsername()+cgDTO.getName()+cgDTO.getActivityId());
			groupService.addNewGroup(cgDTO.getLeaderUsername(),cgDTO.getName(),cgDTO.getActivityId());
			return new ResponseEntity<Group>(HttpStatus.OK);
		}
		return new ResponseEntity<Group>(HttpStatus.FORBIDDEN);
	}

	@PostMapping("/{id}/change_leader")
	public ResponseEntity<Group> changeLeader(@RequestBody UsernameDTO uDTO,@PathVariable String id,@AuthenticationPrincipal User user) { //args: (@RequestBody CreateGroupDTO dto, @AuthenticationPrincipal Employee user)
		int groupId;
		try {
			groupId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return new ResponseEntity<Group>(HttpStatus.BAD_REQUEST);
		}

		if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			boolean success = groupService.changeLeader(groupId,uDTO.getNewEmployeeUsername());
			if(success)
				return new ResponseEntity<Group>(HttpStatus.OK);
			else
				return new ResponseEntity<Group>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Group>(HttpStatus.FORBIDDEN);
	}

	@PostMapping("/{id}/add_member")
	public ResponseEntity<Group> addMember(@RequestBody UsernameDTO uDTO,@AuthenticationPrincipal User user,@PathVariable String id) {		
		int groupId;
		try {
			groupId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return new ResponseEntity<Group>(HttpStatus.BAD_REQUEST);
		}

		if(!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return new ResponseEntity<Group>(HttpStatus.FORBIDDEN);
		}

		//System.out.println("[TIN] ---> "+newEmployeeUsername);

		boolean success = groupService.addMember(groupId,uDTO.getNewEmployeeUsername());

		if(!success)
			return new ResponseEntity<Group>(HttpStatus.CONFLICT);

		return new ResponseEntity<Group>(HttpStatus.OK);
	}

	@PostMapping("/{id}/remove_member")
	public ResponseEntity<Group> removeMember(@RequestBody UsernameDTO uDTO,@AuthenticationPrincipal User user,@PathVariable String id) {
		int groupId;
		try {
			groupId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return new ResponseEntity<Group>(HttpStatus.BAD_REQUEST);
		}

		if(!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return new ResponseEntity<Group>(HttpStatus.FORBIDDEN);
		}


		boolean success = groupService.removeMember(groupId,uDTO.getNewEmployeeUsername());

		if(!success)
			return new ResponseEntity<Group>(HttpStatus.CONFLICT);

		return new ResponseEntity<Group>(HttpStatus.OK);
	}

	@GetMapping("/{id}/members")
	public List<EmployeeDTO> getMembers(@PathVariable String id){
		int groupId;
		try {
			groupId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return null;
		}

		Set<Employee> empSet =groupService.listMembers(groupId);

		List<EmployeeDTO> ls =  new ArrayList<EmployeeDTO>();

		for(Employee emp: empSet) {
			ls.add(new EmployeeDTO(emp));
		}

		return ls;
	}

	@GetMapping("/{id}/hours")
	public int getTotalHours(@PathVariable String id, @AuthenticationPrincipal User user) {
		int total = 0;

		int groupId;
		try {
			groupId = Integer.parseInt(id);
		}catch(NumberFormatException e) {
			return -1;
		}

		if(!isLeaderOrAdmin(user, groupId)) {
			return -1;
		}

		List<Task> tasks = taskService.listByGroup(groupId);
		System.out.println("Tasks of group "+groupId+" :");
		System.out.println(tasks);

		for(Task tsk: tasks) {
			Set<Assignment> assigs = tsk.getAssignments();
			for(Assignment assig:assigs) {
				total+=assig.getHoursWorked();
			}
		}

		return total;
	}

	@GetMapping("/{id}/realisation")
	public double getRealisation(@PathVariable String id,@AuthenticationPrincipal User user) {
		double percentage = 0.0;
		int totalHours = 0;
		int expectedHours = 0;

		int groupId;
		try {
			groupId = Integer.parseInt(id);
		}catch(NumberFormatException e) {
			return -1;
		}

		if(!isLeaderOrAdmin(user, groupId)) {
			return -1;
		}

		List<Task> tasks = taskService.listByGroup(groupId);

		for(Task tsk: tasks) {
			expectedHours += tsk.getEstimatedNumberOfHours();

			Set<Assignment> assigs = tsk.getAssignments();
			for(Assignment assig:assigs) {
				totalHours += assig.getHoursWorked();
			}
		}

		percentage = 1.0 * totalHours / expectedHours;
		return percentage;
	}

	@GetMapping("/{id}/tasks")
	public Set<TaskDTO> getGroupTasks(@PathVariable String id,@AuthenticationPrincipal User user){
		int groupId;
		try {
			groupId = Integer.parseInt(id);
		}catch(NumberFormatException e) {
			return null;
		}

		if(!isLeaderOrAdmin(user, groupId))
			return null;
		Optional<Group> grp = groupService.findById(groupId);
		if (grp.isEmpty()) {
			return null;
		}
		Set<Task> tasks = grp.get().getTasks();
		Set<TaskDTO> ls = new HashSet<>();
		for(Task tsk: tasks) {
			ls.add(new TaskDTO(tsk));
		}

		return ls;
	}

	@GetMapping("/{id}/finish")
	public ResponseEntity<Group> inactivateGroup(@PathVariable String id,@AuthenticationPrincipal User user){
		if(!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return new ResponseEntity<Group>(HttpStatus.FORBIDDEN);
		}

		int groupId;
		try {
			groupId = Integer.parseInt(id);
		}catch(NumberFormatException e) {
			return null;
		}

		boolean success = groupService.renderInactive(groupId);

		if(success)
			return new ResponseEntity<Group>(HttpStatus.OK);
		else
			return new ResponseEntity<Group>(HttpStatus.CONFLICT);
	}

	@GetMapping("/{id}/leader")
	public EmployeeDTO getLeaderOfGroup(@PathVariable String id,@AuthenticationPrincipal User user){
		int groupId;
		try {
			groupId = Integer.parseInt(id);
		}catch(NumberFormatException e) {
			return null;
		}

		Optional<Group> grp = groupService.findById(groupId);
		if (grp.isEmpty()) {
			return null;
		}
		
		return new EmployeeDTO(grp.get().getLeader());
		}



		private boolean isLeaderOrAdmin(User user,int groupId) {
			if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
				return true;
			}

			Optional<Group> grpOpt = groupService.findById(groupId);
			if(grpOpt.isEmpty()) {
				return false;
			}
			Group grupa = grpOpt.get();

			String username = user.getUsername();
			Optional<Employee> emp = empService.findByUsername(username);
			if(emp.isEmpty()) {
				return false;
			}
			Employee maybeLeader = emp.get();
			if(!maybeLeader.equals(grupa.getLeader()))
				return false;

			return true;
		}
	}
