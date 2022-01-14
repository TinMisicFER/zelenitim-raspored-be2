package hr.fer.progi.zelenitim.Raspored.obj;

import javax.persistence.Embeddable;

/**A simple GPS location implementation.
 * 
 * @author Tin Misic
 *
 */
@Embeddable
public class Location {
	
	private String address;
	private float latitude;
	private float longitude;
	
	public Location() {}
	
	public Location(String address) {
		super();
		this.address = address;
	}
	
	public Location(float latitude, float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return String.valueOf(address) + " " + String.valueOf(latitude) + " " + String.valueOf(longitude);
	}
}
