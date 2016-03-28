package com.influans.sp.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String sessionId;
    private Boolean isAdmin;
    private String color;
    
	public User() {}
	public User(String username, String sessionId, Boolean isAdmin,String color) {
		this.username = username;
		this.sessionId = sessionId;
		this.isAdmin = isAdmin;
		System.err.println(color);
		this.color = color;

	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}