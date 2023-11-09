package server;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;


@ServerEndpoint("/websocket")
public class ChatApp {
	boolean utenteApprovato=false;
	static ArrayList<Utente> utenti = new ArrayList<>();
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
            		session.getBasicRemote().sendText("Accetato");
            		Utente us = new Utente(session,user);
            		us.setApprovato(true);
                    utenti.add(us);
                    inviaUsers();
            	}else{
            		utenteApprovato=false;
            		session.getBasicRemote().sendText("Rifiutato");
            	}
        	}
        	
        	
        	if(message.charAt(0)=='M') {
        		metodo="M";
        		session.getBasicRemote().sendText("mess inviato");
        		if(utenteApprovato) {
        			for (Utente clientSession : utenti) {
                        if (clientSession.getSession().isOpen() && !clientSession.getSession().equals(session)) {
                            try {
                                clientSession.getSession().getBasicRemote().sendText(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
        		}else{
            		session.getBasicRemote().sendText("Fai prima l'accesso!");
            	}
        		
        	}
        	
        	
        	
            //String responseMessage = "Server: Hai scritto - " + message;

            // Invia la risposta al client
            //session.getBasicRemote().sendText(responseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void inviaUsers() {
    	String risposta="E";
    	for (Utente us : utenti) {
    		risposta+=us.nome+"|";
    	}
    	//risposta=risposta.substring(0, risposta.length()-1);
    	
    	for (Utente clientSession : utenti) {
    		try {
                clientSession.getSession().getBasicRemote().sendText(risposta);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
