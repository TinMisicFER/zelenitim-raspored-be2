package hr.fer.progi.zelenitim.Raspored.dto;


import hr.fer.progi.zelenitim.Raspored.obj.Activity;


public class ActivityDTO {

	private int id;	
	private String descriptionOfActivity;
	private double pricePerHour;
	
	
	public ActivityDTO(int id, String descriptionOfActivity, double pricePerHour) {
		this.id = id;
		this.descriptionOfActivity = descriptionOfActivity;
		this.pricePerHour = pricePerHour;
	}
	
	public ActivityDTO(Activity activity) {
		this.id = activity.getId();
		this.descriptionOfActivity = activity.getDescriptionOfActivity();
		this.pricePerHour = activity.getPricePerHour();
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
