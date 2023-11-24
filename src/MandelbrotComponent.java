import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.MemoryImageSource;

public class MandelbrotComponent extends JComponent {
    int pixelWidth, pixelHeight;
    private double scalingFactor;
    public static int INITIAL_ITERATIONS = 500;
    private int scalingFactorDivider = 10;
    private int maxIterations = 0;
    /* Mandelbrot function is f(x) = x^2 + c */
    private ComplexNumber z0;

    double mandelbrotLeftCornerX, mandelbrotLeftCornerY, mandelbrotCenterX, mandelbrotCenterY, mandelbrotWidth, mandelbrotHeight;
    /* mandelbrotFrameWidth and mandelbrotFrameHeight represents width and height of the graph in real numbers */
    int sx, sy, sw, sh;
    Image mandelbrotImage;
    int[] pixels;
    boolean selectMode;

    private static final double MANDELBROT_INITIAL_WIDTH = 4.0;
    private static final double MANDELBROT_INITIAL_HEIGHT = 4.0;
    private static final double MANDELBROT_INITIAL_LEFT_CORNER_X = -2.0;
    private static final double MANDELBROT_INITIAL_LEFT_CORNER_Y = 2.0;
    private static final double MANDELBROT_INITIAL_CENTER_X = 0.0;
    private static final double MANDELBROT_INITIAL_CENTER_Y = 0.0;

    public void setSelectMode(boolean selectMode) {
        this.selectMode = selectMode;
    }

    public boolean isSelectMode() {
        return selectMode;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public int getMaxIterations() {
        return this.maxIterations;
    }

    public MandelbrotComponent(int pixelWidth, int pixelHeight, double mandelbrotLeftCornerX, double mandelbrotLeftCornerY, double mandelbrotWidth, double mandelbrotHeight) {
        setSize(new Dimension(pixelWidth, pixelHeight));
        this.mandelbrotLeftCornerX = mandelbrotLeftCornerX;
        this.mandelbrotLeftCornerY = mandelbrotLeftCornerY;
        this.mandelbrotCenterX = MANDELBROT_INITIAL_CENTER_X;
        this.mandelbrotCenterY = MANDELBROT_INITIAL_CENTER_Y;
        this.mandelbrotWidth = mandelbrotWidth;
        this.mandelbrotHeight = mandelbrotHeight;
        this.scalingFactor = mandelbrotWidth / scalingFactorDivider;
        this.setListeners();
        this.maxIterations = INITIAL_ITERATIONS;
        this.setZ0(new ComplexNumber(0, 0));
    }

    private void createImage() {
        MemoryImageSource mis = new MemoryImageSource(pixelWidth, pixelHeight, pixels, 0, pixelWidth);
        this.mandelbrotImage = createImage(mis);
    }

    public void setScalingFactor(double scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    public double getScalingFactor() {
        return scalingFactor;
    }

    public double getMandelbrotLeftCornerX() {
        return mandelbrotLeftCornerX;
    }

    public void setMandelbrotLeftCornerX(double mandelbrotLeftCornerX) {
        this.mandelbrotLeftCornerX = mandelbrotLeftCornerX;
    }

    public double getMandelbrotLeftCornerY() {
        return mandelbrotLeftCornerY;
    }

    public void setMandelbrotLeftCornerY(double mandelbrotLeftCornerY) {
        this.mandelbrotLeftCornerY = mandelbrotLeftCornerY;
    }

    public void setMandelbrotWidth(double mandelbrotWidth) {
        this.mandelbrotWidth = mandelbrotWidth;
    }

    public void setMandelbrotHeight(double mandelbrotHeight) {
        this.mandelbrotHeight = mandelbrotHeight;
    }

    public double getMandelbrotHeight() {
        return mandelbrotHeight;
    }

    public double getMandelbrotWidth() {
        return mandelbrotWidth;
    }

    public void resetScalingFactor() {
        this.scalingFactor = mandelbrotWidth / scalingFactorDivider;
    }

    public MandelbrotComponent(int pixelWidth, int pixelHeight) {
        // initial parameters
        setSize(new Dimension(pixelWidth, pixelHeight));
        mandelbrotLeftCornerX = MANDELBROT_INITIAL_LEFT_CORNER_X;
        mandelbrotLeftCornerY = MANDELBROT_INITIAL_LEFT_CORNER_Y;
        mandelbrotWidth = MANDELBROT_INITIAL_WIDTH;
        mandelbrotHeight = MANDELBROT_INITIAL_HEIGHT;
        mandelbrotCenterX = MANDELBROT_INITIAL_CENTER_X;
        mandelbrotCenterY = MANDELBROT_INITIAL_CENTER_Y;
        this.scalingFactor = mandelbrotWidth / 400;
        this.setZ0(new ComplexNumber(0, 0));
        this.maxIterations = INITIAL_ITERATIONS;
//        sw = pixelWidth / 8; // They are not being used while drawing the mandelbrot
//        sh = pixelHeight / 8;
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        this.pixelWidth = (int) d.getWidth();
        this.pixelHeight = (int) d.getHeight();
        this.setSize(pixelWidth, pixelHeight);
        this.setPreferredSize(new Dimension(pixelWidth, pixelHeight));
        this.setMaximumSize(new Dimension(pixelWidth, pixelHeight));
        this.setMinimumSize(new Dimension(pixelWidth, pixelHeight));
        // Creating a mandelbrot image
        pixels = new int[pixelWidth * pixelHeight];

    }

    public void setZ0(ComplexNumber z0) {
        this.z0 = z0;
    }

    public ComplexNumber getZ0() {
        return z0;
    }

    public void setPixels2() {
        pixels = new int[pixelWidth * pixelHeight];
        // for each pixel in the mandelbrot image
        int iterationColorRatio = (int) Math.ceil(((double) this.maxIterations) / colorArray.length);
        int index = 0;
        double lengthOfAPixelInMandelbrot = (mandelbrotWidth) / pixelWidth;
        double heightOfAPixelInMandelbrot = (mandelbrotHeight) / pixelHeight;
        for (int pixelY = 0; pixelY < pixelHeight; pixelY++) {
            for (int pixelX = 0; pixelX < pixelWidth; pixelX++) {
                // Now get what point current pixel represents in Mandelbrot image
                double x, y;
                x = lengthOfAPixelInMandelbrot * pixelX + mandelbrotLeftCornerX;
                y = mandelbrotLeftCornerY - heightOfAPixelInMandelbrot * pixelY;
                int iterationsTookToEscape = calculateMandelbrotIterations(new ComplexNumber(x, y));
                try {
                    Color color = Color.getHSBColor(((float) iterationsTookToEscape) / this.maxIterations, 1.0f, 1.0f);
                    if (iterationsTookToEscape == 0) {
                        color = Color.getHSBColor(0.25f, 1.0f, 0.2f);
                    }
                    pixels[index++] = color.getRGB();
                } catch (Exception exp) {
                    System.out.println("iterations : " + iterationsTookToEscape);
                    System.out.println("iterationColorRatio : " + iterationColorRatio);
                    System.out.println("colorArray.length: " + colorArray.length);
                    throw new RuntimeException("Exception");
                }
            }
        }
        createImage();
    }

    public void setPixels() {
        double incrementXBy, incrementYBy;
        incrementXBy = mandelbrotWidth / pixelWidth;
        incrementYBy = mandelbrotHeight / pixelHeight;
        int index = 0;
        double currentPixelX, currentPixelY;
        currentPixelY = mandelbrotLeftCornerY;
        int cindex = 0;
        for (int y = 0; y < pixelHeight; y++) {
            currentPixelX = mandelbrotLeftCornerX;
            for (int x = 0; x < pixelWidth; x++) {
                int itr = calculateMandelbrotIterations(new ComplexNumber(currentPixelX, currentPixelY));
                cindex = itr / 60;
                pixels[index++] = colorArray[cindex].getRGB();
                currentPixelX = currentPixelX + incrementXBy;
            }
            currentPixelY = currentPixelY - incrementYBy;
        }
        System.out.println("Pixels set successfully");
        createImage();
    }

    Color[] colorArray = {new Color(0, 0, 120),
            new Color(0, 0, 255),
            new Color(0, 120, 120),
            new Color(0, 200, 20),
            new Color(0, 255, 0),
            new Color(20, 200, 0),
            new Color(100, 120, 0),
            new Color(70, 70, 0),
            new Color(200, 0, 0),
            new Color(120, 0, 0),
            new Color(70, 0, 0),
            new Color(50, 0, 0),
            new Color(255, 255, 200)};


    /**
     * This function calculates the number of iterations required for a complex number to exceed a magnitude of 2. In the Mandelbrot set, numbers that consistently remain under this threshold after a given number of iterations are considered part of the set. Any complex number reaching a magnitude greater than 2 within the specified maximum iterations is deemed to escape to infinity and is not part of the Mandelbrot set. <br/>
     * Used to find whether the given complex number is in mandelbrot set or not
     *
     * @param x ComplexNumber
     * @return Number of iterations took to verify whether the point is in mandelbrot set or not.
     **/
    private int calculateMandelbrotIterations(ComplexNumber x) {
        int iterations = 0;
        ComplexNumber z = x;
        while (iterations < maxIterations) {
            if (iterations == 0) {
                // this is the first iterations, therefore just check the magnitude of c
                if (z.getMagnitude() > 2) {
                    break;
                }
                z = ComplexNumber.add(ComplexNumber.multiply(z0, z0), x);
            } else {
                if (z.getMagnitude() > 2) {
                    break;
                }
                z = ComplexNumber.add(ComplexNumber.multiply(z, z), x);
            }
            iterations++;
        }
        return iterations;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("In paint method");
        int x, y, w, h;
        w = this.getWidth() - (this.getInsets().left + this.getInsets().right);
        h = this.getHeight() - (this.getInsets().top + this.getInsets().bottom);
        x = (int) g.getClipBounds().getX();
        y = (int) g.getClipBounds().getY();
        Graphics2D gg = (Graphics2D) g.create();
        gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        gg.drawImage(mandelbrotImage, x, y, w, h, 0, 0, mandelbrotImage.getWidth(null), mandelbrotImage.getHeight(null), null);
        if (this.isSelectMode()) {
            BasicStroke s = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            gg.setStroke(s);
            gg.setColor(new Color(255, 255, 255));
            gg.drawRect(sx, sy, sw, sh);
        }
        gg.dispose();
        System.out.println("Image Drawned");
    }

    private void setListeners() {
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                System.out.println("point : " + e.getPoint());
                System.out.println("e.getX: " + e.getX());
                System.out.println("e.getY: " + e.getY());
                System.out.println("e.getRotations: " + e.getWheelRotation());
                System.out.println("e.getPreciseWheelRotation: " + e.getPreciseWheelRotation());
            }
        });
    }
}


