package com.influans.sp.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.influans.sp.entity.Card;
import com.influans.sp.entity.Session;
import com.influans.sp.entity.Ticket;
import com.influans.sp.entity.User;
import com.influans.sp.repository.SessionRepository;
import com.influans.sp.repository.UserRepository;
import com.influans.sp.service.Utils;

@Controller
public class HomeController {

	@Autowired
	UserRepository userRepo;
	@Autowired
	SessionRepository sessionRepo;
	
	@MessageMapping("/connect")
    @SendTo("/topic/connect")
    public ResponseEntity<User> connect(String data) throws Exception {
		JSONObject obj = new JSONObject(data);
    	String username = (String) obj.get("username");
    	String sessionId = (String) obj.get("sessionId");
    	Session session = sessionRepo.findSessionBySessionId(sessionId);
    	List<Ticket> tickets = new ArrayList<Ticket>();

    	User new_user=null;
    	if(session!=null){
    		tickets=session.getTickets();
    		List<User> users = userRepo.findUsersBySessionId(sessionId);
    		Boolean userExist=false;
        	for(User user:users){
        		if(user.getUsername().equals(username)){
        			userExist=true;
        			break;
        		}
        	}
        	if(userExist==false){
        		new_user=new User(username,sessionId,false,Utils.getRandomColor());
        	}/*else{
            	return new ResponseEntity<User>(new_user,HttpStatus.IM_USED);
        	}*/
    	}else{
    		new_user=new User(username,sessionId,true,Utils.getRandomColor());
    	}
    	sessionRepo.save(new Session(sessionId,tickets));
    	userRepo.save(new_user);
    	return new ResponseEntity<User>(new_user,HttpStatus.OK);
    }
		
    @MessageMapping("/load_data")
    @SendTo("/topic/load_data")
    public  ResponseEntity<Session> load_data(String data) throws Exception {
    	JSONObject obj = new JSONObject(data);
    	String sessionId = (String) obj.get("sessionId");    	
    	Session session=sessionRepo.findSessionBySessionId(sessionId);    	
    	return new ResponseEntity<Session>(session,HttpStatus.OK);
    }
	
    @MessageMapping("/create_ticket")
    @SendTo("/topic/load_data")
    public ResponseEntity<Session> create_ticket(String data) throws Exception {
    	JSONObject obj = new JSONObject(data);
    	String ticket_name = (String) obj.get("ticketName");
    	String sessionId = (String) obj.get("sessionId");
    	Session session = sessionRepo.findSessionBySessionId(sessionId);
    	for(Ticket ticket:session.getTickets()){
    		if(ticket.getTicketName().equals(ticket_name)){
    			return new ResponseEntity<Session>(session,HttpStatus.IM_USED);
    		}
    	}
    	Ticket new_ticket = new Ticket(ticket_name);
    	session.getTickets().add(new_ticket);
    	sessionRepo.save(session);
    	return new ResponseEntity<Session>(session,HttpStatus.OK);
    }
    
    @MessageMapping("/add_card")
    @SendTo("/topic/load_data")
    public  ResponseEntity<Session> add_card(String data) throws Exception {
    	JSONObject obj = new JSONObject(data);
    	String sessionId = (String) obj.get("sessionId");    	
    	String ticketName = (String) obj.get("ticketName");
    	String username = (String) obj.get("username");    
    	String color = (String) obj.get("color");    
    	int idCard = (int) obj.get("idCard");
    	Session session=sessionRepo.findSessionBySessionId(sessionId);
    	Card card=null;
    	Boolean exist=false;
    	for(Ticket t:session.getTickets()){
    		if(t.getTicketName().equals(ticketName)){
				System.err.println(t.getTicketName().equals(ticketName));
    			for (Card c : t.getCards()) {
    				System.err.println(c.getIdCard()+"-"+idCard);
    				System.err.println(c.getUsername()+"-"+username);
					if(c.getUsername().equals(username)){
						exist=true;
						break;
					}
				}
    			if(exist==false){
    				card=new Card(idCard,color,username);
    				t.getCards().add(card);
    			}
    			break;
    		}
    	}
    	sessionRepo.save(session);
    	if(!exist){
    		return new ResponseEntity<Session>(session,HttpStatus.OK);
    	}else{
    		return new ResponseEntity<Session>(session,HttpStatus.IM_USED);
    	}
    }
    
    @MessageMapping("/remove_card")
    @SendTo("/topic/load_data")
    public  ResponseEntity<Session> remove_card(String data) throws Exception {
    	JSONObject obj = new JSONObject(data);
    	String sessionId = (String) obj.get("sessionId");    	
    	String ticketName = (String) obj.get("ticketName");
    	String username = (String) obj.get("username");    	
    	int idCard = (int) obj.get("idCard");
    	System.err.println(sessionId);
    	Session session=sessionRepo.findSessionBySessionId(sessionId);
    	Card card=null;
    	Boolean mine=false;
    	for(Ticket t:session.getTickets()){
    		if(t.getTicketName().equals(ticketName)){
				System.err.println(t.getTicketName().equals(ticketName));
    			for (Card c : t.getCards()) {
    				System.err.println(c.getIdCard()+"-"+idCard);
    				System.err.println(c.getUsername()+"-"+username);
					if(c.getIdCard()==idCard && c.getUsername().equals(username)){
						card=c;
						mine=true;
						break;
					}
				}
    			t.getCards().remove(card);
    			break;
    		}
    	}
    	sessionRepo.save(session);
    	System.err.println("remove:"+mine);
    	if(mine==false){
        	return new ResponseEntity<Session>(session,HttpStatus.IM_USED);
    	}else{
        	return new ResponseEntity<Session>(session,HttpStatus.OK);
    	}
    }
        
}