/**
 * Sierpinski triangle drawing.
 */

package turtle;

import java.awt.*;
import javax.swing.*;

public class Sierpinski {
    JFrame f;
    TurtleCanvas tc;

    public static void usage() {
        System.err.println("usage: java turtle.Sierpinski nlevels");
        System.err.println("  (nlevels must be >= 0)");
        System.exit(1);
    }

    public static void main(String[] args) {
        try {
            int nlevels = Integer.parseInt(args[0]);
            if (nlevels < 0) {
                usage();
            }
            var s = new Sierpinski(800, 800, nlevels);
        } catch (NumberFormatException e) {
            usage();
        } catch (ArrayIndexOutOfBoundsException e) {
            usage();
        }
    }

    /**
     * Draw a Sierpinski triangle on a turtle canvas.
     * @param width    width of the canvas in pixels
     * @param height   height of the canvas in pixels
     * @param nlevels  number of levels of the Sierpinski drawing
     */
    public Sierpinski(int width, int height, int nlevels) {
        f = new JFrame();
        f.setSize(width, height);
        f.setTitle("Sierpinski triangle");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // TODO: finish this method; `tc` is the TurtleCanvas you need to create.

        f.add(tc);
        f.setVisible(true);
    }

    /**
     * Draw an equilateral triangle, pointing up, with side length `size`.
     * Start and end at the lower left corner.
     * The drawing angle starts and ends at 0 degrees.
     *
     * @param size  the side length
     */
    public void triangleUp(double size) {
        // TODO
    }

    /**
     * Draw an equilateral triangle, pointing down, with side length `size`.
     * Start and end at the bottom point.
     * The drawing angle starts and ends at 0 degrees.
     *
     * @param size  the side length
     */
    public void triangleDown(double size) {
        // TODO
    }


    /**
     * Draw the Sierpinski triangle internals.
     * @param size   the triangle side length
     * @param level  the nesting level of the Sierpinski triangle,
     *               where 0 means "draw nothing".
     */
    public void drawInternals(double size, int level) {
        // TODO
    }
}

