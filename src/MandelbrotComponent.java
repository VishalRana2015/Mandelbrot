import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MandelbrotComponent extends JComponent {
    int pixelWidth, pixelHeight;
    private double scalingFactor;
    public static int INITIAL_ITERATIONS = 500;
    private int scalingFactorDivider = 10;
    private MandelbrotColor mandelbrotColor;
    private int maxIterations = 0;
    /* Mandelbrot function is f(x) = x^2 + c */
    private ComplexNumber z0;

    private ExecutorService executorService;

    double mandelbrotLeftCornerX, mandelbrotLeftCornerY, mandelbrotCenterX, mandelbrotCenterY, mandelbrotWidth, mandelbrotHeight;
    /* mandelbrotFrameWidth and mandelbrotFrameHeight represents width and height of the graph in real numbers */
    int sx, sy, sw, sh;
    Image mandelbrotImage;
    int[] pixels;
    boolean selectMode;
    private Point currentMandelbrotPoint;
    private Point selectionSquareStartingPoint, selectionSquareEndingPoint;
    private int selectionSquareLength;
    private static final double MANDELBROT_INITIAL_WIDTH = 4.0;
    private static final double MANDELBROT_INITIAL_HEIGHT = 4.0;
    private static final double MANDELBROT_INITIAL_LEFT_CORNER_X = -2.0;
    private static final double MANDELBROT_INITIAL_LEFT_CORNER_Y = 2.0;
    private static final double MANDELBROT_INITIAL_CENTER_X = 0.0;
    private static final double MANDELBROT_INITIAL_CENTER_Y = 0.0;
    private static final int FIRST_MANDELBROT_ITERATIONS_TO_STORE = 100;

    private static final Color LINE_COLOR = Color.white;
    private static final int LINE_THICKNESS = 1;

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

    private int THREAD_COUNT = 16;

    public void setCurrentMandelbrotPoint(Point currentMandelbrotPoint) {
        this.currentMandelbrotPoint = currentMandelbrotPoint;
    }

    public Point getCurrentMandelbrotPoint() {
        return this.currentMandelbrotPoint;
    }

    public MandelbrotComponent(int pixelWidth, int pixelHeight, double mandelbrotLeftCornerX, double mandelbrotLeftCornerY, double mandelbrotWidth, double mandelbrotHeight) throws Exception {
        setSize(new Dimension(pixelWidth, pixelHeight));
        executorService = Executors.newFixedThreadPool(THREAD_COUNT);
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
        this.mandelbrotColor = new MandelbrotColor(colorArray);
    }


    public void createImage() {
        MemoryImageSource mis = new MemoryImageSource(pixelWidth, pixelHeight, pixels, 0, pixelWidth);
        this.mandelbrotImage = createImage(mis);
    }

    public void setScalingFactor(double scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    public static double getMandelbrotInitialLeftCornerX() {
        return MANDELBROT_INITIAL_LEFT_CORNER_X;
    }

    public static double getMandelbrotInitialLeftCornerY() {
        return MANDELBROT_INITIAL_LEFT_CORNER_Y;
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

    public void setSelectionSquareStartingPoint(Point selectionSquareStartingPoint) {
        this.selectionSquareStartingPoint = selectionSquareStartingPoint;
    }

    public Point getSelectionSquareStartingPoint() {
        return this.selectionSquareStartingPoint;
    }

    public void setSelectionSquareEndingPoint(Point selectionSquareEndingPoint) {
        this.selectionSquareEndingPoint = selectionSquareEndingPoint;
    }

    public Point getSelectionSquareEndingPoint() {
        return this.selectionSquareEndingPoint;
    }

    public int getPixelHeight() {
        return pixelHeight;
    }

    public int getPixelWidth() {
        return pixelWidth;
    }

    public MandelbrotColor getMandelbrotColor() {
        return mandelbrotColor;
    }

    public void setMandelbrotColor(MandelbrotColor mandelbrotColor) {
        this.mandelbrotColor = mandelbrotColor;
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

    private Color[] colors = new Color[]{
            new Color(254, 0, 0),
            new Color(255, 121, 1),
            new Color(255, 255, 11),
            new Color(34, 219, 19),
            new Color(36, 48, 255),
            new Color(102, 0, 146),
            new Color(200, 0, 249)
    };

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
        ArrayList<Future> futureList = new ArrayList<>();
        for (int pixelY = 0; pixelY < pixelHeight; pixelY++) {
            futureList.add(executorService.submit(new Task(this, pixelY, pixels)));
        }
        for (Future future : futureList) {
            try {
                future.get();
            } catch (Exception exp) {
                System.out.println("Exception thrown : " + exp.getMessage());
            }
        }
        createImage();
    }

    public void drawMandelbrotIterationsLinesFromCurrentPoint(BufferedImage image, Graphics2D graphics2D) {
        if (currentMandelbrotPoint == null) {
            return;
        }
        Dimension dimension = this.getSize();
        double x = (this.getMandelbrotWidth() * currentMandelbrotPoint.getX()) / dimension.getWidth() + this.getMandelbrotLeftCornerX();
        double y = this.getMandelbrotLeftCornerY() - (this.getMandelbrotHeight() * currentMandelbrotPoint.getY()) / dimension.getHeight();
        ArrayList<ComplexNumber> complexNumberList = calculateMandelbrotIterations(new ComplexNumber(x, y), FIRST_MANDELBROT_ITERATIONS_TO_STORE);
        ArrayList<Point> pointList = getPoints(complexNumberList);
        if (pointList == null) {
            return;
        }
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setStroke(new BasicStroke(LINE_THICKNESS));
        graphics2D.setColor(LINE_COLOR);
        CohenSutherlandClippingAlgorithm cohenSutherlandClippingAlgorithm = new CohenSutherlandClippingAlgorithm(0, 0, image.getWidth(null), image.getHeight(null));
        for (int i = 1; i < pointList.size(); i++) {
            Point p1 = pointList.get(i - 1), p2 = pointList.get(i);
            ArrayList<Point2D> points = cohenSutherlandClippingAlgorithm.cohenSutherlandClip(new Point2D.Double(p1.getX(), p1.getY()), new Point2D.Double(p2.getX(), p2.getY()));
            if (points == null) {
                continue;
            }
            graphics2D.drawLine((int) points.get(0).getX(), (int) points.get(0).getY(),
                    (int) points.get(1).getX(), (int) points.get(1).getY());
        }
    }

    public void drawSelectionSquare(BufferedImage image, Graphics2D graphics2D) {
        if (selectionSquareStartingPoint == null || selectionSquareEndingPoint == null) {
            return;
        }
        //Get top left corner
        Point topLeftCorner = getSelectionSquareTopLeftCorner();
        // get length of the square.
        int length = getSelectionSquareLength();
        graphics2D.setStroke(new BasicStroke(LINE_THICKNESS));
        graphics2D.setColor(LINE_COLOR);
        graphics2D.drawRect((int) topLeftCorner.getX(), (int) topLeftCorner.getY(), length, length);
    }

    public Point getSelectionSquareTopLeftCorner() {
        return new Point((int) Math.min(selectionSquareStartingPoint.getX(), selectionSquareEndingPoint.getX()),
                (int) Math.min(selectionSquareStartingPoint.getY(), selectionSquareEndingPoint.getY()));
    }

    public int getSelectionSquareLength() {
        double width, height;
        width = Math.abs(selectionSquareStartingPoint.getX() - selectionSquareEndingPoint.getX());
        height = Math.abs(selectionSquareStartingPoint.getY() - selectionSquareEndingPoint.getY());
        return (int) Math.max(width, height);
    }

    public ArrayList<Point> getPoints(ArrayList<ComplexNumber> list) {
        double lengthOfAPixelInMandelbrot = (mandelbrotWidth) / pixelWidth;
        double heightOfAPixelInMandelbrot = (mandelbrotHeight) / pixelHeight;
        ArrayList<Point> pointList = new ArrayList<>();
        for (int i = 0; i < list.size() && i < FIRST_MANDELBROT_ITERATIONS_TO_STORE; i++) {
            ComplexNumber c = list.get(i);
            pointList.add(new Point((int) (Math.abs(c.getReal() - mandelbrotLeftCornerX) / lengthOfAPixelInMandelbrot), (int) (Math.abs(c.getImaginary() - mandelbrotLeftCornerY) / heightOfAPixelInMandelbrot)));
        }
        return pointList;
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
                ArrayList<ComplexNumber> list = calculateMandelbrotIterations(new ComplexNumber(currentPixelX, currentPixelY), FIRST_MANDELBROT_ITERATIONS_TO_STORE);
                cindex = list.size() / 60;
                pixels[index++] = colorArray[cindex].getRGB();
                currentPixelX = currentPixelX + incrementXBy;
            }
            currentPixelY = currentPixelY - incrementYBy;
        }
        createImage();
    }

    public Color[] colorArray = {
            new Color(255, 255, 200),
            new Color(0, 0, 120),
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
            new Color(50, 0, 0)};


    /**
     * This function calculates the number of iterations required for a complex number to exceed a magnitude of 2. In the Mandelbrot set, numbers that consistently remain under this threshold after a given number of iterations are considered part of the set. Any complex number reaching a magnitude greater than 2 within the specified maximum iterations is deemed to escape to infinity and is not part of the Mandelbrot set. <br/>
     * Used to find whether the given complex number is in mandelbrot set or not
     *
     * @param x ComplexNumber
     * @return Number of iterations took to verify whether the point is in mandelbrot set or not.
     **/
    private ArrayList<ComplexNumber> calculateMandelbrotIterations(ComplexNumber x, Integer maximumIterations) {
        int iterations = 0;
        ComplexNumber z = x;
        ArrayList<ComplexNumber> list = new ArrayList<>();
        list.add(x);
        while (iterations < maximumIterations) {
            if (iterations == 0) {
                // this is the first iterations, therefore just check the magnitude of c
                if (z.getMagnitude() > 2) {
                    break;
                }
                z = ComplexNumber.add(ComplexNumber.multiply(ComplexNumber.multiply(z0, z0), z0), x);
            } else {
                if (z.getMagnitude() > 2) {
                    break;
                }
                z = ComplexNumber.add(ComplexNumber.multiply(z, z), x);
            }
            list.add(z);
            iterations++;
        }
        return list;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x, y, w, h;
        w = this.getWidth() - (this.getInsets().left + this.getInsets().right);
        h = this.getHeight() - (this.getInsets().top + this.getInsets().bottom);
        x = (int) g.getClipBounds().getX();
        y = (int) g.getClipBounds().getY();
        Graphics2D gg = (Graphics2D) g.create();
        gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        BufferedImage bufferedImage;
        bufferedImage = new BufferedImage(mandelbrotImage.getWidth(null), mandelbrotImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        graphics2D.drawImage(mandelbrotImage, 0, 0, null);
        drawMandelbrotIterationsLinesFromCurrentPoint(bufferedImage, graphics2D);
        drawSelectionSquare(bufferedImage, graphics2D);
        graphics2D.dispose();
        gg.drawImage(bufferedImage, x, y, w, h, 0, 0, bufferedImage.getWidth(null), bufferedImage.getHeight(null), null);
        if (this.isSelectMode()) {
            BasicStroke s = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            gg.setStroke(s);
            gg.setColor(new Color(255, 255, 255));
            gg.drawRect(sx, sy, sw, sh);
        }

        gg.dispose();
    }

    private void setListeners() {
        this.addMouseWheelListener((MouseWheelEvent e) -> {
            System.out.println("point : " + e.getPoint());
            System.out.println("e.getX: " + e.getX());
            System.out.println("e.getY: " + e.getY());
            System.out.println("e.getRotations: " + e.getWheelRotation());
            System.out.println("e.getPreciseWheelRotation: " + e.getPreciseWheelRotation());
        });
    }


    static class Task implements Runnable {
        int col;
        int[] pixels;
        MandelbrotComponent mandelbrotComponent;

        Task(MandelbrotComponent component, int col, int[] pixels) {
            this.mandelbrotComponent = component;
            this.col = col;
            this.pixels = pixels;
        }

        @Override
        public void run() {

            int iterationColorRatio = (int) Math.ceil(((double) mandelbrotComponent.getMaxIterations()) / mandelbrotComponent.colorArray.length);
            double lengthOfAPixelInMandelbrot = (mandelbrotComponent.getMandelbrotWidth()) / mandelbrotComponent.getPixelWidth();
            double heightOfAPixelInMandelbrot = (mandelbrotComponent.getMandelbrotHeight()) / mandelbrotComponent.getPixelHeight();
            int pixelY = col;
            for (int pixelX = 0; pixelX < mandelbrotComponent.getPixelWidth(); pixelX++) {
                // Now get what point current pixel represents in Mandelbrot image
                double x, y;
                x = lengthOfAPixelInMandelbrot * pixelX + mandelbrotComponent.getMandelbrotLeftCornerX();
                y = mandelbrotComponent.getMandelbrotLeftCornerY() - heightOfAPixelInMandelbrot * pixelY;
                ArrayList<ComplexNumber> list = mandelbrotComponent.calculateMandelbrotIterations(new ComplexNumber(x, y), mandelbrotComponent.getMaxIterations());
                ComplexNumber zn = list.get(list.size() - 1);
                int iterationsTookToEscape = list.size();
                try {
                    Color color = null;
                    //Color.getHSBColor(((float) iterationsTookToEscape) / this.maxIterations, 1.0f, 1.0f);
                    if (iterationsTookToEscape >= mandelbrotComponent.getMaxIterations()) {
                        color = Color.BLACK;
                    } else {
                        // nsmooth := n + 1 - Math.log(Math.log(zn.abs()))/Math.log(2)
                        float nsmooth = iterationsTookToEscape + 1 - (float) (Math.log(Math.log(Math.sqrt(zn.getReal() * zn.getReal() + zn.getImaginary() * zn.getImaginary()))) / Math.log(2));
                        // Color.HSBtoRGB(0.95f + 10 * smoothcolor ,0.6f,1.0f);
                        //color = Color.getHSBColor(0.95f + 10 * nsmooth, 0.6f, 1.0f);
                        color = mandelbrotComponent.colors[iterationsTookToEscape % mandelbrotComponent.colors.length];
                        float[] dist = {0.0f, 0.2f, 1.0f};
                        Point2D start = new Point2D.Float(0, 0);
                        Point2D end = new Point2D.Float(50, 50);
                        Color[] colors2 = {Color.RED, Color.WHITE, Color.BLUE};
                        LinearGradientPaint p = new LinearGradientPaint(start, end, dist, colors2);

                        int maxIterations= mandelbrotComponent.getMaxIterations();
                        //color = new Color(iterationsTookToEscape*255/maxIterations, iterationsTookToEscape*255/maxIterations, iterationsTookToEscape*255/maxIterations);
                        //color = Color.getHSBColor((float) iterationsTookToEscape / maxIterations, 1.0f, 1.0f);
                        color = mandelbrotComponent.getMandelbrotColor().getColor(iterationsTookToEscape);
                    }
                    pixels[pixelY * mandelbrotComponent.getPixelWidth() + pixelX] = color.getRGB();
                } catch (Exception exp) {
                    System.out.println("iterations : " + iterationsTookToEscape);
                    System.out.println("iterationColorRatio : " + iterationColorRatio);
                    System.out.println("colorArray.length: " + mandelbrotComponent.colorArray.length);
                    exp.printStackTrace();
                    throw new RuntimeException("Exception: " + exp.getMessage());
                }
            }
        }
    }
}




