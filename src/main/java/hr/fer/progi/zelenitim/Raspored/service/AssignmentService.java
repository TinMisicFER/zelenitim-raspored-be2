package hr.fer.progi.zelenitim.Raspored.service;

import hr.fer.progi.zelenitim.Raspored.obj.Assignment;
import hr.fer.progi.zelenitim.Raspored.obj.Employee;
import hr.fer.progi.zelenitim.Raspored.obj.Group;
import hr.fer.progi.zelenitim.Raspored.obj.Task;
import hr.fer.progi.zelenitim.Raspored.repository.AssignmentRepository;
import hr.fer.progi.zelenitim.Raspored.repository.EmployeeRepository;
import hr.fer.progi.zelenitim.Raspored.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private EmployeeRepository empRepo;

    @Autowired
    private TaskRepository taskRepo;

    public List<Assignment> listAll() {return assignmentRepository.findAll();}

    public Optional<Assignment> findById(int assignId) {
        return assignmentRepository.findById(assignId);
    }

    public void addAssignment(Assignment assignment) { assignmentRepository.save(assignment);}

    public boolean addHours(int assignId, int hours) {
        Optional<Assignment> assignOpt = assignmentRepository.findById(assignId);
        if(assignOpt.isEmpty()){
            return false;
        }
        Assignment assign = assignOpt.get();
        int newHours = assign.getHoursWorked() + hours;
        assign.setHoursWorked(newHours);
        assignmentRepository.save(assign);
        return true;

    }


}
