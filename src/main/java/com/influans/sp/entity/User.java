package com.influans.sp.entity;


public class User {

    private String id;

    private String username;

    public User() {}

    public User(String username) {
        this.username = username;
    }

    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

}