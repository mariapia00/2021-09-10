package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Graph<Business,DefaultWeightedEdge> grafo;
	private Map<String, Business> idMap;
	private Business maxDistance = null;
	private List<Business> best;
	
	public Model() {
		dao = new YelpDao();
		idMap = new HashMap<>();
	}
	
	public List<String> getCities(){
		return this.dao.getCities();
	}
	
	public void creaGrafo(String citta) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getBusinessByCity(citta));
		
		// riempo idMap
				for(Business b : this.dao.getBusinessByCity(citta)) {
					this.idMap.put(b.getBusinessId(), b);
				}
				
				// aggiungo gli archi
				for(Collegate c : this.dao.getCollegate(citta, idMap)) {
					Graphs.addEdge(this.grafo, c.getB1(), c.getB2(), c.getWeight());
				
				}
		
	}
	public double calcolaLocaleDistante(Business b) {
		//trovare il locale ADIACENTE più distante da b
		List<Business> adiacenti = Graphs.neighborListOf(this.grafo, b);
		double max = Double.MIN_VALUE;
		for(Business bus : adiacenti) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(b, bus));
			if(peso> max) {
				max = peso;
				maxDistance = bus;
			}
		}
		return max;
	}
	
	public List<Business> getpercorso(Business b1, Business b2, double media){
		best = new ArrayList<>();
		List<Business> parziale = new ArrayList<>();
		parziale.add(b1);
		ricorsiva(parziale, b2, 0, media);
		return best;
	}
	private void ricorsiva(List<Business> parziale, Business b2, int livello, double media) {
		if(parziale.size()> best.size()) {
			if(parziale.get(parziale.size()-1).equals(b2)) {
				best = new ArrayList<>(parziale);
			}
		}
		if(livello==this.grafo.vertexSet().size())
			return;
		
		for(Business b : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))){
			if(!parziale.contains(b) && (b.getStars()>media || b.equals(b2))){
				parziale.add(b);
				ricorsiva(parziale, b2, livello+1, media);
				parziale.remove(parziale.size()-1);
			}
		}
	}
	public YelpDao getDao() {
		return dao;
	}

	public Graph<Business, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public Map<String, Business> getIdMap() {
		return idMap;
	}

	public Business getMaxDistance() {
		return maxDistance;
	}

	public Set<Business> getBusinessByCity() {
		return this.grafo.vertexSet();
	}

	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}

	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}

	
}
