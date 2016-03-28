package com.influans.sp.entity;


public class Card {
	    private int idCard;
	    private String color;
	    private String username;
	    public Card() {}
	    
		public Card(int idCard,String color,String username) {
			this.idCard = idCard;
			this.color=color;
			this.username=username;
		}
		
		public Card(int idCard,String username) {
			this.idCard = idCard;
			this.username=username;
		}
		
		public Card(int idCard) {
			this.idCard = idCard;
		}
		public int getIdCard() {
			return idCard;
		}

		public void setIdCard(int idCard) {
			this.idCard = idCard;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}		
}
