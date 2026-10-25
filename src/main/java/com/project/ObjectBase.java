package com.project;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class ObjectBase {

    private final int id;
    protected final Color color;
    protected final double width;

    public ObjectBase(int id, Color color, double width) {
        this.id = id;
        this.color = color;
        this.width = width;
    }

    public abstract String getType();

    public abstract void addPoint(double x, double y);

    public abstract void move(double dx, double dy);

    public abstract Rectangle2D getBounds();

    // Cada objecte decideix com es dibuixa.
    public abstract void draw(GraphicsContext gc);

    protected Rectangle2D createBounds(double minX, double minY, double maxX, double maxY) {
        double margin = width / 2 + 4;
        return new Rectangle2D(minX - margin, minY - margin, maxX - minX + margin * 2, maxY - minY + margin * 2);
    }

    @Override
    public String toString() {
        return getType() + " " + id;
    }
}
