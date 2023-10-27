package server;

import java.io.IOException;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;



@ServerEndpoint("/websocket")
public class ChatApp {

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
            // Elabora il messaggio del client e invia una risposta
        	String metodo = "";
        	String user="";
        	String password="";
        	
        	if(message.charAt(0)=='L') {
        		metodo="L";
        		for(int i=1;i<message.length();i++) {
        			if(message.charAt(i)=='|') {
        				user=message.substring(1,i);
        				password=message.substring(i+1);
        			}
        		}
        	}
        	
        	if(user.equals(password)) {
        		session.getBasicRemote().sendText("accettato ("+user+" "+password+")");
        	}else{
        		session.getBasicRemote().sendText("rifiutato ("+user+" "+password+")");
        	}
        	
            String responseMessage = "Server: Hai scritto - " + message;

            // Invia la risposta al client
            session.getBasicRemote().sendText(responseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
