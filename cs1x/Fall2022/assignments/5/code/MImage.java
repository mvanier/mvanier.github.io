/**
 * Mandelbrot set image.
 */

package mandelbrot;

/**
 * Instances of this class contains the bounding information of the
 * image rectangle (minimum/maximum x and y values) and the 2D array
 * of iteration counts used by the display routines.
 */
public class MImage {
    private int nx;
    private int ny;
    private int[][] counts;
    private double xmin;
    private double ymin;
    private double xmax;
    private double ymax;

    public MImage(int nx, int ny,
      double xmin, double ymin, double xmax, double ymax) {
        this.nx     = nx;
        this.ny     = ny;
        this.counts = new int[nx][ny];
        this.xmin   = xmin;
        this.ymin   = ymin;
        this.xmax   = xmax;
        this.ymax   = ymax;
    }

    /**
     * Set the iteration count at a particular position in the array.
     *
     * @param i      x coordinate in pixels
     * @param j      y coordinate in pixels
     * @param count  iteration count
     */
    public void set(int i, int j, int count) {
        if (i < 0 || i >= nx || j < 0 || j >= ny) {
            String msg = "i = " + i + "; j = " + j;
            throw new ArrayIndexOutOfBoundsException(msg);
        }
        counts[i][j] = count;
    }

    /**
     * Get the iteration count at a particular position in the array.
     *
     * @param i      x coordinate in pixels
     * @param j      y coordinate in pixels
     */
    public int get(int i, int j) {
        if (i < 0 || i >= nx || j < 0 || j >= ny) {
            String msg = "i = " + i + "; j = " + j;
            throw new ArrayIndexOutOfBoundsException(msg);
        }
        return counts[i][j];
    }

    public double getXmin() { return xmin; }
    public double getYmin() { return ymin; }
    public double getXmax() { return xmax; }
    public double getYmax() { return ymax; }
}
