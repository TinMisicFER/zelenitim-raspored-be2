package hr.fer.progi.zelenitim.Raspored.obj;

import java.sql.Date;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "Task")
public class Task {

	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	private String description;
	private Integer estimatedNumberOfHours;
	
	private java.sql.Date startDate;
	private java.sql.Date endDate;
	
	@Embedded
	private Location location;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@ManyToOne
	@JoinColumn(name = "groupID", nullable = false)
	private Group group;

	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
	Set<Assignment> assignments;
	
	public Task() {}

	public Task(String description, Integer estimatedNumberOfHours, Group group, Date startDate, Date endDate) {
		if(description==null || group==null)
			throw new NullPointerException();
		this.description = description;
		this.estimatedNumberOfHours = estimatedNumberOfHours;
		this.group = group;
		this.startDate = startDate;
		this.endDate = endDate;
		this.location = new Location((float)45.8000466,(float)15.9646868);
	}

	public Task(String description, Group group, Location location) {
		this.description = description;
		this.group = group;
		//this.location = new Location((float)45.8000466,(float)15.9646868);
		this.location = location;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Date getStartDate() {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getEstimatedNumberOfHours() {
		return estimatedNumberOfHours;
	}

	public void setEstimatedNumberOfHours(Integer estimatedNumberOfHours) {
		this.estimatedNumberOfHours = estimatedNumberOfHours;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
	public Set<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(Set<Assignment> assignments) {
		this.assignments = assignments;
	}

}
