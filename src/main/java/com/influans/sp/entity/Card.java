package com.influans.sp.entity;


public class Card {
	    private String idCard;
	    private User user;
	    public Card() {}
	    
		public Card(String idCard,User user) {
			this.idCard = idCard;
		}

		public String getIdCard() {
			return idCard;
		}

		public void setIdCard(String idCard) {
			this.idCard = idCard;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}
}
