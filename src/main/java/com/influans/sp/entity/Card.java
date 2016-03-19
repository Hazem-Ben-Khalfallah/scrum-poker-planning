package com.influans.sp.entity;

public class Card {
	   private String id;
	   private String idCard;
	    private String color;
	    private Boolean isMine;
	    public Card() {}
	    
		public Card(String idCard, String color) {
			this.idCard = idCard;
			this.color = color;
			this.isMine=false;
		}
		public Card(String idCard, String color,boolean isMine) {
			this.idCard = idCard;
			this.color = color;
			this.isMine=isMine;
		}


		public String getIdCard() {
			return idCard;
		}

		public void setIdCard(String idCard) {
			this.idCard = idCard;
		}



		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

		public Boolean getIsMine() {
			return isMine;
		}

		public void setIsMine(Boolean isMine) {
			this.isMine = isMine;
		}
}
