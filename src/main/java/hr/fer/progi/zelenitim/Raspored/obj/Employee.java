package hr.fer.progi.zelenitim.Raspored.obj;

import java.util.Set;

import javax.persistence.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "employee")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String lastName;
	
	@Column(unique=true, nullable = false)
	private String email;
	
	@Column(unique=true, nullable = false)
	private String OIB;
	
	@Column(nullable = false)
	private String password;
	
	@Column(unique=true, nullable = false)
	private String username;
	
	@ManyToMany(mappedBy = "members")
	private Set<Group> groupsIAmAMemberOf;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	Set<Assignment> assignments;
	
	@OneToMany(mappedBy = "leader", cascade = CascadeType.ALL)
	private Set<Group> leaderOf;
	
	public Employee() {}
		
	public Set<Group> getGroupsIAmAMemberOf() {
		return groupsIAmAMemberOf;
	}

	public Set<Group> getLeaderOf() {
		return leaderOf;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOIB() {
		return OIB;
	}

	public void setOIB(String oIB) {
		OIB = oIB;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = new BCryptPasswordEncoder().encode(password);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

	public Set<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(Set<Assignment> assignments) {
		this.assignments = assignments;
	}

	public void setGroupsIAmAMemberOf(Set<Group> groupsIAmAMemberOf) {
		this.groupsIAmAMemberOf = groupsIAmAMemberOf;
	}

	public void setLeaderOf(Set<Group> leaderOf) {
		this.leaderOf = leaderOf;
	}

	public Employee(String name, String lastName, String email, 
			String OIB, String username, String password) {
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.OIB = OIB;
		this.username = username;
		this.password = new BCryptPasswordEncoder().encode(password);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		Employee other = (Employee) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	
}
