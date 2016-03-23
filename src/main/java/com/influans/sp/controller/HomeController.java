package com.influans.sp.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@MessageMapping("/connect")
    @SendTo("/topic/connect")
    public ResponseEntity<User> connect(Connexion cnx) throws Exception {
    	Session session = sessionRepo.findBySessionId(cnx.getSessionId());
    	User new_user=null;
    	if(session!=null){
    		Boolean userExist=false;
    		List<User> users =  session.getUsers();
        	for(User user:users){
        		if(user.getUsername().equals(cnx.getUsername())){
        			userExist=true;
        			break;
        		}
        	}
        	if(userExist==false){
        		new_user=new User(cnx.getUsername(),Utils.getRandomColor(),false);
        		session.getUsers().add(new_user);
        	}else{
            	return new ResponseEntity<User>(new_user,HttpStatus.IM_USED);
        	}
        	
    	}else{
    		List<User> users = new ArrayList<User>();
    		users.add(new User(cnx.getUsername(),Utils.getRandomColor(),true));
    		session = new Session(cnx.getSessionId(), new ArrayList<Ticket>(),users);
    		new_user=new User(cnx.getUsername(),Utils.getRandomColor(),true);
    	}
    	sessionRepo.save(session);
    	return new ResponseEntity<User>(new_user,HttpStatus.OK);
    }
	
    @MessageMapping("/load_data")
    @SendTo("/topic/load_data")
    public ResponseEntity<Session> new_user(Connexion cnx) throws Exception {
    	Session session = sessionRepo.findBySessionId(cnx.getSessionId());
    	return new ResponseEntity<Session>(session,HttpStatus.OK);
    }
    
    @MessageMapping("/create_ticket")
    @SendTo("/topic/create_ticket")
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