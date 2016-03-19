package com.influans.sp.entity;

import java.util.List;

public class Data {

    private String id;
    private User user;
    private String sessionId;
    private List<Card> chosenCard;
    private List<Card> myCards;

    public Data() {}

	public Data(User user, String sessionId, List<Card> chosenCard, List<Card> myCards) {
		this.user = user;
		this.sessionId = sessionId;
		this.chosenCard = chosenCard;
		this.myCards = myCards;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public List<Card> getChosenCard() {
		return chosenCard;
	}

	public void setChosenCard(List<Card> chosenCard) {
		this.chosenCard = chosenCard;
	}

	public List<Card> getMyCards() {
		return myCards;
	}

	public void setMyCards(List<Card> myCards) {
		this.myCards = myCards;
	}

	
    
}