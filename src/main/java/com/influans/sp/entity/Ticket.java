package com.influans.sp.entity;

import java.util.ArrayList;
import java.util.List;

public class Ticket {
	private String ticketName;
	private List<Card> cards;

	public Ticket() {}

	public Ticket(String ticketName) {
		this.ticketName = ticketName;
		this.cards = new ArrayList<Card>();
		
	}
	
	public Ticket(String ticketName, List<Card> cards) {
		this.ticketName = ticketName;
		this.cards = cards;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public String getTicketName() {
		return ticketName;
	}

	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}
	
}
