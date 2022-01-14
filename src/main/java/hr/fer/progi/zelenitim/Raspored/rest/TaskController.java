package hr.fer.progi.zelenitim.Raspored.rest;

import hr.fer.progi.zelenitim.Raspored.dto.IntegerDTO;
import hr.fer.progi.zelenitim.Raspored.dto.StringDTO;
import hr.fer.progi.zelenitim.Raspored.dto.CreateTaskDTO;
import hr.fer.progi.zelenitim.Raspored.dto.TaskDTO;
import hr.fer.progi.zelenitim.Raspored.obj.*;
import hr.fer.progi.zelenitim.Raspored.service.EmployeeService;
import hr.fer.progi.zelenitim.Raspored.service.GroupService;
import hr.fer.progi.zelenitim.Raspored.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final GroupService groupService;
    private final EmployeeService empService;


    @Autowired
    public TaskController(TaskService taskService, GroupService groupService, EmployeeService empService) {
        this.taskService = taskService;
        this.groupService = groupService;
        this.empService = empService;
    }

    //provjera je li taj user voditelj grupe
    @GetMapping("")
    public Set<TaskDTO> leadersAllGroupTasks(@AuthenticationPrincipal User user, @RequestBody int groupId){
        Optional<Employee> emp = empService.findByUsername(user.getUsername());
        Optional<Group> grpOpt = groupService.findById(groupId);
        if (!emp.isPresent() || !grpOpt.isPresent()) {
            return null;
        }
        if (!isLeader(user, groupId)) {
            return null;
        }
        Group g = grpOpt.get();
        Set<TaskDTO> tDTOs = new HashSet<>();
        if (grpOpt.get().getTasks().isEmpty()) {
            return null;
        }
        Set<Task> tasks = grpOpt.get().getTasks();
        for (Task t:tasks ) {
            TaskDTO task = new TaskDTO(t);
            tDTOs.add(task);
        }


        return tDTOs;
    }

    @GetMapping("/{id}")
    public TaskDTO index(@PathVariable String id,@AuthenticationPrincipal User user) {
        int idn;
        try {
            idn = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return null;
        }
        Optional<Task> tsk = taskService.findById(idn);
        if (tsk.isPresent()) {
            Optional<Employee> emp = empService.findByUsername(user.getUsername());
            int groupId = tsk.get().getGroup().getId();
            if (!emp.isPresent()) {
                return null;
            }
            if (!isLeader(user, groupId)) {
                return null;
            }
            Task task = tsk.get();
            TaskDTO tDTO = new TaskDTO(task.getDescription(),task.getId(),task.getEstimatedNumberOfHours()
                    ,task.getStartDate(),task.getEndDate(),task.getLocation());

            return tDTO;
        }
        return null;

    }

    @PostMapping("")
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskDTO ctDTO,@AuthenticationPrincipal User user) {
        int groupId = ctDTO.getGroupId();
        Optional<Group> grp = groupService.findById(groupId);
        if (!grp.isPresent()) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
        Optional<Employee> e = empService.getEmployeeById(ctDTO.getEmployeeId());
        Employee employee = e.get();
        if (!grp.get().getMembers().contains(employee)) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
        Group grupa = grp.get();
        String username = user.getUsername();
        Optional<Employee> emp = empService.findByUsername(username);
        if (emp.isEmpty()) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
        Employee mybLeader = emp.get();
        if (!mybLeader.equals(grupa.getLeader())) {
            return new ResponseEntity<Task>(HttpStatus.FORBIDDEN);
        }
        taskService.addNewTask(ctDTO.getDescription(), ctDTO.getGroupId(),ctDTO.getEmployeeId());
        return new ResponseEntity<Task>(HttpStatus.OK);
    }

    @PostMapping("/{id}/editdiscription")
    public ResponseEntity<Task> changeDescription(@RequestBody StringDTO sDTO,@PathVariable String id,@AuthenticationPrincipal User user) {
        int taskId;
        try {
            taskId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
        Optional<Task> task = taskService.findById(taskId);
        int groupId = task.get().getGroup().getId();
        if (!isLeader(user, groupId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        boolean success = taskService.changeDescription(sDTO.getText(),taskId);
        if(success)
            return new ResponseEntity<Task>(HttpStatus.OK);
        else
            return new ResponseEntity<Task>(HttpStatus.CONFLICT);

    }

    @PostMapping("/{id}/editlocation")
    public ResponseEntity<Task> changeLocation(@RequestBody Location loc, @PathVariable String id,@AuthenticationPrincipal User user) {
        int taskId;
        try {
            taskId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
        Optional<Task> task = taskService.findById(taskId);
        int groupId = task.get().getGroup().getId();
        if (!isLeader(user, groupId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        boolean success = taskService.changeLocation(taskId,loc.getAddress(),loc.getLatitude(),loc.getLongitude());
        if(success)
            return new ResponseEntity<Task>(HttpStatus.OK);
        else
            return new ResponseEntity<Task>(HttpStatus.CONFLICT);
    }

    @PostMapping("/{id}/editestimatedh")
    public ResponseEntity<Task> changeEstimatedNoHours(@RequestBody IntegerDTO iDTO, @PathVariable String id, @AuthenticationPrincipal User user) {
        int taskId;
        try {
            taskId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
        Optional<Task> task = taskService.findById(taskId);
        int groupId = task.get().getGroup().getId();
        if (!isLeader(user, groupId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        boolean success = taskService.changeEstimatedHours(taskId,iDTO.getNumber());
        if(success)
            return new ResponseEntity<Task>(HttpStatus.OK);
        else
            return new ResponseEntity<Task>(HttpStatus.CONFLICT);
    }

    @PostMapping("/{id}/editstart")
    public ResponseEntity<Task> changeStartDate(@RequestBody StringDTO sDTO, @PathVariable String id,@AuthenticationPrincipal User user) throws ParseException {
        int taskId;
        try {
            taskId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
        Optional<Task> task = taskService.findById(taskId);
        int groupId = task.get().getGroup().getId();
        if (!isLeader(user, groupId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            java.util.Date start = new SimpleDateFormat("dd/MM/yyyy").parse(sDTO.getText());
            java.sql.Date sqlstart = new java.sql.Date(start.getTime());
            boolean success = taskService.changeStartDate(taskId,sqlstart);
            if(success)
                return new ResponseEntity<Task>(HttpStatus.OK);
            else
                return new ResponseEntity<Task>(HttpStatus.CONFLICT);
        } catch (ParseException e) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/{id}/editend")
    public ResponseEntity<Task> changeEndDate(@RequestBody StringDTO endString, @PathVariable String id,@AuthenticationPrincipal User user) {
        int taskId;
        try {
            taskId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
        Optional<Task> task = taskService.findById(taskId);
        int groupId = task.get().getGroup().getId();
        if (!isLeader(user, groupId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            java.util.Date end = new SimpleDateFormat("dd/MM/yyyy").parse(endString.getText());
            java.sql.Date sqlend = new java.sql.Date(end.getTime());
            boolean success = taskService.changeEndDate(taskId,sqlend);
            if(success)
                return new ResponseEntity<Task>(HttpStatus.OK);
            else
                return new ResponseEntity<Task>(HttpStatus.CONFLICT);
        } catch (ParseException e) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/{id}/assignto")
    public ResponseEntity<Task> addAssignment(@RequestBody StringDTO sDTO, @AuthenticationPrincipal User user, @PathVariable String id) {
        int taskId;
        try {
            taskId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
        Optional<Task> task = taskService.findById(taskId);
        System.out.println("-------------");
        System.out.println(task);
        System.out.println("-------------");
        if (!task.isPresent()) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
        Group group = task.get().getGroup();
        Integer groupId = group.getId();
        if(!isLeader(user,groupId)) {
            return new ResponseEntity<Task>(HttpStatus.FORBIDDEN);
        }
        Optional<Employee> emp = empService.findByUsername(sDTO.getText());
        if (!emp.isPresent()) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
        Employee employee = emp.get();
        if (!group.getMembers().contains(employee)) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
        taskService.addAssignment(employee,task.get());
        return new ResponseEntity<Task>(HttpStatus.OK);

    }

    public boolean isLeader(User user,int groupId) {
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