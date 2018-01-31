package com.istic;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static final long serialVersionUID = -2704222221111608377L;


    final ToggleGroup group = new ToggleGroup();

    @FXML
    Button startVCOButton,stopVCOButton;
    @FXML
    Slider frequencySlider;
    @FXML
    RadioButton sawRadio, traingleRadio,SquareRadio;

    VCO vco = new VCO();


    public void initialize(URL location, ResourceBundle resources) {
        sawRadio.setToggleGroup(group);
        traingleRadio.setToggleGroup(group);
        SquareRadio.setToggleGroup(group);
        SquareRadio.setSelected(true);
        frequencySlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                frequencySlider.setValue(Math.round(frequencySlider.getValue()));

                vco.changeOctave((int)frequencySlider.getValue());


            }
        });



    }


    public void startSoundVCO() throws InterruptedException {
        vco.start();


    }

    public void stopSoundVCO() throws InterruptedException {
        vco.stop();


    }

    public void squareSound(){
        vco.squareSound();



    }
    public void sawSound(){
        vco.sawSound();



    }
    public void triangleSound(){
       vco.triangleSound();


    }


}
