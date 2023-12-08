
import javax.swing.*;
import java.awt.*;

public class Mandelbrot {

    public static void main(String[] args) {
        UIManager.getLookAndFeelDefaults()
                .put("defaultFont", new Font("Arial", Font.BOLD, 22));
        try{
            JFrame frame = new MandelbrotFrame("Mandelbrot Frame");
            frame.setVisible(true);
        }
        catch (Exception exp){
            System.out.println("Exception caught while instantiating instance of MandelbrotFrame class");
            System.out.println(exp.getMessage());
        }
    }
}
