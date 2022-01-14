package hr.fer.progi.zelenitim.Raspored.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.progi.zelenitim.Raspored.obj.Group;
import hr.fer.progi.zelenitim.Raspored.obj.Task;
import hr.fer.progi.zelenitim.Raspored.obj.Activity;
import hr.fer.progi.zelenitim.Raspored.obj.Employee;
import hr.fer.progi.zelenitim.Raspored.repository.ActivityRepository;
import hr.fer.progi.zelenitim.Raspored.repository.EmployeeRepository;
import hr.fer.progi.zelenitim.Raspored.repository.GroupRepository;

@Service
public class GroupService {
	@Autowired
	private GroupRepository groupRepo;
	
	@Autowired
	private EmployeeRepository empRepo;
	
	@Autowired
	private ActivityRepository actRepo;
	
	public void addNewGroup(String leaderUsername,String name,Integer activityId) {
		Optional<Employee> lead = empRepo.findByUsername(leaderUsername);
		Optional<Activity> act = actRepo.findById(activityId);
		if(lead.isEmpty())
			throw new RuntimeException("No employee with username " + leaderUsername);
		if(act.isEmpty())
			throw new RuntimeException("No activity with ID " + activityId);
		
		Group group = new Group(lead.get(),name,act.get());
		
		groupRepo.save(group);
	}
	
	public void saveGroup(Group grupa) {
		groupRepo.save(grupa);
	}
	
	public List<Group> listAll() {
		return groupRepo.findAll();
	}
	
	public Optional<Group> findById(int groupId) {
		return groupRepo.findById(groupId);	
	}
	
	public List<Group> like(String query){
		return groupRepo.likeQuery("%" + query + "%");
	}
	
	public Set<Employee> listMembers(int groupId) {
	    Optional<Group> grp = groupRepo.findById(groupId);
	    if(grp.isPresent()) {
	    	return grp.get().getMembers();
	    }else {
	    	return new HashSet<Employee>();
	    }
	}
	
	public boolean addMember(int groupId, String username) {
		Optional<Group> grpOpt = groupRepo.findById(groupId);
		Optional<Employee> empOpt = empRepo.findByUsername(username);
		
		if(grpOpt.isEmpty() || empOpt.isEmpty()){
			return false;
		}
		
		Group grupa = grpOpt.get();
		Employee emp = empOpt.get();
		HashSet<Employee> employees = new HashSet<>(grupa.getMembers());
		employees.add(emp);
		grupa.setMembers(employees);
		
		groupRepo.save(grupa);
		return true;
	}
	
	public boolean removeMember(int groupId, String username) {
		Optional<Group> grpOpt = groupRepo.findById(groupId);
		Optional<Employee> empOpt = empRepo.findByUsername(username);
		
		if(grpOpt.isEmpty() || empOpt.isEmpty()){
			return false;
		}
		
		Group grupa = grpOpt.get();
		Employee emp = empOpt.get();
		if(emp.equals(grupa.getLeader()))
			return false;
		
		HashSet<Employee> employees = new HashSet<>(grupa.getMembers());
		employees.remove(emp);
		grupa.setMembers(employees);
		
		groupRepo.save(grupa);
		return true;
	}
	
//	public boolean addTask(int groupId, Set<Employee> giveTo) {
//		return false;
//	}
//	
//	public boolean modifyTask(int groupID, int taskID) {
//		return false;
//	}
	
	public boolean changeLeader(int groupId, String username) {
		Optional<Group> grpOpt = groupRepo.findById(groupId);
		Optional<Employee> empOpt = empRepo.findByUsername(username);
		
		if(grpOpt.isEmpty() || empOpt.isEmpty()){
			System.out.println("GroupService::changeLeader -----> cannot find group or employee.");
			return false;
		}
		
		Group grupa = grpOpt.get();
		Employee emp = empOpt.get();
		grupa.setLeader(emp);
		Set<Employee> empSet = grupa.getMembers();
		empSet.add(emp);
		grupa.setMembers(empSet);
		
		groupRepo.save(grupa);
		return true;
	}
	
	public boolean renderInactive(int groupId) {
		Optional<Group> grpOpt = groupRepo.findById(groupId);
		
		if(grpOpt.isEmpty()){
			return false;
		}
		
		Group grupa = grpOpt.get();
		grupa.setActive(false);
		
		groupRepo.save(grupa);
		return true;
	}
	
	
	
}
