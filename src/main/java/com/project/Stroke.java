package com.project;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// A freehand stroke: an ordered list of points with its own style
public class Stroke extends ObjectBase {

    private final List<Point2D> points = new ArrayList<>();

    public Stroke(int id, Color color, double width) {
        super(id, color, width);
    }

    @Override
    public String getType() {
        return "Traç lliure";
    }

    @Override
    public void addPoint(double x, double y) {
        points.add(new Point2D(x, y));
    }

    @Override
    public void move(double dx, double dy) {
        for (int i = 0; i < points.size(); i++) {
            points.set(i, points.get(i).add(dx, dy));
        }
    }

    @Override
    public Rectangle2D getBounds() {
        double minX = points.get(0).getX();
        double minY = points.get(0).getY();
        double maxX = minX;
        double maxY = minY;
        for (Point2D p : points) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }
        return createBounds(minX, minY, maxX, maxY);
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (points.isEmpty()) return;

        gc.setStroke(color);
        gc.setLineWidth(width);

        // Join each point with the next one
        gc.beginPath();
        gc.moveTo(points.get(0).getX(), points.get(0).getY());
        for (Point2D p : points) {
            gc.lineTo(p.getX(), p.getY());
        }
        gc.stroke();
    }
}
