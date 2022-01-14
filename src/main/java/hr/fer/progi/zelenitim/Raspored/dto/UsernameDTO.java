package hr.fer.progi.zelenitim.Raspored.dto;

public class UsernameDTO {
	private String newEmployeeUsername;
	
	public UsernameDTO() {}
	
	public UsernameDTO(String newEmployeeUsername) {
		this.newEmployeeUsername = newEmployeeUsername;
	}

	public String getNewEmployeeUsername() {
		return newEmployeeUsername;
	}

	public void setNewEmployeeUsername(String newEmployeeUsername) {
		this.newEmployeeUsername = newEmployeeUsername;
	}
	
}
