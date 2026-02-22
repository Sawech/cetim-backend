package com.cetim.labs.dto.response;

import java.util.List;

public class UserInfoResponse {
	private Long id;
	private String username;
	private String SousDirection;
	private String service;
	private List<String> roles;

	public UserInfoResponse(Long id, String username, List<String> roles, String SousDirection, String service) {
		this.id = id;
		this.username = username;
		this.roles = roles;
		this.SousDirection = SousDirection;
		this.service = service;
	}

	public String getSousDirection() {
		return SousDirection;
	}

	public void setSousDirection(String SousDirection) {
		this.SousDirection = SousDirection;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getService() {
		return service;
	}
}