package hr.fer.progi.zelenitim.Raspored.obj;

import java.io.Serializable;

public class JwtResponse implements Serializable
{
	
	private final String jwttoken;

	public JwtResponse(String jwttoken) {
		this.jwttoken = jwttoken;
	}

	public String getToken() {
		return this.jwttoken;
	}
}
