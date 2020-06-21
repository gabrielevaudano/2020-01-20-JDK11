package it.polito.tdp.artsmia.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	Graph<Integer, DefaultWeightedEdge> grafo;
	List<String> roles;
	ArtsmiaDAO dao;
	List<Adiacenza> adiacenze;
	List<Integer> path;
	
	public Model() {
		dao = new ArtsmiaDAO();
		roles = new ArrayList<String>(dao.listRoles());
	}
	
	public List<String> getRuolo() {
		return dao.listRoles();
	}
	
	public void creaGrafo(String role) {
		if (role==null)
			throw new InvalidParameterException("Non è stato selezionato un ruolo.");
		
		this.grafo = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.listArtistsWithRole(role));
		
		this.adiacenze = new ArrayList<Adiacenza>();
		
		for (Adiacenza a : dao.listArchi(role))
			if (this.grafo.vertexSet().contains(a.getA1()) && this.grafo.vertexSet().contains(a.getA2()))
			{
				
				Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getWeight());
				adiacenze.add(a);
			}
	}
	
	public Graph<Integer, DefaultWeightedEdge> getGrafo() {
		if (this.grafo==null)
			throw new NullPointerException("Non è stato generato alcun grafo.");
		
		return this.grafo;
	}
	
	public List<Adiacenza> getArtistiConnessi() {
		if (this.adiacenze == null)
			throw new NullPointerException("Non è ancora stata creata una lista di artisti connessi. Probabilmente la procedura di creazione del grafo non è stata avviata o non è andata a buon fine.");
		 
		this.adiacenze.sort(new Comparator<Adiacenza>() {
	
				@Override
				public int compare(Adiacenza o1, Adiacenza o2) {
					// TODO Auto-generated method stub
					return -o1.getWeight().compareTo(o2.getWeight());			}
			});
	 
	 return adiacenze;
	}
	
	public List<Integer> trovaPercorso(Integer a) {
		if (!this.grafo.vertexSet().contains(a))
			throw new NullPointerException("Il grafo non contiene l'artista selezionato.");
	
		List<Integer> parziale = new ArrayList<Integer>();
		
		ricerca(parziale, null, a);
		return path;
	}

	private void ricerca(List<Integer> parziale, Double weight, Integer precedente) {			
		for (Integer successivo : Graphs.neighborListOf(this.grafo, precedente)) {
			if (weight==null)
				weight = grafo.getEdgeWeight(grafo.getEdge(precedente, successivo));
				
			if (grafo.getEdgeWeight(grafo.getEdge(precedente, successivo)) == weight) 
				if (!parziale.contains(successivo)) {
					parziale.add(successivo);
					ricerca(parziale, weight, successivo);
					parziale.remove(successivo);
				}
			
		}
		
		if (path==null || parziale.size() > path.size())
			path = new ArrayList<Integer>(parziale);
		
	}
	
	
}
