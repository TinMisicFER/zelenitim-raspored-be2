package hr.fer.progi.zelenitim.Raspored.service;

import hr.fer.progi.zelenitim.Raspored.obj.*;
import hr.fer.progi.zelenitim.Raspored.repository.AssignmentRepository;
import hr.fer.progi.zelenitim.Raspored.repository.EmployeeRepository;
import hr.fer.progi.zelenitim.Raspored.repository.GroupRepository;
import hr.fer.progi.zelenitim.Raspored.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private GroupRepository grpRepo;

    @Autowired
    private EmployeeRepository empRepo;

    @Autowired
    private AssignmentRepository assignRepo;

    public List<Task> listAll() { return taskRepository.findAll(); }

    public Optional<Task> findById(int taskId) {
        return taskRepository.findById(taskId);
    }

    public void addNewTask(String description,Integer groupId,Integer empId) {
        Optional<Group> grp = grpRepo.findById(groupId);
        Optional<Employee> emp = empRepo.findById(empId);
        if (grp.isEmpty())
            throw new RuntimeException("No group with ID " + groupId);
        if (emp.isEmpty())
            throw new RuntimeException("No employee with ID " + empId);
        Location loc = new Location((float)45.8000466,(float)15.9646868);
        Task task = new Task(description,grp.get(),loc);
        taskRepository.save(task);
        Set<Task> grpTasks = grp.get().getTasks();
        grpTasks.add(task);
        grp.get().setTasks(grpTasks);
        grpRepo.save(grp.get());
        newAssignment(emp.get(),task,groupId);
    }
    
    //dodao Tin za DataInitializer
    public void saveTask(Task task) {
    	taskRepository.save(task);
    }

    //novi assignment pri stvarnju novog zadatka
    public void newAssignment(Employee emp, Task task, int groupId) {
        Optional<Group> grp = grpRepo.findById(groupId);
        if (grp.get().getTasks().isEmpty()) {
            Set<Task> grptasks = new HashSet<Task>();
            grptasks.add(task);
            grp.get().setTasks(grptasks);
            grpRepo.save(grp.get());
            }
        else {
            if (!grp.get().getTasks().contains(task)) {
            Set<Task> grpTasks = grp.get().getTasks();
            grpTasks.add(task);
            grp.get().setTasks(grpTasks);
            grpRepo.save(grp.get());
         }
        }

        Assignment assign = new Assignment(emp,task,0);
        assignRepo.save(assign);
        HashSet<Assignment> assigns = new HashSet<Assignment>();
        assigns.add(assign); //dodao Tin
        task.setAssignments(assigns);
    }

    //pridjeljivanje zadatka jos jednom djelatniku
    public void addAssignment(Employee emp, Task task) {
        Assignment assign = new Assignment(emp,task,0);
        assignRepo.save(assign);
        Set<Assignment> assigns = task.getAssignments();
        assigns.add(assign);
        task.setAssignments(assigns);
    }

    public boolean changeDescription(String desc,Integer taskId) {
        Optional<Task> tskOpt = taskRepository.findById(taskId);
        if(tskOpt.isEmpty()){
            System.out.println("TaskService::changeDescription -----> cannot find task.");
            return false;
        }
        Task task = tskOpt.get();
        task.setDescription(desc);
        taskRepository.save(task);
        return true;
    }

    public boolean changeLocation(int taskId,String adr,float lat,float lon) {
        Optional<Task> tskOpt = taskRepository.findById(taskId);
        if(tskOpt.isEmpty()){
            System.out.println("TaskService::changeLocation -----> cannot find task.");
            return false;
        }
        Task task = tskOpt.get();
        Location loc = new Location(lat,lon);
        loc.setAddress(adr);
        task.setLocation(loc);
        taskRepository.save(task);
        return true;
    }

    public boolean changeEstimatedHours(int taskId,int esth) {
        Optional<Task> tskOpt = taskRepository.findById(taskId);
        if(tskOpt.isEmpty()){
            System.out.println("TaskService::changeNumberOfEstimatedHours -----> cannot find task.");
            return false;
        }
        Task task = tskOpt.get();
        if (task.getStartDate() == null || task.getEndDate() == null) {
            return false;
        }
        long diff = task.getEndDate().getTime() - task.getStartDate().getTime();
        int days = (int) (diff / 1000 / 60 / 60 / 24);
        int weeks = days/7;
        long maxHours = weeks*40 + (days-weeks*7)*8;
        if (esth > maxHours) {
            return false;
        }
        task.setEstimatedNumberOfHours(esth);
        taskRepository.save(task);
        return true;
    }

    public boolean changeStartDate(int taskId, java.sql.Date start){
        Optional<Task> tskOpt = taskRepository.findById(taskId);
        if(tskOpt.isEmpty()){
            System.out.println("TaskService::changeStartDate -----> cannot find task.");
            return false;
        }
        Task task = tskOpt.get();
        if (task.getStartDate() == null || task.getEndDate() == null) {
            return false;
        }
        long diff = task.getEndDate().getTime() - start.getTime();
        int days = (int) (diff / 1000 / 60 / 60 / 24);
        int weeks = days/7;
        long maxHours = weeks*40 + (days-weeks*7)*8;
        if (task.getEstimatedNumberOfHours() > maxHours) {
            return false;
        }
        task.setStartDate(start);
        taskRepository.save(task);
        return true;
    }

    public boolean changeEndDate(int taskId,Date end){
        Optional<Task> tskOpt = taskRepository.findById(taskId);
        if(tskOpt.isEmpty()){
            System.out.println("TaskService::changeStartDate -----> cannot find task.");
            return false;
        }
        Task task = tskOpt.get();
        if (task.getStartDate() == null || task.getEndDate() == null) {
            return false;
        }
        long diff = end.getTime() - task.getStartDate().getTime();
        int days = (int) (diff / 1000 / 60 / 60 / 24);
        int weeks = days/7;
        long maxHours = weeks*40 + (days-weeks*7)*8;
        if (task.getEstimatedNumberOfHours() > maxHours) {
            return false;
        }
        task.setEndDate(end);
        taskRepository.save(task);
        return true;
    }



    /**List of all tasks belonging to the group with groupId
     *
     * @param groupId
     * @return
     */
    public List<Task> listByGroup(int groupId) {
    	Optional<Group> g = grpRepo.findById(groupId);
    	
    	if(g.isEmpty()) {
    		return new ArrayList<Task>();
    	}
    	
    	Group grupa = g.get();
    	return List.copyOf(grupa.getTasks());
    }


}