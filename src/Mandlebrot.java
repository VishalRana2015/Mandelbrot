import javax.swing.*;
import java.awt.*;

public class Mandlebrot {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Mandelbrot");
        frame.setSize(800,700);
        frame.setLocation(50,0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel =new JPanel();
        MandelbrotComponent comp =new MandelbrotComponent(1200,1200);
        comp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        comp.setLocation(50,50);
        panel.setLayout(null);
        panel.add(comp);
        comp.addKeyListener(new MyKeyListener());
        comp.setFocusable(true);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

}
