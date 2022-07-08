package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.yelp.model.Evento.EventType;

public class Simulatore {

	//Parametri
	private int INTERVISTATORI;
	private int INTERVISTATI;
	private Model model;
	
	//Output
	private Statistiche st;
	
	//Coda degli eventi
	private PriorityQueue<Evento> coda;
	
	//Stato del mondo
	private int tempo;
	private List<User> giaIntervistati;
	
	
	public Simulatore(int iNTERVISTATORI, int iNTERVISTATI, Model model) {
		super();
		INTERVISTATORI = iNTERVISTATORI;
		INTERVISTATI = iNTERVISTATI;
		this.model = model;
		st = new Statistiche();
	}
	
	public void init() {
		tempo = 0;
		coda = new PriorityQueue<Evento>();
		giaIntervistati = new ArrayList<User>();
		
		
		//riempiamo la coda con gli eventi del primo giorno
		for(int i=0; i<INTERVISTATORI; i++) {
			creaEvento(i);
		}
		st.incrementaGiorni();
		tempo++;
	}
	
	private void creaEvento(int intervistatore) {
		User candidato = model.getUsersInGrafo().get((int) Math.random()*model.getUsersInGrafo().size());
		
		while(giaIntervistati.contains(candidato)){
			candidato = model.getUsersInGrafo().get((int) Math.random()*model.getUsersInGrafo().size());
		}
		
		giaIntervistati.add(candidato);
		
		double prob = Math.random()*100;
		Evento e;
		
		if(prob>=40) {
			//successo
			e = new Evento(intervistatore, candidato, tempo, EventType.SUCCESSO);
		} else if (prob>=20 && prob<40) {
			//ferie
			e = new Evento(intervistatore, candidato, tempo, EventType.FERIE);
		} else {
			//fallimento
			e = new Evento(intervistatore, candidato, tempo, EventType.FALLIMENTO);
		}
		
		coda.add(e);
	}
	
	private void creaEvento(int intervistatore, User utenteSimile) {
		
		giaIntervistati.add(utenteSimile);
		
		double prob = Math.random()*100;
		Evento e;
		
		if(prob>=40) {
			//successo
			e = new Evento(intervistatore, utenteSimile, tempo, EventType.SUCCESSO);
		} else if (prob>=20 && prob<40) {
			//ferie
			e = new Evento(intervistatore, utenteSimile, tempo, EventType.FERIE);
		} else {
			//fallimento
			e = new Evento(intervistatore, utenteSimile, tempo, EventType.FALLIMENTO);
		}
		
		coda.add(e);
	}
	
	private void creaEvento(int intervistatore, List<User> utentiPiuSimili) {
		User candidato = utentiPiuSimili.get((int) Math.random()*utentiPiuSimili.size());
		
		while(giaIntervistati.contains(candidato)){
			candidato = utentiPiuSimili.get((int) Math.random()*utentiPiuSimili.size());
		}
		
		giaIntervistati.add(candidato);
		
		double prob = Math.random()*100;
		Evento e;
		
		if(prob>=40) {
			//successo
			e = new Evento(intervistatore, candidato, tempo, EventType.SUCCESSO);
		} else if (prob>=20 && prob<40) {
			//ferie
			e = new Evento(intervistatore, candidato, tempo, EventType.FERIE);
		} else {
			//fallimento
			e = new Evento(intervistatore, candidato, tempo, EventType.FALLIMENTO);
		}
		
		coda.add(e);
	}
	
	public void run() {
		while(!coda.isEmpty() && giaIntervistati.size()<INTERVISTATI) {
			Evento e = coda.poll();
			processaEvento(e);
		}
	}

	private void processaEvento(Evento e) {
		
		switch(e.getTipo()) {
		case SUCCESSO:
			st.incrementaIntervistati(e.getIntervistatore());
			
			List<User> utentiPiuSimili = model.getUtentePiuSimile(e.getIntervistato());
			
			if(utentiPiuSimili.size()==1) {
				if(giaIntervistati.contains(utentiPiuSimili.get(0))) {
					creaEvento(e.getIntervistatore());
				} else {
					creaEvento(e.getIntervistatore(), utentiPiuSimili.get(0));
				}
			} else {
				int i = 0;
				for(int j = 0; j<utentiPiuSimili.size(); j++) {
					if(!giaIntervistati.contains(utentiPiuSimili.get(j))) {
						i++;
					}
				}
				if(i>0) {
					creaEvento(e.getIntervistatore(), utentiPiuSimili);
				} else {
					creaEvento(e.getIntervistatore());
				}
			}
			
			
			break;
		case FALLIMENTO:
			double prob = Math.random()*100;
			Evento ee;
			
			if(prob>=40) {
				//successo
				ee = new Evento(e.getIntervistatore(), e.getIntervistato(), tempo, EventType.SUCCESSO);
			} else if (prob>=20 && prob<40) {
				//ferie
				ee = new Evento(e.getIntervistatore(), e.getIntervistato(), tempo, EventType.FERIE);
			} else {
				//fallimento
				ee = new Evento(e.getIntervistatore(), e.getIntervistato(), tempo, EventType.FALLIMENTO);
			}
			
			coda.add(ee);
			tempo++;
			st.incrementaGiorni();
			break;
		case FERIE:
			st.incrementaIntervistati(e.getIntervistatore());
			
			//salto un giorno
			int t = tempo+1;
			
			double random = Math.random()*model.getUsersInGrafo().size();
			
			while(giaIntervistati.contains(model.getUsersInGrafo().get((int) random))) {
				random = Math.random()*model.getUsersInGrafo().size();
			}
			
			User candidato = model.getUsersInGrafo().get((int) random);
			giaIntervistati.add(candidato);
			
			double probb = Math.random()*100;
			Evento eee;
			
			if(probb>=40) {
				//successo
				eee = new Evento(e.getIntervistatore(), candidato, t, EventType.SUCCESSO);
			} else if (probb>=20 && probb<40) {
				//ferie
				eee = new Evento(e.getIntervistatore(), candidato, t, EventType.FERIE);
			} else {
				//fallimento
				eee = new Evento(e.getIntervistatore(), candidato, t, EventType.FALLIMENTO);
			}
			
			coda.add(eee);
			
			break;
		}
		
		
	}
	
	private User selezionaIntervistato(Collection<User> lista) {
		User u = null;
		
		
	}
	
}
