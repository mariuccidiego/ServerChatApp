package server;

import java.io.IOException;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;



@ServerEndpoint("/websocket")
public class ChatApp {
	Boolean utenteApprovato=false;
    @OnOpen
    public void onOpen(Session session) {
        // Questo metodo gestisce la richiesta di apertura della connessione WebSocket
        System.out.println("WebSocket opened");

        // Invia un messaggio di benvenuto al client
        try {
            session.getBasicRemote().sendText("Ciao");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Questo metodo gestisce i messaggi inviati dai client
        try {
            //TODO rendere le variabili globali
        	//LOGIN
        	String metodo = "";
        	String user="";
        	String password="";
        	//Boolean utenteApprovato=false;
        	
        	if(message.charAt(0)=='L') {
        		metodo="L";
        		for(int i=1;i<message.length();i++) {
        			if(message.charAt(i)=='|') {
        				user=message.substring(1,i);
        				password=message.substring(i+1);
        			}
        		}
        		if(user.equals(password)) {
            		utenteApprovato=true;
            		session.getBasicRemote().sendText("accettato ("+user+" "+password+")");
            	}else{
            		utenteApprovato=false;
            		session.getBasicRemote().sendText("rifiutato ("+user+" "+password+")");
            	}
        	}
        	
        	//MESSAGE
        	String nome="";
        	String id="";
        	String messaggio="";	
        	
        	if(message.charAt(0)=='M') {
        		metodo="M";
        		session.getBasicRemote().sendText("M");
        		for(int i=1;i<message.length();i++) {
        			if(message.charAt(i)=='|') {
        				nome=message.substring(1,i);
        				session.getBasicRemote().sendText("nome="+nome);
        				for(int j=i+1;j<message.length();j++) {
        					if(message.charAt(j)=='|') {
        						id=message.substring(i+1,j);
        						session.getBasicRemote().sendText("id="+id);
        						messaggio=message.substring(j+1);
        						session.getBasicRemote().sendText("messagio="+message);
        					}
        				}
        				i=message.length();
        			}
        		}
        		if(utenteApprovato) {
            		session.getBasicRemote().sendText(nome+"("+id+") = "+messaggio);
            		for (Session clientSession : session.getOpenSessions()) {
                        if (clientSession.isOpen()) {
                            clientSession.getBasicRemote().sendText(messaggio);
                        }
                    }
            	}else{
            		session.getBasicRemote().sendText("Fai prima l'accesso!");
            	}
        	}
        	
        	
        	
            String responseMessage = "Server: Hai scritto - " + message;

            // Invia la risposta al client
            session.getBasicRemote().sendText(responseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
