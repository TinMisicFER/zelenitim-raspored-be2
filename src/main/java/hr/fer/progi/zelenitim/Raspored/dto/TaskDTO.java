package hr.fer.progi.zelenitim.Raspored.dto;

import hr.fer.progi.zelenitim.Raspored.obj.Location;
import hr.fer.progi.zelenitim.Raspored.obj.Task;

import java.sql.Date;

public class TaskDTO {
    private String description;
    private Integer id;
    private Integer estimatedNumberOfHours;
    private java.sql.Date startDate;
    private java.sql.Date endDate;
    private Location location;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEstimatedNumberOfHours() {
        return estimatedNumberOfHours;
    }

    public void setEstimatedNumberOfHours(Integer estimatedNumberOfHours) {
        this.estimatedNumberOfHours = estimatedNumberOfHours;
    }

    public java.util.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TaskDTO() {}

    public TaskDTO(String description, Integer id, Integer estimatedNumberOfHours, Date startDate, Date endDate,
                   Location location) {
        this.description = description;
        this.id = id;
        this.estimatedNumberOfHours = estimatedNumberOfHours;
        this.startDate = startDate;
        this.endDate = endDate;
        //this.location = new Location((float)45.8000466,(float)15.9646868);
        this.location = location;
    }
    
    public TaskDTO(Task task) {
    	this.startDate = task.getStartDate();
    	this.endDate = task.getEndDate();
    	this.estimatedNumberOfHours = task.getEstimatedNumberOfHours();
    	this.id = task.getId();
    	this.location = task.getLocation();
    	this.description = task.getDescription();
    	}
}
