/**
 * Grid.java
 *
 * JPanel that represents grid where map, axes, and Coordinate points are drawn to. Also has method for showing
 * Coordinate ToolTip text.
 */
package View;

import Model.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Grid extends JPanel {
    private int originX;
    private int originY;
    private ArrayList<Coordinate> coordinates;
    private final double X_SCALING = 0.63733;
    private final double Y_SCALING = 0.298;
    Image image;


    public Grid() {
        setToolTipText("");
        setLayout(null);
    }

    /**
     * Initializes origin to center of screen so that Coordinates can be drawn in proper location.
     */
    public void initOrigin() {
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2 );
    }

    public int getOriginX() {
        return originX;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public int getOriginY() {
        return originY;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Fetches Coordinate ArrayList so that they can be drawn to the screen.
     */
    public void getCoordinates() {
        coordinates = Coordinate.coordinates;
        repaint();
    }

    public double getX_SCALING() {
        return X_SCALING;
    }

    public double getY_SCALING() {
        return Y_SCALING;
    }

    /**
     * Overrides paintComponent() method to draw map, axes, and Coordinates to screen.
     * @param graphics
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        initOrigin();
        Graphics2D g2d = (Graphics2D) graphics;

        // Draws image to take up entire JPanel
        g2d.drawImage(this.image, 0, 0, getWidth(), getHeight(), null);

        // Draws X & Y axes
        g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
        g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());

        // Draws all Coordinates
        if (coordinates != null) {
            // draw coordinate points with translated screen coordinates
            for (int i = 0; i < coordinates.size(); i++) {
                g2d.fill(coordinates.get(i));
            }
        }
    }

    /**
     * Displays ToolTip with name and X,Y coordinates when hovering over each Coordinate.
     * @param event the {@code MouseEvent} that initiated the
     *              {@code ToolTip} display
     * @return
     */
    @Override
    public String getToolTipText(MouseEvent event) {
        if (coordinates != null) {
            for (Coordinate coordinate : coordinates) {
                if (coordinate.contains(event.getPoint())) {
                    return coordinate.getName() + ": " + coordinate.getOriginalPoint().x + ", "
                            + coordinate.getOriginalPoint().y;
                }
            }
        }
        return null;
    }
}
