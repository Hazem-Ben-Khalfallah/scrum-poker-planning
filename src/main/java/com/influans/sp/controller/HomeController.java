package com.influans.sp.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.influans.sp.entity.Card;
import com.influans.sp.entity.Connexion;
import com.influans.sp.entity.Session;
import com.influans.sp.entity.Ticket;
import com.influans.sp.entity.User;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.service.Utils;

@Controller
public class HomeController {

	@Autowired
	SessionRepository sessionRepo;
	
    @MessageMapping("/new_user")
    @SendTo("/topic/new_user")
    public ResponseEntity<Session> new_user(Connexion cnx) throws Exception {
    	Session session = sessionRepo.findBySessionId(cnx.getSessionId());
    	if(session!=null){
    		Boolean userExist=false;
    		List<User> users =  session.getUsers();
        	for(User user:users){
        		if(user.getUsername().equals(cnx.getUsername())){
        			userExist=true;
        			break;
        		}
        	}
        	if(!userExist){
        		session.getUsers().add(new User(cnx.getUsername(),Utils.getRandomColor(),false));
        	}
        	
    	}else{
    		List<User> users = new ArrayList<User>();
    		users.add(new User(cnx.getUsername(),Utils.getRandomColor(),true));
    		session = new Session(cnx.getSessionId(), new ArrayList<Ticket>(),users);
    	}
    	sessionRepo.save(session);
    	return new ResponseEntity<Session>(session,HttpStatus.OK);
    }
    
    @MessageMapping("/new_ticket")
    @SendTo("/topic/new_ticket")
    public ResponseEntity<Session> new_ticket(String data) throws Exception {
    	JSONObject obj = new JSONObject(data);
    	String ticket_name = (String) obj.get("name");
    	String sessionId = (String) obj.get("sessionId");
    	Session session = sessionRepo.findBySessionId(sessionId);
    	for(Ticket ticket:session.getTickets()){
    		if(ticket.getName().equals(ticket_name)){
    			return new ResponseEntity<Session>(session,HttpStatus.IM_USED);
    		}
    	}
    	List<Card> cards = new ArrayList<Card>();
    	session.getTickets().add(new Ticket(ticket_name, cards));
    	sessionRepo.save(session);
    	return new ResponseEntity<Session>(session,HttpStatus.OK);
    }
    
}