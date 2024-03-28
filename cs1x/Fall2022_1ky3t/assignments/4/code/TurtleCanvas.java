/**
 * Canvas for turtle graphics operations.
 */

package turtle;

import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * Record class for lines.
 * x1, y1 - the coordinates of the starting point in the line
 * x2, y2 - the coordinates of the ending point in the line
 */
record Line (double x1, double y1, double x2, double y2) {}

/**
 * A drawing surface for turtle graphics.
 */
public class TurtleCanvas extends JComponent {
    // TODO declare fields here.

    /**
     * Constructor for TurtleCanvas.
     * @param width   width of the canvas in pixels
     * @param height  height of the canvas in pixels
     */
    public TurtleCanvas(int width, int height) {
        // TODO
    }

    /**
     * Draw a line on the canvas.
     * @param g      2D graphics context
     * @param line   line to draw
     */
    public void drawLine(Graphics2D g, Line line) {
        // TODO
    }

    /**
     * Move the turtle forward.
     * @param len  distance to move the turtle.
     */
    public void forward(double len) {
        // TODO
    }

    /**
     * Rotate the turtle
     * @param deg  the number of degrees to rotate the turtle
     */
    public void rotate(double deg) {
        // TODO
    }

    /**
     * Clear the lines from the drawing.
     */
    public void clearLines() {
        // TODO
    }

    /**
     * Put the turtle pen down.
     */
    public void penDown() {
        // TODO
    }

    /**
     * Lift the turtle pen up.
     */
    public void penUp() {
        // TODO
    }

    /**
     * Move the turtle to a new absolute position on the canvas.
     * Do not draw a line.
     *
     * @param x   x coordinate to move to
     * @param y   y coordinate to move to
     */
    public void moveTo(double x, double y) {
        // TODO
    }

    public void setAngle(double angle) {
        // TODO
    }

    /**
     * Set the background color.
     */
    public void setBackground(Color color) {
        // TODO
    }

    /**
     * Set or unset the highlight property.
     * @param b  true if you want to set the highlight property,
     *           false otherwise
     */
    public void setHighlight(boolean b) {
        // TODO
    }

    /**
     * Render the entire image on the canvas based on the lines.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // TODO
    }
}
