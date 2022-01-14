package hr.fer.progi.zelenitim.Raspored.dto;

public class CreateAssignmentDTO {
    private Integer hours;
    private Integer employeeId;
    private Integer taskId;

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}
