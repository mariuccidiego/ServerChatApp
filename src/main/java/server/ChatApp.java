/*
 * ChatApp - Applicazione di chat basata su WebSocket
 * 
 * Protocollo di Comunicazione:
 * - Login: Invia il messaggio seguente per effettuare il login: Lnome|password
 *   - Risposta login accettato: Accettato
 *   - Risposta login rifiutato: Rifiutato
 * 
 * - Messaggio: Invia il messaggio seguente per inviare un messaggio: Mnome|id|messaggio
 * 
 * - Utenti Connessi: Ricevi il messaggio seguente per ottenere la lista degli utenti connessi: Euname1|uname2|...|unameN
 * 
 * @author Diego Magiucci - Chiara Rosi
 * @version 1.0
 * @since 16-11-2023
 */

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
	// Contiene tutti gli utenti collegati alla chat
	static ArrayList<Utente> utenti = new ArrayList<>();
    
	// Gestisce la connessione con i client
	@OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket opened");
        
        try {
            session.getBasicRemote().sendText("Ciao");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        
        try {
        	// LOGIN
        	String user="-";
        	String password="";
        	
        	if(message.charAt(0)=='L') {
        		
        		// Divide username e password 
        		for(int i=1;i<message.length();i++) {
        			if(message.charAt(i)=='|') {
        				user=message.substring(1,i);
        				password=message.substring(i+1);
        			}
        		}
        		
        		if(user.equals(password)) {
            		utenteApprovato=true;
            		session.getBasicRemote().sendText("Accetato");
            		// Aggiunge l'utente alla lista dei collegati e loggati
            		Utente us = new Utente(session,user);
            		us.setApprovato(true);
                    utenti.add(us);
                    inviaUsers();
            	}else{
            		utenteApprovato=false;
            		session.getBasicRemote().sendText("Rifiutato");
            	}
        	}
        	
        	// RICEZIONE MESSAGGIO
        	if(message.charAt(0)=='M') {
        		if(utenteApprovato) {
        			// Invia a tutti gli utenti collegati al server e loggati il messaggio, tranne a chi lo ha inviato
        			for (Utente sessione : utenti) {
                        if (sessione.getSession().isOpen() && !sessione.getSession().equals(session)) {
                            try {
                                sessione.getSession().getBasicRemote().sendText(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
        		}else{
        			//TODO in caso l'utente non abbia fatto l'accesso prima
            		//ESEMPIO: session.getBasicRemote().sendText("Fai prima l'accesso!");
            	}
        		
        	}
        	
        	
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Funzione per inviare a tutti gli utenti loggati la lista di tutti gli utenti loggati
    public void inviaUsers() {
    	
    	// Compone la stringa
    	String risposta="E";
    	// Rimuove gli utenti che erano loggati ma non sono più connessi
    	utenti.removeIf(u -> !u.getSession().isOpen());
    	for (Utente us : utenti) {
    		risposta+=us.nome+"|";
    	}
    	risposta=risposta.substring(0, risposta.length()-1);
    	
    	// Invia a tutti la lista
    	for (Utente sessione : utenti) {
    		try {
                sessione.getSession().getBasicRemote().sendText(risposta);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
