package hr.fer.progi.zelenitim.Raspored.dto;

public class UserDTO {
	private String username;
	private boolean isAllowed;
	
	public UserDTO() {}
	
	public UserDTO(String username,boolean isAllowed) {
		this.username = username;
		this.isAllowed = isAllowed;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isAllowed() {
		return isAllowed;
	}

	public void setAllowed(boolean isAllowed) {
		this.isAllowed = isAllowed;
	}
	
	
}
