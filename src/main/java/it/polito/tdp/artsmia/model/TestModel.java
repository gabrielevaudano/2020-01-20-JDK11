package it.polito.tdp.artsmia.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		Model m = new Model();
		m.creaGrafo("photographer");
		
		System.out.println(m.getGrafo());
	
		List<Integer> l = m.trovaPercorso(10224);
		System.out.println(l);
	}

}
