package com.project;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class Controller implements Initializable {

    @FXML private Canvas canvas;
    @FXML private Canvas colorsCanvas;
    @FXML private RadioButton lineRadio;
    @FXML private RadioButton rectRadio;
    @FXML private ColorPicker colorPicker;
    @FXML private ColorPicker fillPicker;
    @FXML private Spinner<Integer> widthSpinner;
    @FXML private ListView<ObjectBase> objectsList;
    @FXML private Button deleteButton;

    private GraphicsContext gc;

    // Drawing state
    private final ObservableList<ObjectBase> objects = FXCollections.observableArrayList();
    private ObjectBase currentObject = null;
    private int nextId = 1;

    private Boolean dragging = false;
    private double lastX, lastY;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gc = canvas.getGraphicsContext2D();
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);

        colorPicker.setValue(Color.BLACK);

        // The palette handles its own drawing and clicks
        new ColorPalette(colorsCanvas, colorPicker);

        objectsList.setItems(objects);
        objectsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> redraw());
        deleteButton.disableProperty().bind(objectsList.getSelectionModel().selectedItemProperty().isNull());

        // Drawing canvas events
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseReleased(this::onMouseReleased);

        redraw();
    }

    @FXML
    private void onToolClicked(MouseEvent event) {
        objectsList.getSelectionModel().clearSelection();
    }

    @FXML
    private void actionDelete(ActionEvent event) {
        objects.remove(objectsList.getSelectionModel().getSelectedItem());
        objectsList.getSelectionModel().clearSelection();
        redraw();
    }

    // Drawing canvas

    private void onMousePressed(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY) return;

        ObjectBase selected = objectsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (selected.getBounds().contains(event.getX(), event.getY())) {
                dragging = true;
                lastX = event.getX();
                lastY = event.getY();
            }
            return;
        }

        // Start a new stroke with the current style
        Color color = colorPicker.getValue();
        double width = widthSpinner.getValue();
        if (lineRadio.isSelected()) {
            currentObject = new Line(nextId, color, width, event.getX(), event.getY());
        } else if (rectRadio.isSelected()) {
            currentObject = new Rect(nextId, color, width, fillPicker.getValue(), event.getX(), event.getY());
        } else {
            currentObject = new Stroke(nextId, color, width);
        }
        currentObject.addPoint(event.getX(), event.getY());
        redraw();
    }

    private void onMouseDragged(MouseEvent event) {
        if (dragging) {
            objectsList.getSelectionModel().getSelectedItem().move(event.getX() - lastX, event.getY() - lastY);
            lastX = event.getX();
            lastY = event.getY();
            redraw();
            return;
        }

        if (currentObject == null) return;

        currentObject.addPoint(event.getX(), event.getY());
        redraw();
    }

    private void onMouseReleased(MouseEvent event) {
        dragging = false;

        if (currentObject == null) return;

        // The stroke is finished: keep it in memory
        currentObject.addPoint(event.getX(), event.getY());
        objects.add(currentObject);
        currentObject = null;
        nextId++;
        redraw();
    }

    private void redraw() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Saved strokes, in creation order
        for (ObjectBase obj : objects) {
            obj.draw(gc);
        }

        // Stroke being drawn right now (preview)
        if (currentObject != null) {
            currentObject.draw(gc);
        }

        ObjectBase selected = objectsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Rectangle2D bounds = selected.getBounds();
            gc.save();
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.setLineDashes(5);
            gc.strokeRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
            gc.restore();
        }
    }
}
