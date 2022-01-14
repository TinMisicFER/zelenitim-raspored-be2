package hr.fer.progi.zelenitim.Raspored.obj;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "EmployeeGroup")
public class Group {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	
	@ManyToOne @JoinColumn(name="LeaderID", nullable=false)
	private Employee leader;
	
	@Column(unique=true, nullable=false)
	private String name;
	
	@ManyToOne @JoinColumn(name="ActivityID", nullable=false)
	private Activity activity;
	

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Task> tasks = new HashSet<Task>();

	
	@ManyToMany
	@JoinTable(
			  name = "membership", 
			  joinColumns = @JoinColumn(name = "employee_id"), 
			  inverseJoinColumns = @JoinColumn(name = "group_id"))
	private Set<Employee> members;

	
	private boolean isActive;

	/**Creates a new group with the given leader, name and activity
	 * </br>
	 * The group is automatically active upon creation, but must manually be set to inactive once the Activity is finished
	 * 
	 * @param leader The Employee that is the leader of the group
	 * @param name The unique name of the group
	 * @param activity The activity that the group is doing
	 */
	public Group(Employee leader, String name, Activity activity) {
		if(leader==null || name==null || activity==null) {
			throw new NullPointerException();
		}
		this.leader = leader;
		this.members = new HashSet<Employee>();
		this.members.add(this.leader);
		this.name = name;
		this.activity = activity;
		this.isActive = true;
	}
	
	public Group() {}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Employee getLeader() {
		return leader;
	}

	public void setLeader(Employee leader) {
		this.leader = leader;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return name;
	}

	public Activity getActivity() {
		return activity;
	}
	
	public Set<Employee> getMembers(){
		return this.members;
	}
	
	public void setMembers(Set<Employee> members) {
		this.members = members;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
}
