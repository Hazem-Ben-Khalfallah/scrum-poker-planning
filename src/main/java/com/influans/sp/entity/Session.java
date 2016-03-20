package com.influans.sp.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;

public class Session {
	@Id
    private String sessionId;
    private List<Ticket> tickets;
    private List<User> users;

    public Session() {}

	public Session(String sessionId, List<Ticket> tickets,List<User> users) {
		this.sessionId = sessionId;
		this.tickets = tickets;
		this.users = users;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
    
}