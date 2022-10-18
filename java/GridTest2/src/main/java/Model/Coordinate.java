/**
 * Model.java
 *
 * Models a Coordinate. Extends Ellipse2D.Double to create point on map using scaled X, Y values computed in Controller
 * class. Holds fetched Coordinates which are displayed by the grid (Grid.java).
 */
package Model;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Coordinate extends Ellipse2D.Double {
    private Point originalPoint;
    private Point scaledPoint;
    private String name;
    private int radius;
    public static ArrayList<Coordinate> coordinates = new ArrayList<>();

    /**
     * Creates Ellipse2D.Double that is used to draw on the Grid.
     *
     * See {@link View.Grid} for drawing
     * @param scaledPoint scaled X, Y coordinates to fit on screen
     * @param originalPoint original X, Y values fetched from server
     * @param name
     * @param radius
     */
    public Coordinate(Point scaledPoint, Point originalPoint, String name, int radius) {
        super(scaledPoint.x - radius / 2, scaledPoint.y - radius / 2, 20, 20);
        this.scaledPoint = scaledPoint;
        this.originalPoint = originalPoint;
        this.name = name;
    }

    public Point getOriginalPoint() {
        return originalPoint;
    }

    public void setOriginalPoint(Point originalPoint) {
        this.originalPoint = originalPoint;
    }

    public Point getScaledPoint() {
        return scaledPoint;
    }

    public void setScaledPoint(Point scaledPoint) {
        this.scaledPoint = scaledPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void clearCoordinates() {
        coordinates.clear();
    }

    @Override
    public String toString() {
        return "Model.Coordinate{" +
                "x=" + originalPoint.x +
                ", y=" + originalPoint.y +
                ", name='" + name + '\'' +
                '}';
    }
}
