
import javax.swing.*;
import java.awt.*;

public class Mandlebrot {

    public static void main(String[] args) {
        UIManager.getLookAndFeelDefaults()
                .put("defaultFont", new Font("Arial", Font.BOLD, 22));
        JFrame frame = new MandelbrotFrame("Mandelbrot Frame");
        frame.setVisible(true);
    }
}
