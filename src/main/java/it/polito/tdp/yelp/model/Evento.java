package it.polito.tdp.yelp.model;

public class Evento implements Comparable<Evento>{
	private int intervistatore;
	private User intervistato;
	private int tempo;
	private EventType tipo;
	
	public enum EventType {
		SUCCESSO,
		FALLIMENTO,
		FERIE
	}

	public Evento(int intervistatore, User intervistato, int tempo, EventType tipo) {
		super();
		this.intervistatore = intervistatore;
		this.intervistato = intervistato;
		this.tempo = tempo;
		this.tipo = tipo;
	}




	public int getIntervistatore() {
		return intervistatore;
	}




	public User getIntervistato() {
		return intervistato;
	}




	public int getTempo() {
		return tempo;
	}




	public EventType getTipo() {
		return tipo;
	}




	@Override
	public int compareTo(Evento o) {
		// TODO Auto-generated method stub
		return tempo-o.getTempo();
	}
}
