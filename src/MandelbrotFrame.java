import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;

public class MandelbrotFrame extends JFrame {
    MandelbrotComponent mandelbrotComponent;

    public static long MAX_DELAY = 100;
    public static int MENU_MAXIMUM_WIDTH = 500;
    public static int VERTICAL_STRUCT_HEIGHT = 5;
    private JButton zoomInButton, zoomOutButton, upButton, downButton, leftButton, rightButton;
    private JSpinner iterationsSpinner;

    boolean isTriggered;
    private Timer timer;

    public MandelbrotFrame(String frameName) {
        super(frameName);
        this.setSize(800, 700);
        this.setLocation(50, 0);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        this.timer = new Timer();
        this.isTriggered = false;
        mandelbrotComponent = new MandelbrotComponent(800, 800, -2, 2, 4, 4);
        mandelbrotComponent.setSelectMode(true);
        // Points near the main cardioid line
//        mandelbrotComponent.setZ0(new ComplexNumber(-0.75, 0));
//        mandelbrotComponent.setZ0(new ComplexNumber(-1, 0));
//        mandelbrotComponent.setZ0(new ComplexNumber(-1.25,0.1));
//        mandelbrotComponent.setZ0(new ComplexNumber(0.355, 0.355));
//        mandelbrotComponent.setZ0(new ComplexNumber(1.75, 0));
//        mandelbrotComponent.setZ0(new ComplexNumber(-0.1, 0.65));
//        mandelbrotComponent.setZ0(new ComplexNumber(0.285, 0.01));
//        mandelbrotComponent.setZ0(new ComplexNumber(0.355, -0.355));
        mandelbrotComponent.setPixels2();
        mandelbrotComponent.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mandelbrotComponent.setLocation(50, 50);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.7;
        JPanel mandelbrotComponentPanel = new JPanel();
        mandelbrotComponentPanel.add(mandelbrotComponent);
        panel.add(mandelbrotComponent, constraints);
        mandelbrotComponentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JPanel menuPanel = createPanel(mandelbrotComponent);
        constraints.weightx = 0.3;
        constraints.gridx = 1;
        panel.add(menuPanel, constraints);
        mandelbrotComponent.setFocusable(true);
        this.setContentPane(panel);
        this.setActions();
        this.setVisible(true);
    }

    public JPanel createPanel(MandelbrotComponent mandelbrotComponent) {
        JPanel panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        JPanel iterationPanel = new JPanel();
        iterationPanel.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();
        cont.gridx = 0;
        cont.gridy = 0;
        cont.weightx = 1;
        cont.weighty = 1;
        cont.gridwidth = 1;
        JLabel iterationsLabel = new JLabel("Set Iterations");
        iterationPanel.add(iterationsLabel, cont);

        SpinnerNumberModel iterationsSpinnerNumberModel = new SpinnerNumberModel(mandelbrotComponent.getMaxIterations(), 1, 1000, 1);
        iterationsSpinner = new JSpinner(iterationsSpinnerNumberModel);

        iterationsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner spinner = (JSpinner) e.getSource();
                mandelbrotComponent.setMaxIterations((Integer) spinner.getValue());
                updateUI();
            }
        });
        cont.gridy = 2;
        iterationPanel.add(iterationsSpinner, cont);
        System.out.println(iterationPanel.getPreferredSize().getHeight());
        System.out.println(iterationPanel.getMaximumSize().getHeight());
        iterationPanel.setMaximumSize(iterationPanel.getPreferredSize());
        panel.add(iterationPanel);

        // ------------------------------------------------------------z0 Panel-----------------------------------------------------------------------------------------------------------------------------
        JSeparator separator1 = new JSeparator(JSeparator.HORIZONTAL);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        panel.add(separator1);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));

        JPanel z0Panel = new JPanel();
        z0Panel.setLayout(new GridBagLayout());
        cont.gridx = 0;
        cont.gridy = 0;
        cont.gridwidth = 4;
        JLabel equationLabel = new JLabel("<html>Initial value z0: f(z)=z0^2+c</html>");
        z0Panel.add(equationLabel, cont);
        cont.gridy = 1;
        cont.gridwidth = 1;
        cont.anchor = GridBagConstraints.EAST;
        z0Panel.add(new JLabel("Real: "), cont);
        SpinnerNumberModel spinnerNumberModelReal = new SpinnerNumberModel(mandelbrotComponent.getZ0().getReal(), -4, 4, 0.01);
        JSpinner realSpinner = new JSpinner(spinnerNumberModelReal);
        SpinnerNumberModel spinnerNumberModelImg = new SpinnerNumberModel(mandelbrotComponent.getZ0().getImaginary(), -4, 4, 0.01);
        JSpinner imgSpinner = new JSpinner(spinnerNumberModelImg);

        realSpinner.addChangeListener((ChangeEvent e) -> {
            JSpinner spinner = (JSpinner) e.getSource();
            double real = (double) spinner.getValue();
            mandelbrotComponent.getZ0().setReal(real);
            updateUI();
        });

        imgSpinner.addChangeListener((ChangeEvent e) -> {
            JSpinner spinner = (JSpinner) e.getSource();
            double img = (double) spinner.getValue();
            mandelbrotComponent.getZ0().setImaginary(img);
            updateUI();
        });

        cont.gridx = 1;
        cont.anchor = GridBagConstraints.WEST;
        z0Panel.add(realSpinner, cont);
        cont.gridx = 2;
        cont.anchor = GridBagConstraints.EAST;

        z0Panel.add(new JLabel("Img: "), cont);
        cont.gridx = 3;
        cont.anchor = GridBagConstraints.WEST;

        z0Panel.add(imgSpinner, cont);
        //Todo
        z0Panel.setMinimumSize(new Dimension(300, (int) z0Panel.getPreferredSize().getHeight()));
        z0Panel.setMaximumSize(new Dimension(300, (int) z0Panel.getPreferredSize().getHeight()));
        z0Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(z0Panel);

        //------------------------------------Zoom Panel added -------------------------------------------------------------------------------------------------------------------
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);
        panel.add(separator2);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));

        JPanel zoomPanel = new JPanel();
        zoomPanel.setLayout(new GridBagLayout());
        JLabel zoomLabel = new JLabel("Zoom");
        cont.gridx = 0;
        cont.gridy = 0;
        cont.gridwidth = 2;
        cont.anchor = GridBagConstraints.CENTER;
        zoomPanel.add(zoomLabel, cont);

        zoomInButton = new JButton("Zoom In");
        cont.gridy = 1;
        cont.anchor = GridBagConstraints.EAST;
        zoomPanel.add(zoomInButton, cont);

        zoomOutButton = new JButton("Zoom Out");
        cont.gridy = 1;
        cont.anchor = GridBagConstraints.WEST;
        zoomPanel.add(zoomOutButton, cont);

        zoomPanel.setMinimumSize(new Dimension(MENU_MAXIMUM_WIDTH, (int) zoomPanel.getPreferredSize().getHeight()));
        zoomPanel.setMaximumSize(new Dimension(MENU_MAXIMUM_WIDTH, (int) zoomPanel.getPreferredSize().getHeight()));
        panel.add(zoomPanel);

        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        JSeparator separator3 = new JSeparator();
        panel.add(separator3);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));

        // --------------------------------------------Move Left, Right, Up and Down-------------------------------------------------------------------------------------------------------
        upButton = new JButton("Up");
        downButton = new JButton("Down");
        leftButton = new JButton("Left");
        rightButton = new JButton("Right");

        JPanel movePanel = new JPanel();
        movePanel.setLayout(new GridBagLayout());
        cont.gridx = 0;
        cont.gridy = 0;
        cont.gridwidth = 3;
        cont.anchor = GridBagConstraints.CENTER;
        JLabel moveLabel = new JLabel("Choose Direction");
        movePanel.add(moveLabel, cont);
        cont.gridx = 1;
        cont.gridy = 1;
        cont.gridwidth = 1;
        cont.anchor = GridBagConstraints.ABOVE_BASELINE;
        movePanel.add(upButton, cont);
        cont.gridx = 0;
        cont.gridy = 2;
        cont.anchor = GridBagConstraints.EAST;
        movePanel.add(leftButton, cont);

        cont.gridx = 2;
        cont.gridy = 2;
        cont.anchor = GridBagConstraints.WEST;
        movePanel.add(rightButton, cont);

        cont.gridx = 1;
        cont.gridy = 3;
        cont.anchor = GridBagConstraints.CENTER;
        movePanel.add(downButton, cont);

        movePanel.setMaximumSize(movePanel.getPreferredSize());
        movePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(movePanel);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        JSeparator separator4 = new JSeparator();
        panel.add(separator4);
        panel.add(Box.createVerticalStrut(300));

        //----------------------------------------------------------------------------------------------------------------------
        return panel;
    }


    private void setActions() {

        zoomInButton.addActionListener((ActionEvent e) -> {
            // on button click,
            mandelbrotComponent.setMandelbrotLeftCornerX(mandelbrotComponent.getMandelbrotLeftCornerX() + mandelbrotComponent.getScalingFactor() / 2);
            mandelbrotComponent.setMandelbrotLeftCornerY(mandelbrotComponent.getMandelbrotLeftCornerY() - mandelbrotComponent.getScalingFactor() / 2);
            mandelbrotComponent.setMandelbrotHeight(mandelbrotComponent.getMandelbrotHeight() - mandelbrotComponent.getScalingFactor());
            mandelbrotComponent.setMandelbrotWidth(mandelbrotComponent.getMandelbrotWidth() - mandelbrotComponent.getScalingFactor());
            mandelbrotComponent.resetScalingFactor();
            updateUI();
        });

        zoomOutButton.addActionListener((ActionEvent e) -> {
            mandelbrotComponent.setMandelbrotLeftCornerX(mandelbrotComponent.getMandelbrotLeftCornerX() - mandelbrotComponent.getScalingFactor() / 2);
            mandelbrotComponent.setMandelbrotLeftCornerY(mandelbrotComponent.getMandelbrotLeftCornerY() + mandelbrotComponent.getScalingFactor() / 2);
            mandelbrotComponent.setMandelbrotHeight(mandelbrotComponent.getMandelbrotHeight() + mandelbrotComponent.getScalingFactor());
            mandelbrotComponent.setMandelbrotWidth(mandelbrotComponent.getMandelbrotWidth() + mandelbrotComponent.getScalingFactor());
            mandelbrotComponent.resetScalingFactor();
            updateUI();
        });

        upButton.addActionListener((ActionEvent e) -> {
            mandelbrotComponent.setMandelbrotLeftCornerY(mandelbrotComponent.getMandelbrotLeftCornerY() + mandelbrotComponent.getScalingFactor());
            updateUI();
        });

        downButton.addActionListener((ActionEvent e) -> {
            mandelbrotComponent.setMandelbrotLeftCornerY(mandelbrotComponent.getMandelbrotLeftCornerY() - mandelbrotComponent.getScalingFactor());
            updateUI();
        });

        leftButton.addActionListener((ActionEvent e) -> {
            mandelbrotComponent.setMandelbrotLeftCornerX(mandelbrotComponent.getMandelbrotLeftCornerX() - mandelbrotComponent.getScalingFactor());
            updateUI();
        });

        rightButton.addActionListener((ActionEvent e) -> {
            mandelbrotComponent.setMandelbrotLeftCornerX(mandelbrotComponent.getMandelbrotLeftCornerX() + mandelbrotComponent.getScalingFactor());
            updateUI();
        });

       mandelbrotComponent.addMouseMotionListener(new MouseMotionListener() {
           @Override
           public void mouseDragged(MouseEvent e) {

           }

           @Override
           public void mouseMoved(MouseEvent e) {
               mandelbrotComponent.setPoint(e.getPoint());
               try{
                   SwingUtilities.invokeLater(()-> {
                        mandelbrotComponent.repaint();
                   });
               }catch (Exception exception){
                   System.out.println("Exception caught: " + exception.getMessage());
               }
           }
       });

       mandelbrotComponent.addMouseListener(new MouseListener() {
           @Override
           public void mouseClicked(MouseEvent e) {

           }

           @Override
           public void mousePressed(MouseEvent e) {

           }

           @Override
           public void mouseReleased(MouseEvent e) {

           }

           @Override
           public void mouseEntered(MouseEvent e) {

           }

           @Override
           public void mouseExited(MouseEvent e) {
                mandelbrotComponent.setPoint(null);
                try{
                    SwingUtilities.invokeLater(() -> {
                        mandelbrotComponent.repaint();
                    });
                }catch (Exception exp){
                    System.out.println("Exception caught: "+exp.getMessage());
                }
           }
       });
    }

    private void updateUI() {
        if (isTriggered) {
            timer.cancel();
        }
        isTriggered = true;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("run method invoked");
                try {
                    SwingUtilities.invokeAndWait(() -> {
                        mandelbrotComponent.setPixels2();
                        mandelbrotComponent.repaint();
                        isTriggered = false;
                    });
                } catch (Exception exp) {
                    System.out.println("Exception caught while processing updateMandelbrotTimerTask: " + exp.getMessage());
                }
            }
        }, MAX_DELAY);
    }
}
