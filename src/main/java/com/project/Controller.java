package com.project;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

public class Controller implements Initializable {

    // Elements de la vista (el nom ha de coincidir amb el fx:id del layout.fxml)
    @FXML private Canvas canvas;
    @FXML private Canvas colorsCanvas;

    @FXML private ToggleGroup toolGroup;
    @FXML private RadioButton freeRadio;
    @FXML private RadioButton lineRadio;
    @FXML private RadioButton rectRadio;

    @FXML private ColorPicker strokePicker;
    @FXML private ColorPicker fillPicker;
    @FXML private Spinner<Integer> widthSpinner;

    @FXML private ListView<String> objectsList;
    @FXML private Button deleteButton;

    // "Rotulador" de l'àrea de dibuix
    private GraphicsContext gc;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gc = canvas.getGraphicsContext2D();

        // Colors inicials del formulari
        strokePicker.setValue(Color.BLACK);
        fillPicker.setValue(Color.LIGHTBLUE);

        redraw();
    }

    // Esborra el Canvas i ho torna a dibuixar tot
    private void redraw() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    @FXML
    private void actionDelete(ActionEvent event) {
        // Pendent: eliminar l'objecte seleccionat
    }
}
