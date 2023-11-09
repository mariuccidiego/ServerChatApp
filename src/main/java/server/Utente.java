package server;

import jakarta.websocket.Session;

public class Utente {
	private Session session;
	private boolean approvato;
	public String nome;

    public Utente(Session session, String nome) {
        this.session = session;
        this.nome = nome;  
    }

	public boolean getApprovato() {
		return approvato;
	}

	public void setApprovato(boolean approvato) {
		this.approvato = approvato;
	}

	public Session getSession() {
		return session;
	}
	
	

}
