package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();

    	try {
    		if (model.getGrafo()==null || model.getGrafo().vertexSet().size()==0 || model.getGrafo().edgeSet().size()==0)
        		throw new NullPointerException("Il grafo non è stato creato o è vuoto.");
        	    	
        	for (Adiacenza a : model.getArtistiConnessi())
        		txtResult.appendText(String.format("L'artista %s è connesso con %s, il numero di esposizioni comuni è %f\n", a.getA1(), a.getA1(), a.getWeight()));
        
    	} 
		catch (Exception e) {
			txtResult.setText(e.getMessage());
		}
	}

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	
    	try {
    		if (txtArtista.getText().isBlank())
        		throw new NullPointerException("Non è stato inserito alcun artista.");
        		
    		List<Integer> l = model.trovaPercorso(Integer.parseInt(txtArtista.getText()));
    		
        	for (Integer a : l)
        		txtResult.appendText(String.format("Artista con ID %d\n",	 a) );
        	
        	txtResult.appendText(String.format("Il numero totale di artista è %d", l.size()));
        	
    	} catch (NumberFormatException ne) {
    		txtResult.setText("Il campo indicato non è valido. Inserire un numero.");
    	}
    	catch (Exception e) {
    		txtResult.setText(e.getMessage());
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();

    	try {
    		if (this.boxRuolo.getSelectionModel().getSelectedItem().isBlank())
	    		throw new NullPointerException("Non è stato selezionato alcun ruolo.");
	    	
	    	model.creaGrafo(this.boxRuolo.getSelectionModel().getSelectedItem());
	    	
	    	txtResult.appendText(String.format("Il grafo è stato creato con successo. Contiene %d vertici e %d archi.\n", model.getGrafo().vertexSet().size(), model.getGrafo().edgeSet().size()));
    	} catch (Exception e) {
	    		txtResult.setText(e.getMessage());
    	}
	}

    public void setModel(Model model) {
    	this.model = model;
    	
    	try {
    		this.boxRuolo.getItems().addAll(model.getRuolo());
    	} catch (Exception e) {
    		txtResult.setText(e.getMessage());
    	}
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
