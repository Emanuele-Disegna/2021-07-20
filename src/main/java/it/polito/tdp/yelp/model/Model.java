package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private YelpDao dao;
	private Map<String, User> idMap;
	private Graph<User, DefaultWeightedEdge> grafo;
	private List<Adiacenza> adiacenze;
	double migliorPeso;
	
	public Model() {
		super();
		dao = new YelpDao();
		idMap = new HashMap<String, User>();
		migliorPeso = 0;
	}
	
	public void creaGrafo(int n, int anno) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		dao.getAllUsersWithMoreThanN(n, idMap);
		
		//Aggiungo i vertici
		Graphs.addAllVertices(grafo, idMap.values());
		
		//Aggiungo gli archi
		adiacenze = dao.getAllAdiacenze(anno, idMap);
		for(Adiacenza a : adiacenze) {
			Graphs.addEdge(grafo, a.getU1(), a.getU2(), a.getPeso());
		}
		
		System.out.println("numero di vertici = "+grafo.vertexSet().size());
		System.out.println("numero di archi = "+grafo.edgeSet().size());
	}
	
	public List<User> getUtentePiuSimile(User u) {
		System.out.println(Graphs.neighborListOf(grafo, u).size());
		
		List<User> res = new ArrayList<>();
		
		int max = 0;
		for(DefaultWeightedEdge e : grafo.edgesOf(u)) {
			if(grafo.getEdgeWeight(e)>max) {
				max = (int) grafo.getEdgeWeight(e);
			}
		}
		
		for(DefaultWeightedEdge e : grafo.edgesOf(u)) {
			if(grafo.getEdgeWeight(e)==max) {
				User u2 = Graphs.getOppositeVertex(grafo, e, u);
				res.add(u2);
			}
		}
		
		System.out.println(res.size());
		return res;
	}
	
	public Collection<User> getUsersInGrafo(){
		return idMap.values();
	}
	
	public Graph<User, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public double getMigliorPeso() {
		return migliorPeso;
	}
}
