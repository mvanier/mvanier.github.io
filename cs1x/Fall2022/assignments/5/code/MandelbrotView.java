/**
 * Mandelbrot set display.
 * This program non-interactively displays the Mandelbrot set given
 * command-line parameters.
 */

package mandelbrot;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Data class for holding color palette information.
 */
class Palettes {
    static Color[][] palettes = new Color[][] {
        // Default, good contrast.
        // https://stackoverflow.com/questions/16500656/
        //   which-color-gradient-is-used-to-color-mandelbrot-in-wikipedia
        new Color[] {
            new Color( 66,  30,  15),  // brown 3
            new Color( 25,   7,  26),  // dark violet
            new Color(  9,   1,  47),  // darkest blue
            new Color(  4,   4,  73),  // blue 5
            new Color(  0,   7, 100),  // blue 4
            new Color( 12,  44, 138),  // blue 3
            new Color( 24,  82, 177),  // blue 2
            new Color( 57, 125, 209),  // blue 1
            new Color(134, 181, 229),  // blue 0
            new Color(211, 236, 248),  // lightest blue
            new Color(241, 233, 191),  // lightest yellow
            new Color(248, 201,  95),  // light yellow
            new Color(255, 170,   0),  // dirty yellow
            new Color(204, 128,   0),  // brown 0
            new Color(153,  87,   0),  // brown 1
            new Color(106,  52,   3),  // brown 2
        },
    };
}


/**
 * Class for displaying Mandelbrot images.
 */
public class MandelbrotView extends JPanel {
    private int           width;          // image width in pixels
    private int           height;         // image height in pixels
    private double        xmin;           // minimum x coordinate in the image
    private double        xmax;           // maximum x coordinate in the image
    private double        ymin;           // minimum y coordinate in the image
    private double        ymax;           // maximum y coordinate in the image
    private int           maxiterations;  // maximum iteration count
    private Color[]       colorMap;       // color map
    private BufferedImage bimg;           // actual image

    public static void main(String[] args) {
        // TODO: Process command-line arguments.
        // TODO: Define ymax so that the aspect ratio is preserved.

        // Compute and display the image.
        JFrame frame = new JFrame();
        frame.getContentPane().add(
          new MandelbrotView(width, height, xmin, ymin, xmax, ymax));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setVisible(true);
    }

    /**
     * Print a usage message and exit.
     */
    public static void usage() {
        System.err.print("usage: java mandelbrot.MandelbrotView ");
        System.err.println("width height xmin ymin xmax");
        System.err.println("  width:  width in pixels (must be > 0)");
        System.err.println("  height: height in pixels (must be > 0)");
        System.err.println("  xmin:   minimum x value");
        System.err.println("  ymin:   minimum y value");
        System.err.println("  xmax:   maximum x value");
        System.err.println("  xmax must be > xmin");
        System.err.print("  ymax is computed from the window aspect ratio");
        System.err.println(" (width / height)");
        System.err.print("example: java mandelbrot.MandelbrotView ");
        System.err.println("1200 900 -2.5 -1.5 1.5");
        System.exit(1);
    }

    public MandelbrotView(int width, int height,
      double xmin, double ymin, double xmax, double ymax) {
        super();
        this.width    = width;
        this.height   = height;
        this.xmin     = xmin;
        this.ymin     = ymin;
        this.xmax     = xmax;
        this.ymax     = ymax;
        maxiterations = 1024;

        // Set the color map.
        setColorMap()
        // Generate initial image of the Mandelbrot set.
        draw();
    }

    /**
     * Set the color map.
     */
    private void setColorMap() {
        // TODO
    }

    /**
     * Paint the panel.
     *
     * @param g  graphics context of the panel
     */
    public void paint(Graphics g) {
        g.drawImage(bimg, 0, 0, this);
    }

    /**
     * Generate and display the Mandelbrot set.
     */
    private void draw() {
        Mandelbrot man = new Mandelbrot(width, height,
          xmin, ymin, xmax, ymax, maxiterations);
        draw(man.getImg());
    }

    /**
     * Display the Mandelbrot set.
     *
     * @param mimg  the image data (iteration counts) to display
     */
    private void draw(MImage mimg) {
        // TODO
    }
 }
