package com.istic.modulesController;

import com.istic.cable.CableController;
import com.istic.port.Port;
import com.istic.port.PortController;
import com.istic.util.Style;

import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ModuleController implements Serializable {
    /**
     * Le grand Controller
     */
    protected Controller controller;

    /**
     * Node du controller
     */
    Node root;

    /**
     * valeur de la position du port
     */
    protected double x = 0, y = 0;


    /**
     * sur quel port l'utisateur à decider de cabler
     */
    protected int currentPort = -1;

    /**
     * Sert lors de la sauvegarde et de la restoration
     */
    JSONObject jsonModuleObject;

    /**
     * Liste des Ports controller
     */
    ArrayList<PortController> portControllers;

    /**
     * Lie le controller du module au controller général
     *
     * @param controller controller général
     */
    public void init(Controller controller) {
        this.controller = controller;
        this.portControllers = new ArrayList<>();
    }

    /**
     *  Get the position of the module on the UI
     * @param stacks the positions on the UI
     * @return position du module
     */
    public int getPosition(StackPane[] stacks) {
        int position = -1, current = -1;

        for (StackPane stack : stacks) {
            current++;
            if (!stack.getChildren().isEmpty()) {
                if (stack.getChildren().get(0).getUserData().equals(this)) {
                    position = current;
                }
            }
        }
        return position;
    }

    /**
     * Crée un cable entre deux ports
     */
    public void connect() {
        Line line = this.controller.getMouseLine();
        // si il y a déja une connection, on connect
        if (this.controller.isPlugged() && !this.controller.getTemporaryCableModuleController().equals(this)) {
            this.controller.connect(this);
            this.controller.setTemporaryCableModuleController(null);
            this.controller.setPlugged(false);
            line.setVisible(false);

        } else if (this.controller.isPlugged()) {  // si c'est le même module
            this.controller.setTemporaryCableModuleController(null);
            this.controller.setPlugged(false);
            line.setVisible(false);

        } else { // si c'est le premier port, on le garde en variable temporaire
            line.setStartX(this.getX());
            line.setStartY(this.getY());
            line.setVisible(true);
            this.controller.setPlugged(true);
            this.controller.setTemporaryCableModuleController(this);
        }
    }

    /**
     * Supprime le cable branché sur un port de module
     *
     * @param port port duquel on veut supprimer le cable
     */
    public void disconnect(Port port) {
        ArrayList<CableController> cables = (ArrayList<CableController>) this.controller.getCables().clone();
        Port portOne;
        Port portTwo;
        CableController removeCable = null;

        for (CableController cableController : cables) {
            portOne = cableController.getCable().getPortOne();
            portTwo = cableController.getCable().getPortTwo();
            if (portOne.equals(port)) {
                cableController.disconnect();
                removeCable = cableController;
            }
            if (portTwo.equals(port)) {
                cableController.disconnect();
            }
        }
        if (removeCable != null) {
            this.controller.getCables().remove(removeCable);
        }
    }


    /**
     * Get the layout of the port on the UI
     *
     * @param port the port clicked on the UI
     */
    public void getLayout(ImageView port) {

        Bounds boundsInScene = port.localToScene(port.getBoundsInLocal());
        x = (boundsInScene.getMaxX() + boundsInScene.getMinX()) / 2.0;
        y = (boundsInScene.getMaxY() + boundsInScene.getMinY()) / 2.0;
    }

    /**
     * Récupère l'information concernant le port sur lequel l'utilisateur a cliqué
     * @return le port sur lequel l'utilisateur a cliqué côté IHM
     */
    public abstract Port getCurrentPort();
    
    public abstract void updateTheme(int i);

    /**
     * met à jour le cables du port lors d'un dragAndDrop
     *
     * @param port le port à mettre un jour
     */
    public void updateCablesPositionFromPort(Port port) {
        List<CableController> cables = this.controller.getCables();
        Port portOne;
        Port portTwo;
        for (CableController cableController : cables) {
            portOne = cableController.getCable().getPortOne();
            portTwo = cableController.getCable().getPortTwo();
            if (portOne.equals(port)) {
                cableController.updatePosition(1);
            } else if (portTwo.equals(port)) {
                cableController.updatePosition(2);
            }
        }
    }



    /**
     * Met à jour la position des cables liés au module
     */
    public void updateCablesPosition() {
        for (PortController portController : getAllPorts()) {
            getLayout(portController.getView());
            this.updateCablesPositionFromPort(portController.getPort());
        }
    }

    public abstract void removeModule();

    /**
     * recupere les ports du module avec le composant javafx associé
     * @return les ports
     */
    public ArrayList<PortController> getAllPorts(){
        return  portControllers;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Sauvegarde l'etat courant du module dans un objet  json
     */
    public void serialize() {
        jsonModuleObject = new JSONObject();
        jsonModuleObject.put("type", this.getClass().getSimpleName());
        jsonModuleObject.put("position", getPosition(this.controller.getStacks()));


    }

    public JSONObject getJsonModuleObject() {
        return jsonModuleObject;
    }

    public void setJsonModuleObject(JSONObject jsonModuleObject) {

        this.jsonModuleObject = jsonModuleObject;
    }

    /**
     * Charge une configuration du plan de montage depuis un objet json
     * @param jsonObjectModule configuration à charger
     */
    public abstract void restore(JSONObject jsonObjectModule);

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}