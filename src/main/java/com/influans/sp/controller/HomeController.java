package com.influans.sp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.influans.sp.entity.Card;
import com.influans.sp.entity.Data;
import com.influans.sp.entity.User;



@Controller
public class HomeController {
	
    @MessageMapping("/new_user")
    @SendTo("/topic/new_user")
    public ResponseEntity<Data> new_user(User user,String sessionId) throws Exception {
    	List<Card> chosenCards = new ArrayList<Card>();
    	chosenCards.add(new Card("2", "red"));
    	chosenCards.add(new Card("2", "black"));
    	chosenCards.add(new Card("3", "green",true));
    	List<Card> myCards = new ArrayList<Card>();
    	myCards.add(new Card("1", "green",true));
    	myCards.add(new Card("2", "green",true));
    	//myCards.add(new Card("3", "green",true));
    	myCards.add(new Card("4", "green",true));
    	myCards.add(new Card("5", "green",true));
    	myCards.add(new Card("6", "green",true));
    	myCards.add(new Card("7", "green",true));
    	myCards.add(new Card("8", "green",true));
    	myCards.add(new Card("9", "green",true));
    	myCards.add(new Card("10", "green",true));
    	myCards.add(new Card("11", "green",true));
    	myCards.add(new Card("12", "green",true));
    	Data data = new Data(user, sessionId,chosenCards,myCards);
    	return new ResponseEntity<Data>(data,HttpStatus.OK);
    }
    
}