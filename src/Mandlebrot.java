import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Mandlebrot {

    public static void main(String[] args) {
        JFrame frame = new MandelbrotFrame("Mandelbrot Frame");
        frame.setVisible(true);
    }
}
