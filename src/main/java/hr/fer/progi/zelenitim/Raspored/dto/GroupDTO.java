package hr.fer.progi.zelenitim.Raspored.dto;

import hr.fer.progi.zelenitim.Raspored.obj.Group;

/**A class for <b>returning</b> the important information of a Group
 * 
 * @author Tin Misic
 *
 */
public class GroupDTO {
	private String name;
	private Integer id;
	private boolean isActive;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public GroupDTO() {}
	
	public GroupDTO(Integer id,String name,boolean isActive) {
		this.id = id;
		this.name = name;
		this.isActive = isActive;
	}
	
	public GroupDTO(Group group) {
		this.id = group.getId();
		this.name = group.getName();
		this.isActive = group.isActive();
	}
}
