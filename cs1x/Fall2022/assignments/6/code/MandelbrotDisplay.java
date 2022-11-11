/**
 * Mandelbrot set display.
 */

package mandelbrot;

import java.util.Stack;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.event.*;
import javax.imageio.*;

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
        // Psychedelic.
        new Color[] {
            new Color(255,   0,   0),  // red
            new Color(255, 127,   0),  // orange
            new Color(255, 255,   0),  // yellow
            new Color(  0, 255,   0),  // green
            new Color(  0, 127, 255),  // blue-green
            new Color(  0, 255, 255),  // cyan
            new Color(  0,   0, 255),  // blue
        },
        // Muted psychedelic.
        new Color[] {
            new Color(200,   0,   0),
            new Color(200, 100,   0),
            new Color(  0, 200,   0),
            new Color(  0, 100, 200),
            new Color(  0,   0, 200),
        },
    };
}


/**
 * Class for displaying Mandelbrot images.
 */
public class MandelbrotDisplay extends JPanel {
    private MImage        mimg;
    private BufferedImage bimg;
    private Stack<MImage> history;
    // TODO: Declare whichever additional fields you need.

    public static void main(String[] args) {
        int w = 1200;
        int h =  900;

        JFrame frame = new JFrame();
        frame.getContentPane().add(new MandelbrotDisplay(w, h));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(w, h);
        frame.setVisible(true);
    }

    public MandelbrotDisplay(int width, int height) {
        super();
        if (width <= 0) {
            throw new IllegalArgumentException("width must be >= 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("height must be >= 0");
        }

        // TODO: Initialize field values.

        // Set the color palette.
        setColorMap();
  
        // Generate initial image of the Mandelbrot set.
        draw();

        // Add mouse and keyboard event handlers.

        MouseInputAdapter mouseEventHandler = new MouseInputAdapter() {
            public void mousePressed(MouseEvent e) {
                // NOTE: e.getX() is the current mouse x coordinate
                // NOTE: e.getY() is the current mouse y coordinate
                // TODO
            }

            public void mouseDragged(MouseEvent e) {
                // TODO
            }

            public void mouseReleased(MouseEvent e) {
                // TODO
            }
        };

        KeyAdapter keyListener = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                // NOTE: e.getKeyChar() is the key pressed (a char)
                // TODO
            }
        };

        addMouseListener(mouseEventHandler);
        addMouseMotionListener(mouseEventHandler);
        addKeyListener(keyListener);
        setFocusable(true);
    }

    /**
     * Set the color palette.
     */
    private void setColorMap() {
        // TODO
    }

    /**
     * Generate and display the Mandelbrot set.
     */
    private void draw() {
        // TODO
    }

    /**
     * Display the Mandelbrot set.
     */
    private void draw(MImage mimg) {
        // TODO
    }

    /**
     * Paint the panel.
     */
    public void paint(Graphics g) {
        g.drawImage(bimg, 0, 0, this);
        // TODO: also paint the box if there is one.
    }

    /**
     * Save an image.
     */
    private void saveImage(BufferedImage img, String filename) {
        File outputfile = new File(filename);
        try {
            ImageIO.write(img, "png", outputfile);
        } catch (IOException e) {
            System.err.println("ERROR: " + e);
        }
    }
}
