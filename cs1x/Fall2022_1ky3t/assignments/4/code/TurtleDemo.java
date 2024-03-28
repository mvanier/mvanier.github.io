/**
 * Turtle graphics demo.
 */

package turtle;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class TurtleDemo {
    JFrame f;
    TurtleCanvas tc;
    int count;

    public static void main(String[] args) {
        int w = 800;
        int h = 800;
        var t = new TurtleDemo(w, h);
    }

    public TurtleDemo(int w, int h) {
        count = 0;
        f = new JFrame();
        tc = new TurtleCanvas(w, h);
        f.setSize(w, h);
        f.setTitle("Turtle graphics demo");
        f.add(tc);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        makeDrawing();
        f.addMouseListener(new MouseAdapter() {
           @Override
           public void mousePressed(MouseEvent e) {
              count++;
              makeDrawing();
              f.getContentPane().repaint();
           }
        });
    }

    public void makeDrawing() {
        int lines = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                if (lines >= count) {
                    return;
                }
                tc.forward(100);
                lines++;
                tc.rotate(90);
            }
            tc.rotate(90);
            tc.rotate(18);
        }
        tc.rotate(18);
    }
}

