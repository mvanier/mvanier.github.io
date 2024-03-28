/**
 * Mandelbrot set generation.
 */

package mandelbrot;

public class Mandelbrot {
    // TODO: declare fields here.
    private MImage mimg;

    public Mandelbrot(int width, int height,
      double xmin, double ymin, double xmax, double ymax,
      int maxiterations) {
        // TODO
    }

    /**
     * Compute the iteration count of the Mandelbrot given the actual (x, y) coordinates.
     * https://en.wikipedia.org/wiki/Plotting_algorithms_for_the_Mandelbrot_set
     *
     * @param x0  x coordinate of a point in the complex plane
     * @param y0  y coordinate of a point in the complex plane
     */
    private int mcount(double x0, double y0) {
        // TODO
    }

    /**
     * "Draw" the Mandelbrot set into an MImage.
     */
    public void draw() {
        // TODO
    }

    public MImage getImg() { return mimg; }
}
