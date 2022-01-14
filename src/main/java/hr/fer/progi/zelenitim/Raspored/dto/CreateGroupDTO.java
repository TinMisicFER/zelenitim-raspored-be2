package hr.fer.progi.zelenitim.Raspored.dto;

public class CreateGroupDTO {

	private String leaderUsername;
	private String name;
	private Integer activityId;
	
	public CreateGroupDTO() {
		System.out.println("Creating new group...");
	}
	
	public String getLeaderUsername() {
		return leaderUsername;
	}
	
	public void setLeaderUsername(String leaderUsername) {
		this.leaderUsername = leaderUsername;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getActivityId() {
		return activityId;
	}
	
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	
	
}
