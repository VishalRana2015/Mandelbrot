import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;

public class MandelbrotFrame extends JFrame {
    MandelbrotComponent mandelbrotComponent;

    public static long MAX_DELAY = 17;
    public static int MENU_MAXIMUM_WIDTH = 500;
    public static int MENU_MINIMUM_WIDTH = 300;
    public static int VERTICAL_STRUCT_HEIGHT = 5;
    private JButton zoomInButton, zoomOutButton, upButton, downButton, leftButton, rightButton, resetButton;
    private static JLabel xValueLabel, yValueLabel, currentPixelIterationCountLabel;
    private static JPanel pointPanel;
    private static JSlider paletteSlider;
    private static JTextField paletteValueField;

    private static JSpinner realSpinner, imgSpinner;
    private static SpinnerNumberModel iterationsSpinnerNumberModel, spinnerNumberModelReal, spinnerNumberModelImg;
    private JSpinner iterationsSpinner;


    boolean isTriggered;
    private Timer timer;

    public MandelbrotFrame(String frameName) throws Exception {
        super(frameName);
        setFont(new FontUIResource(new Font("Cabin", Font.PLAIN, 12)));
        this.setSize(new Dimension(1200, 800));
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        this.timer = new Timer();
        this.isTriggered = false;
        mandelbrotComponent = new MandelbrotComponent(800, 800, -2, 2, 4, 4);
        mandelbrotComponent.setSelectMode(true);
//        Points near the main cardioid line
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
        menuPanel.setMinimumSize(new Dimension(MENU_MINIMUM_WIDTH, 500));
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
        JLabel iterationsLabel = new JLabel("<html>Set Iterations</html>");
        iterationPanel.add(iterationsLabel, cont);

        iterationsSpinnerNumberModel = new SpinnerNumberModel(mandelbrotComponent.getMaxIterations(), 1, 5000, 1);
        iterationsSpinner = new JSpinner(iterationsSpinnerNumberModel);

        cont.gridy = 2;
        iterationPanel.add(iterationsSpinner, cont);
        iterationPanel.setMinimumSize(new Dimension(MENU_MINIMUM_WIDTH, 50));
        iterationPanel.setPreferredSize(new Dimension(MENU_MINIMUM_WIDTH, 50));
        iterationPanel.setMaximumSize(iterationPanel.getPreferredSize());
        iterationPanel.setBackground(Color.PINK);
        panel.add(iterationPanel);

        // ------------------------------------------------------------z0 Panel-----------------------------------------------------------------------------------------------------------------------------
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        panel.add(createSeparator());
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
        spinnerNumberModelReal = new SpinnerNumberModel(mandelbrotComponent.getZ0().getReal(), -4, 4, 0.01);
        realSpinner = new JSpinner(spinnerNumberModelReal);
        spinnerNumberModelImg = new SpinnerNumberModel(mandelbrotComponent.getZ0().getImaginary(), -4, 4, 0.01);
        imgSpinner = new JSpinner(spinnerNumberModelImg);

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
        z0Panel.setMinimumSize(new Dimension(MENU_MINIMUM_WIDTH, (int) z0Panel.getPreferredSize().getHeight()));
        z0Panel.setMaximumSize(new Dimension(MENU_MINIMUM_WIDTH, (int) z0Panel.getPreferredSize().getHeight()));
        z0Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(z0Panel);

        //------------------------------------Zoom Panel added -------------------------------------------------------------------------------------------------------------------
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        panel.add(createSeparator());
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));

        JPanel zoomPanel = new JPanel();
        zoomPanel.setLayout(new GridBagLayout());
        JLabel zoomLabel = new JLabel("Zoom");
        cont.gridx = 0;
        cont.gridy = 0;
        cont.gridwidth = 2;
        cont.anchor = GridBagConstraints.CENTER;
        zoomPanel.add(zoomLabel, cont);

        zoomInButton = new JButton("<html>Zoom In</html>");
        cont.gridy = 1;
        cont.anchor = GridBagConstraints.EAST;
        zoomPanel.add(zoomInButton, cont);

        zoomOutButton = new JButton("<html>Zoom Out</html>");
        cont.gridy = 1;
        cont.anchor = GridBagConstraints.WEST;
        zoomPanel.add(zoomOutButton, cont);

        zoomPanel.setMinimumSize(new Dimension(MENU_MAXIMUM_WIDTH, (int) zoomPanel.getPreferredSize().getHeight()));
        zoomPanel.setMaximumSize(new Dimension(MENU_MAXIMUM_WIDTH, (int) zoomPanel.getPreferredSize().getHeight()));
        zoomPanel.setBackground(Color.PINK);
        panel.add(zoomPanel);

        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        panel.add(createSeparator());
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));

        // --------------------------------------------Move Left, Right, Up and Down-------------------------------------------------------------------------------------------------------
        upButton = new JButton("<html>Up</html>");
        downButton = new JButton("<html>Down</html>");
        leftButton = new JButton("<html>Left</html>");
        rightButton = new JButton("<html>Right</html>");

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
        panel.add(createSeparator());
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));

        //--------------------------- Palette Length -------------------------------------------------------------------------------------------//
        JPanel palettePanel = new JPanel();
        BoxLayout palettePanelBoxLayout = new BoxLayout(palettePanel, BoxLayout.Y_AXIS);
        palettePanel.setLayout(palettePanelBoxLayout);
        JLabel paletteLabel = new JLabel("<html>Palette Length</html>");
        palettePanel.add(paletteLabel);
        paletteSlider = new JSlider(1, mandelbrotComponent.getMaxIterations());
        paletteSlider.setValue(mandelbrotComponent.getMandelbrotColor().getPaletteLength()); // initial palette length
        palettePanel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        palettePanel.add(paletteSlider);
        paletteValueField = new JTextField();
        paletteValueField.setText(paletteSlider.getValue() + "");
        paletteValueField.setMaximumSize(paletteValueField.getPreferredSize());
        paletteValueField.setMinimumSize(paletteValueField.getPreferredSize());
        paletteValueField.setDisabledTextColor(Color.BLACK);
        paletteValueField.disable();
        palettePanel.add(paletteValueField);

        panel.add(palettePanel);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        panel.add(createSeparator());
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));

        //----------------------------------------------------------------------------------------------------------------------
        pointPanel = new JPanel();
        pointPanel.setLayout(new GridBagLayout());
        cont = new GridBagConstraints();
        xValueLabel = new JLabel("xValue");
        yValueLabel = new JLabel("yValue");
        currentPixelIterationCountLabel = new JLabel("count");

        //printing x-value in the first row
        cont.gridx = 0;
        cont.gridy = 0;
        cont.weighty = 1;
        cont.weightx = 1;
        cont.anchor = GridBagConstraints.EAST;
        cont.gridwidth = 1;
        cont.gridheight = 1;
        pointPanel.add(new JLabel("<html>x:&nbsp</html>"), cont);

        cont.gridwidth = 3;
        cont.gridx = 1;
        cont.anchor = GridBagConstraints.WEST;
        pointPanel.add(xValueLabel, cont);

        // printing y value in the second row
        cont.gridx = 0;
        cont.gridy = 1;
        cont.gridwidth = 1;
        cont.anchor = GridBagConstraints.EAST;
        pointPanel.add(new JLabel("<html>y:&nbsp</html>"), cont);

        cont.gridx = 1;
        cont.gridwidth = 3;
        cont.anchor = GridBagConstraints.WEST;
        pointPanel.add(yValueLabel, cont);

        //-- printing iteration in the third row
        JPanel currentPixelEscapeIterationsCountShowPanel = new JPanel();
        currentPixelEscapeIterationsCountShowPanel.setLayout(new GridBagLayout());
        JLabel iterationTextLabel = new JLabel("<html>Iterations took to escape:&nbsp</html>");
        cont.gridx = 0;
        cont.gridy = 0;
        cont.weightx = 3;
        cont.gridwidth = 1;
        cont.anchor = GridBagConstraints.CENTER;
        currentPixelEscapeIterationsCountShowPanel.add(iterationTextLabel, cont);

        currentPixelIterationCountLabel = new JLabel("<html></html>");
        cont.gridx = 1;
        cont.gridy = 0;
        cont.weightx = 1;
        cont.gridwidth = 1;
        cont.anchor = GridBagConstraints.WEST;
        currentPixelEscapeIterationsCountShowPanel.add(currentPixelIterationCountLabel, cont);

        cont.gridx = 0;
        cont.gridy = 2;
        cont.fill = GridBagConstraints.HORIZONTAL;
        cont.gridwidth = 4;
        pointPanel.add(currentPixelEscapeIterationsCountShowPanel, cont);

        pointPanel.setPreferredSize(new Dimension(MENU_MAXIMUM_WIDTH, 75));
        pointPanel.setMaximumSize(new Dimension(MENU_MAXIMUM_WIDTH, 75));
        pointPanel.setMinimumSize(new Dimension(MENU_MAXIMUM_WIDTH, 75));
        panel.add(pointPanel);


        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        panel.add(createSeparator());
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));

        //---------------------------------------------------------------------------------------------------

        // -- Reset Button -----------------------------------------------------
        JPanel resetPanel = new JPanel();
        resetPanel.setLayout(new FlowLayout());
        resetButton = new JButton("<html>Reset</html");
        resetPanel.add(resetButton);
        resetPanel.setMinimumSize(new Dimension(MENU_MAXIMUM_WIDTH, 50));
        resetPanel.setPreferredSize(new Dimension(MENU_MAXIMUM_WIDTH, 50));
        resetPanel.setMaximumSize(new Dimension(MENU_MINIMUM_WIDTH, 50));
        resetPanel.setBackground(Color.PINK);
        panel.add(resetPanel);
        return panel;
    }

    private static JSeparator createSeparator() {
        return new JSeparator(JSeparator.HORIZONTAL);
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
                Point p = e.getPoint();
                mandelbrotComponent.setSelectionSquareEndingPoint(p);
                try {
                    SwingUtilities.invokeLater(() -> {
                        mandelbrotComponent.revalidate();
                        mandelbrotComponent.repaint();
                    });
                } catch (Exception exp) {
                    System.out.println("Exception caught: " + exp.getMessage());
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                Dimension dimension = mandelbrotComponent.getSize();
                Double x = (mandelbrotComponent.getMandelbrotWidth() * p.getX()) / dimension.getWidth() + mandelbrotComponent.getMandelbrotLeftCornerX();
                Double y = mandelbrotComponent.getMandelbrotLeftCornerY() - (mandelbrotComponent.getMandelbrotHeight() * p.getY()) / dimension.getHeight();
                xValueLabel.setText(String.format("%.20f", x));
                yValueLabel.setText(String.format("%.20f", y));
                // convert these points in pixel coordinates or image coordinates
                double pixelX = (mandelbrotComponent.getPixelWidth() / mandelbrotComponent.getMandelbrotWidth()) * (x - mandelbrotComponent.getMandelbrotLeftCornerX());
                double pixelY = (mandelbrotComponent.getPixelHeight() / mandelbrotComponent.getMandelbrotHeight()) * (mandelbrotComponent.getMandelbrotLeftCornerY() - y);
                Point pixelPoint = new Point((int) pixelX, (int) pixelY);
                currentPixelIterationCountLabel.setText(String.valueOf(mandelbrotComponent.getIterationsFor(pixelPoint)));
                pointPanel.setVisible(true);
                mandelbrotComponent.setCurrentMandelbrotPoint(e.getPoint());
                try {
                    SwingUtilities.invokeLater(() -> {
                        mandelbrotComponent.repaint();
                        pointPanel.revalidate();
                        pointPanel.repaint();
                    });
                } catch (Exception exception) {
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
                mandelbrotComponent.setCurrentMandelbrotPoint(null);
                Point p = e.getPoint();
                mandelbrotComponent.setSelectionSquareStartingPoint(p);
                mandelbrotComponent.setSelectionSquareEndingPoint(p);
                try {
                    SwingUtilities.invokeLater(() -> {
                        mandelbrotComponent.revalidate();
                        mandelbrotComponent.repaint();
                    });
                } catch (Exception exp) {
                    System.out.println("Exception caught: " + exp.getMessage());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point topLeftCorner = mandelbrotComponent.getSelectionSquareTopLeftCorner();
                int length = mandelbrotComponent.getSelectionSquareLength();

                // convert the topLeftCorner point, in real coordinates.
                Dimension dimension = mandelbrotComponent.getSize();
                Double x = mandelbrotComponent.getMandelbrotWidth() * ((double) topLeftCorner.getX() / mandelbrotComponent.getPixelWidth()) + mandelbrotComponent.getMandelbrotLeftCornerX();
                Double y = mandelbrotComponent.getMandelbrotLeftCornerY() - mandelbrotComponent.getMandelbrotHeight() * ((double) topLeftCorner.getY() / mandelbrotComponent.getPixelHeight());
                mandelbrotComponent.setMandelbrotLeftCornerX(x);
                mandelbrotComponent.setMandelbrotLeftCornerY(y);

                double mandelbrotWidth = mandelbrotComponent.getMandelbrotWidth() * (length / (double) mandelbrotComponent.getPixelWidth());
                double mandelbrotHeight = mandelbrotComponent.getMandelbrotHeight() * (length / (double) mandelbrotComponent.getPixelHeight());
                mandelbrotComponent.setMandelbrotWidth(mandelbrotWidth);
                mandelbrotComponent.setMandelbrotHeight(mandelbrotHeight);
                mandelbrotComponent.setPixels2();
                mandelbrotComponent.setSelectionSquareStartingPoint(null);// disabling selection mode.
                mandelbrotComponent.setSelectionSquareEndingPoint(null);
                mandelbrotComponent.resetScalingFactor();
                try {
                    SwingUtilities.invokeLater(() -> {
                        mandelbrotComponent.revalidate();
                        mandelbrotComponent.repaint();
                    });
                } catch (Exception exp) {
                    System.out.println("Exception caught: " + exp.getMessage());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
                mandelbrotComponent.setCurrentMandelbrotPoint(null);
                pointPanel.setVisible(false);
                try {
                    SwingUtilities.invokeLater(() -> {
                        mandelbrotComponent.repaint();
                        pointPanel.revalidate();
                        pointPanel.repaint();
                    });
                } catch (Exception exp) {
                    System.out.println("Exception caught: " + exp.getMessage());
                }
            }
        });

        iterationsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner spinner = (JSpinner) e.getSource();
                mandelbrotComponent.setMaxIterations((Integer) spinner.getValue());
                paletteSlider.setMaximum((int) spinner.getValue());
                updateUI();
            }
        });

        paletteSlider.addChangeListener((ChangeEvent e) -> {
            JSlider slider = (JSlider) e.getSource();
            int value = slider.getValue();
            paletteValueField.setText(String.valueOf(value));
            mandelbrotComponent.getMandelbrotColor().setPaletteLength(value);
            SwingUtilities.invokeLater(() -> {
                mandelbrotComponent.createImage();
                mandelbrotComponent.revalidate();
                mandelbrotComponent.repaint();
            });
        });

        resetButton.addActionListener((ActionEvent e) -> {
            iterationsSpinnerNumberModel.setValue(MandelbrotComponent.INITIAL_ITERATIONS);
            spinnerNumberModelReal.setValue(0.0);
            spinnerNumberModelImg.setValue(0.0);
            mandelbrotComponent.setMandelbrotLeftCornerX(MandelbrotComponent.MANDELBROT_INITIAL_LEFT_CORNER_X);
            mandelbrotComponent.setMandelbrotLeftCornerY(MandelbrotComponent.MANDELBROT_INITIAL_LEFT_CORNER_Y);
            mandelbrotComponent.setMandelbrotWidth(MandelbrotComponent.MANDELBROT_INITIAL_WIDTH);
            mandelbrotComponent.setMandelbrotHeight(MandelbrotComponent.MANDELBROT_INITIAL_HEIGHT);
            mandelbrotComponent.resetScalingFactor();
            updateUI();
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

    private void setFont(FontUIResource myFont) {
        UIManager.put("CheckBoxMenuItem.acceleratorFont", myFont);
        UIManager.put("Button.font", myFont);
        UIManager.put("ToggleButton.font", myFont);
        UIManager.put("RadioButton.font", myFont);
        UIManager.put("CheckBox.font", myFont);
        UIManager.put("ColorChooser.font", myFont);
        UIManager.put("ComboBox.font", myFont);
        UIManager.put("Label.font", myFont);
        UIManager.put("List.font", myFont);
        UIManager.put("MenuBar.font", myFont);
        UIManager.put("Menu.acceleratorFont", myFont);
        UIManager.put("RadioButtonMenuItem.acceleratorFont", myFont);
        UIManager.put("MenuItem.acceleratorFont", myFont);
        UIManager.put("MenuItem.font", myFont);
        UIManager.put("RadioButtonMenuItem.font", myFont);
        UIManager.put("CheckBoxMenuItem.font", myFont);
        UIManager.put("OptionPane.buttonFont", myFont);
        UIManager.put("OptionPane.messageFont", myFont);
        UIManager.put("Menu.font", myFont);
        UIManager.put("PopupMenu.font", myFont);
        UIManager.put("OptionPane.font", myFont);
        UIManager.put("Panel.font", myFont);
        UIManager.put("ProgressBar.font", myFont);
        UIManager.put("ScrollPane.font", myFont);
        UIManager.put("Viewport.font", myFont);
        UIManager.put("TabbedPane.font", myFont);
        UIManager.put("Slider.font", myFont);
        UIManager.put("Table.font", myFont);
        UIManager.put("TableHeader.font", myFont);
        UIManager.put("TextField.font", myFont);
        UIManager.put("Spinner.font", myFont);
        UIManager.put("PasswordField.font", myFont);
        UIManager.put("TextArea.font", myFont);
        UIManager.put("TextPane.font", myFont);
        UIManager.put("EditorPane.font", myFont);
        UIManager.put("TabbedPane.smallFont", myFont);
        UIManager.put("TitledBorder.font", myFont);
        UIManager.put("ToolBar.font", myFont);
        UIManager.put("ToolTip.font", myFont);
        UIManager.put("Tree.font", myFont);
        UIManager.put("FormattedTextField.font", myFont);
        UIManager.put("IconButton.font", myFont);
        UIManager.put("InternalFrame.optionDialogTitleFont", myFont);
        UIManager.put("InternalFrame.paletteTitleFont", myFont);
        UIManager.put("InternalFrame.titleFont", myFont);
    }
}
