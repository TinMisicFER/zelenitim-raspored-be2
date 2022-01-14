package hr.fer.progi.zelenitim.Raspored.obj;

import javax.persistence.*;

@Entity
@Table(name = "Assignment")
public class Assignment {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "EmployeeID")
    Employee employee;

    public Assignment(Employee employee, Task task, Integer hoursWorked) {
        this.employee = employee;
        this.task = task;
        this.hoursWorked = hoursWorked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Integer getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(Integer hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public Assignment() {}

    @ManyToOne
    @JoinColumn(name = "TaskID")
    Task task;

    private Integer hoursWorked;
}
