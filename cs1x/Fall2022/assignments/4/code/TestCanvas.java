/**
 * Example of a simple drawing canvas.
 */

package turtle;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class TestCanvas extends JComponent {
    private int     width;
    private int     height;
    private int     lineWidth;
    private Color   background;
    private Color   color;

    public static void main(String[] args) {
        int w = 400;
        int h = 400;
        var f = new JFrame();
        f.setSize(w, h + 20);  // height: compensate for title bar
        f.setTitle("Canvas demo");
        var t = new TestCanvas(w, h);
        f.add(t);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public TestCanvas(int w, int h) {
        width      = w;
        height     = h;
        lineWidth  = 1;
        background = new Color(230, 215, 210);
        color      = Color.BLACK;
    }

    /**
     * Draw a line on the canvas.
     * @arg g      2D graphics context
     * @arg x1     x coordinate of one corner
     * @arg y1     y coordinate of one corner
     * @arg x2     x coordinate of other corner
     * @arg y2     y coordinate of other corner
     */
    public void drawLine(Graphics2D g, double x1, double y1, double x2, double y2) {
        var l = new Line2D.Double(x1, y1, x2, y2);
        g.setColor(color);
        g.setStroke(new BasicStroke(lineWidth));
        g.draw(l);
    }

    public void setBackground(Color color) {
        background = color;
    }

    protected void paintComponent(Graphics g) {
        var g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);
        var r = new Rectangle2D.Double(0, 0, width, height);
        g2d.setColor(background);
        g2d.fill(r);
        color = Color.RED; 
        drawLine(g2d, 10, 10, 390, 390);
        color = Color.BLUE; 
        drawLine(g2d, 10, 390, 390, 10);
    }
}
