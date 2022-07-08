package it.polito.tdp.yelp.model;

import java.util.HashMap;
import java.util.Map;

public class Statistiche {
	private Map<Integer, Integer> conto;
	private int giorni;
	public Statistiche() {
		super();
		this.conto = new HashMap<>();
		this.giorni = 0;
	}
	
	public void incrementaGiorni() {
		giorni++;
	}
	
	public void incrementaIntervistati(Integer u) {
		if(!conto.containsKey(u)) {
			conto.put(u, 1);
		} else {
			int i = conto.get(u)+1;
			conto.put(u, i);
		}
	}

	public Map<Integer, Integer> getConto() {
		return conto;
	}

	public int getGiorni() {
		return giorni;
	}
	
	
}
