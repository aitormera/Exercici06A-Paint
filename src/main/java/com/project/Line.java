package com.project;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line extends ObjectBase {

    private double x1, y1, x2, y2;

    public Line(int id, Color color, double width, double x, double y) {
        super(id, color, width);
        this.x1 = x;
        this.y1 = y;
        this.x2 = x;
        this.y2 = y;
    }

    @Override
    public String getType() {
        return "Línia";
    }

    @Override
    public void addPoint(double x, double y) {
        x2 = x;
        y2 = y;
    }

    @Override
    public void move(double dx, double dy) {
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
    }

    @Override
    public Rectangle2D getBounds() {
        return createBounds(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2));
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setLineWidth(width);
        gc.strokeLine(x1, y1, x2, y2);
    }
}
