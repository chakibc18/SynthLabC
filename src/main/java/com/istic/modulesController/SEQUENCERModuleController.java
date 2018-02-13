package com.istic.modulesController;

import com.istic.port.Port;
import com.istic.sequencer.Sequence;
import com.istic.whitenoise.BruitBlanc;
import javafx.scene.control.Slider;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.plaf.SliderUI;

public class SEQUENCERModuleController extends ModuleController implements Initializable {


    @FXML
    AnchorPane pane;
    
    @FXML
    ImageView gate;
    
    @FXML
    ImageView out;
    
    @FXML
    Slider sliderSeq1,sliderSeq2,sliderSeq3,sliderSeq4,sliderSeq5,sliderSeq6,sliderSeq7,sliderSeq8;
    
    
    
    Sequence sequence;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
        Slider[] sliders = new Slider[]{ sliderSeq1, sliderSeq2,sliderSeq3,sliderSeq4,sliderSeq5
        		,sliderSeq6,sliderSeq7,sliderSeq8};
    	
    	for(int i =0 ;i<sliders.length;i++){
    		final int j = i;
    		sliders[i].valueProperty().addListener((ov, old_val, new_val) -> {
            double value = sliders[j].getValue();
            sliders[j].setValue(value);
            sequence.setValue(value,j);
            System.out.println(j);
        });
    	}
    }

    
    /**
     * Initialise le contrôleur du module et
     * ajoute le module au synthétiseur
     *
     * @param controller controleur general
     */
    public void init(Controller controller) {
        super.init(controller);
        this.sequence = new Sequence();
        this.controller.getSynth().add(sequence);
    }
    
    /**
     * Récupère l'information concernant le port sur lequel l'utilisateur a cliqué
     * @return le port sur lequel l'utilisateur a cliqué côté IHM
     */
    @Override
    public Port getCurrentPort() {
        if (currentPort == 0) {
            return sequence.getOutputPort();
        } else if (currentPort == 1) {
            return sequence.getGatePort();
        }
        return null;
    }

    @Override
    public Map<ImageView, Port> getAllPorts() {
    	Map<ImageView, Port> hm = new HashMap<>();
    	hm.put(out, sequence.getOutputPort());
    	hm.put(gate, sequence.getGatePort());
        return hm;
    }
    
    
    /**
     * Connecting the outPort to draw the cable
     */
    public void connectOutPort() {

        if(!this.sequence.getOutput().isConnected()) {
        	currentPort = 0;
            getLayout(out);
            super.connect();
        }
    }
    
    /**
     * Connecting the outPort to draw the cable
     */
    public void connectGatePort() {

        if(!this.sequence.getGatePort().isConnected()) {
        	currentPort = 1;
            getLayout(gate);
            super.connect();
        }
    }
    
    

    /**
     * Supprime le module du Board ainsi que les cables
     * et les dépendances côté modèle
     *
     * @throws IOException si deconnexion impossible
     */
    @FXML // A decommenter et adapter quand le model sequencer sera fait !
    public void removeModule() {
        if(this.controller.getTemporaryCableModuleController()==null) {
        	Port port1 = sequence.getOutputPort();
        	Port port2 = sequence.getGatePort();
            super.disconnect(port1);
            super.disconnect(port2);
            // Deconnexion du module Output du synthetizer
            this.controller.getSynth().remove(sequence);
            // Get parent node of pane corresponding to OutMod
            // Recupere le noeud parent fxml du outmod
            StackPane stackPane = (StackPane) pane.getParent();
            // supprime le mod niveau ihm
            stackPane.getChildren().remove(pane);
            this.controller.disconnect(this);
        }
    }
}