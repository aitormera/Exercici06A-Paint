package com.project;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

// Predefined colors drawn on their own Canvas
public class ColorPalette {

    private static final int COLUMNS = 12;
    private static final int ROWS = 4;
    private static final Color[] COLORS = createColors();
    private static final double WIDTH = 18;
    private static final double TIP = 14;
    private static final double SPACING = 32;
    private static final double LIFT = 8;

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final ColorPicker colorPicker;

    public ColorPalette(Canvas canvas, ColorPicker colorPicker) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.colorPicker = colorPicker;

        canvas.setOnMouseClicked(this::onMouseClicked);
        colorPicker.valueProperty().addListener((obs, oldVal, newVal) -> draw());
        draw();
    }

    private static Color[] createColors() {
        Color[] colors = new Color[COLUMNS * ROWS];
        for (int col = 0; col < COLUMNS; col++) {
            double hue = col * 360.0 / COLUMNS;
            colors[col] = Color.gray(col / (COLUMNS - 1.0));
            colors[COLUMNS + col] = Color.hsb(hue, 0.8, 0.5);
            colors[COLUMNS * 2 + col] = Color.hsb(hue, 0.8, 1.0);
            colors[COLUMNS * 3 + col] = Color.hsb(hue, 0.4, 1.0);
        }
        return colors;
    }

    private double getTop(int row) {
        return LIFT + row * SPACING;
    }

    private void onMouseClicked(MouseEvent event) {
        int col = (int) (event.getX() / WIDTH);
        if (col >= COLUMNS) return;

        for (int row = ROWS - 1; row >= 0; row--) {
            if (event.getY() >= getTop(row)) {
                colorPicker.setValue(COLORS[row * COLUMNS + col]);
                return;
            }
        }
    }

    private void draw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Color color = COLORS[row * COLUMNS + col];
                double x = col * WIDTH;
                double y = getTop(row);

                if (color.equals(colorPicker.getValue())) {
                    y = y - LIFT;
                }

                gc.setFill(color);
                gc.fillRect(x, y + TIP, WIDTH, canvas.getHeight() - y - TIP);

                gc.setFill(Color.TAN);
                gc.fillPolygon(new double[] { x, x + WIDTH / 2, x + WIDTH }, new double[] { y + TIP, y, y + TIP }, 3);

                gc.setFill(color);
                gc.fillPolygon(new double[] { x + WIDTH / 2 - 3, x + WIDTH / 2, x + WIDTH / 2 + 3 }, new double[] { y + 5, y, y + 5 }, 3);

                gc.setStroke(Color.GRAY);
                gc.setLineWidth(0.5);
                gc.strokeRect(x, y + TIP, WIDTH, canvas.getHeight() - y - TIP);
            }
        }
    }
}
