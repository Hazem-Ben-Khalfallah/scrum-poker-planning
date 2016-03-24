package com.influans.sp.entity;


public class Card {
	    private int idCard;
	    private User user;
	    public Card() {}
	    
		public Card(int idCard,User user) {
			this.idCard = idCard;
			this.user=user;
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

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}
}
