package hr.fer.progi.zelenitim.Raspored.rest;

import hr.fer.progi.zelenitim.Raspored.dto.*;
import hr.fer.progi.zelenitim.Raspored.obj.Assignment;
import hr.fer.progi.zelenitim.Raspored.obj.Employee;
import hr.fer.progi.zelenitim.Raspored.obj.Group;
import hr.fer.progi.zelenitim.Raspored.obj.Task;
import hr.fer.progi.zelenitim.Raspored.service.AssignmentService;
import hr.fer.progi.zelenitim.Raspored.service.EmployeeService;
import hr.fer.progi.zelenitim.Raspored.service.GroupService;
import hr.fer.progi.zelenitim.Raspored.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/assignment")
public class AssignmentController {
    private final AssignmentService assignmentService;
    private final GroupService groupService;
    private final EmployeeService empService;
    private final TaskService taskService;


    @Autowired
    public AssignmentController(AssignmentService assignmentService, GroupService groupService, EmployeeService empService, TaskService taskService) {
        this.assignmentService = assignmentService;
        this.groupService = groupService;
        this.empService = empService;
        this.taskService = taskService;
    }
    @GetMapping("")
    public Set<AssignmentDTO> usersAssignments(@AuthenticationPrincipal User user){
        Set<Assignment> assigns;
        Optional<Employee> emp = empService.findByUsername(user.getUsername());
        assigns = emp.get().getAssignments();

        Set<AssignmentDTO> aDTOs = new HashSet<>();
        for(Assignment a:assigns) {
            AssignmentDTO adto = new AssignmentDTO(a);
            aDTOs.add(adto);
        }

        return aDTOs;
    }

    @GetMapping("/{id}")
    public AssignmentDTO index(@PathVariable String id) {
        int idn;
        try {
            idn = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return null;
        }
        Optional<Assignment> assign =  assignmentService.findById(idn);
        if(assign.isPresent()) {
            Assignment assignment =  assign.get();
            AssignmentDTO aDTO = new AssignmentDTO(assignment);

            return aDTO;
        }
        return null;
    }



    @PostMapping("/{id}/addHours")
    public ResponseEntity<Assignment> addWorkHours(@RequestBody IntegerDTO hours, @PathVariable String id) {
        int assignId;
        try {
            assignId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return new ResponseEntity<Assignment>(HttpStatus.BAD_REQUEST);
        }

        if (hours.getNumber() > 8) {
            return new ResponseEntity<Assignment>(HttpStatus.BAD_REQUEST);
        }
        boolean success = assignmentService.addHours(assignId,hours.getNumber());
        if (!success) {
            return new ResponseEntity<Assignment>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Assignment>(HttpStatus.OK);
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