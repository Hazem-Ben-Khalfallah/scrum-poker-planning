package com.influans.sp.entity;

public class User {
    private String username;
    private String color;
    private Boolean isAdmin;

    public User() {}

    public User(String username,String color,Boolean isAdmin) {
        this.username = username;
        this.color=color;
        this.isAdmin=isAdmin;

    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	@Override
    public String toString() {
        return "";
    }

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}