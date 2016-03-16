package com.influans.sp.entity;

import org.springframework.data.annotation.Id;

public class User {

    @Id
    private String id;

    private String username;
    private Boolean isAdmin;

    public User() {}

    public User(String username, Boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
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

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
    public String toString() {
        return String.format(
                "User[id=%s, username='%s', isAdmin='%s']",
                id, username, isAdmin);
    }

}