package hr.fer.progi.zelenitim.Raspored.dto;

import hr.fer.progi.zelenitim.Raspored.obj.Assignment;
import hr.fer.progi.zelenitim.Raspored.obj.Group;
import hr.fer.progi.zelenitim.Raspored.obj.Location;

import java.sql.Date;

public class AssignmentDTO {
    private Integer id;
    private Integer hoursWorked;
    private String taskDis;
    private String taskAdress;
    private float taskLatitude;
    private float taskLongitude;
    private Integer empId;
    private String groupName;
    private Integer taskEstimatedNumberOfHours;
    private java.sql.Date taskStartDate;
    private java.sql.Date taskEndDate;
    private Integer taskId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskAdress() {
        return taskAdress;
    }

    public void setTaskAdress(String taskAdress) {
        this.taskAdress = taskAdress;
    }

    public float getTaskLatitude() {
        return taskLatitude;
    }

    public void setTaskLatitude(float taskLatitude) {
        this.taskLatitude = taskLatitude;
    }

    public float getTaskLongitude() {
        return taskLongitude;
    }

    public void setTaskLongitude(float taskLongitude) {
        this.taskLongitude = taskLongitude;
    }

    public Integer getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(Integer hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public String getTaskDis() {
        return taskDis;
    }

    public void setTaskDis(String taskDis) {
        this.taskDis = taskDis;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getEmpId() {
        return empId;
    }



    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public Integer getTaskEstimatedNumberOfHours() {
        return taskEstimatedNumberOfHours;
    }

    public void setTaskEstimatedNumberOfHours(Integer taskEstimatedNumberOfHours) {
        this.taskEstimatedNumberOfHours = taskEstimatedNumberOfHours;
    }

    public Date getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(Date taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public Date getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(Date taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public AssignmentDTO(Integer id, Integer hoursWorked, String taskDis, Integer empId, String groupName) {
        this.id = id;
        this.hoursWorked = hoursWorked;
        this.taskDis = taskDis;
        this.empId = empId;
        this.groupName = groupName;
    }

    public AssignmentDTO(Integer id, Integer hoursWorked, String taskDis, Integer empId, String groupName, Integer taskEstimatedNumberOfHours, Date taskStartDate, Date taskEndDate, Integer taskId,
                         String taskAdress, float taskLatitude,float taskLongitude) {
        this.id = id;
        this.hoursWorked = hoursWorked;
        this.taskDis = taskDis;
        this.empId = empId;
        this.groupName = groupName;
        this.taskEstimatedNumberOfHours = taskEstimatedNumberOfHours;
        this.taskStartDate = taskStartDate;
        this.taskEndDate = taskEndDate;
        this.taskId = taskId;
        this.taskAdress = taskAdress;
        this.taskLatitude = taskLatitude;
        this.taskLongitude = taskLongitude;
    }

    public AssignmentDTO(Assignment a) {
        this.id = a.getId();
        this.hoursWorked = a.getHoursWorked();
        this.taskDis = a.getTask().getDescription();
        this.empId = a.getEmployee().getId();
        this.groupName = a.getTask().getGroup().getName();
        this.taskEstimatedNumberOfHours = a.getTask().getEstimatedNumberOfHours();
        this.taskStartDate = a.getTask().getStartDate();
        this.taskEndDate = a.getTask().getEndDate();
        this.taskId = a.getTask().getId();
        this.taskAdress = a.getTask().getLocation().getAddress();
        this.taskLatitude = a.getTask().getLocation().getLatitude();
        this.taskLongitude = a.getTask().getLocation().getLongitude();
    }

    public AssignmentDTO () {}
}
