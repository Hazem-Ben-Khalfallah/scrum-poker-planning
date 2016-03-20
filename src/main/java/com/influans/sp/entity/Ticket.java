package com.influans.sp.entity;

import java.util.List;

public class Ticket {

	String name;
	List<Card> chosenCards;
	
	public Ticket() {}

	public Ticket(String name, List<Card> chosenCards) {
		this.name = name;
		this.chosenCards = chosenCards;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Card> getChosenCards() {
		return chosenCards;
	}

	public void setChosenCards(List<Card> chosenCards) {
		this.chosenCards = chosenCards;
	}
	
	
}
