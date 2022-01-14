package hr.fer.progi.zelenitim.Raspored.obj;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "activity")
public class Activity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	@Column(unique=true, nullable=false)
	private String descriptionOfActivity;
	
	@Column(nullable=false)
	private double pricePerHour;
	
	
	@OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
	private Set<Group> groups;
	
	public Activity() {}
	
	public Activity(String description, double pricePerHour) {
		this.descriptionOfActivity = description;
		this.pricePerHour = pricePerHour;
	}
	
	public String getDescriptionOfActivity() {
		return descriptionOfActivity;
	}

	public void setDescriptionOfActivity(String descriptionOfActivity) {
		this.descriptionOfActivity = descriptionOfActivity;
	}

	public double getPricePerHour() {
		return pricePerHour;
	}

	public void setPricePerHour(double pricePerHour) {
		this.pricePerHour = pricePerHour;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
