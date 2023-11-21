import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.MemoryImageSource;

public class MandelbrotComponent extends JComponent {
    int width, height;
    public static int ITERATIONS = 500;
    double mandelbrotFrameLeftCornerX, mandelbrotFrameLeftCornerY, mandelbrotFrameCenterX, mandelbrotFrameCenterY, mandelbrotFrameWidth, mandelbrotFrameHeight;
    /* mandelbrotFrameWidth and mandelbrotFrameHeight represents width and height of the graph in real numbers */
    int sx, sy, sw, sh;
    Image mandlebrotimage;
    int[] pixels;
    boolean selectMode;
    static BoxMover mover;

    static {
        mover = new BoxMover();
    }

    public void setSelectMode(boolean selectMode) {
        this.selectMode = selectMode;
    }

    public boolean isSelectMode() {
        return selectMode;
    }

    public MandelbrotComponent(int width, int height) {
        this.width = width;
        this.height = height;
        selectMode = false;
        this.setSize(width, height);
        this.setPreferredSize(new Dimension(width, height));
        this.setMaximumSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));
        // Creating a mandelbrot image
        pixels = new int[width * height];
        // initial parameters
        mandelbrotFrameLeftCornerX = -2;
        mandelbrotFrameLeftCornerY = 2;
        mandelbrotFrameWidth = 4;
        mandelbrotFrameHeight = 4;
        mandelbrotFrameCenterX = 0;
        mandelbrotFrameCenterY = 0;
        sw = width / 8;
        sh = height / 8;
        setPixels();
        MemoryImageSource mis = new MemoryImageSource(width, height, pixels, 0, width);
        mandlebrotimage = createImage(mis);
        this.addKeyListener(mover);
    }

    private void setPixels() {
        double incrementXBy, incrementYBy;
        incrementXBy = mandelbrotFrameWidth / width;
        incrementYBy = mandelbrotFrameHeight / height;
        int index = 0;
        double currentPixelX, currentPixelY;
        currentPixelY = mandelbrotFrameLeftCornerY;
        int cindex = 0;
        for (int y = 0; y < height; y++) {
            currentPixelX = mandelbrotFrameLeftCornerX;
            for (int x = 0; x < width; x++) {
                int itr = mandlebrottest(currentPixelX, currentPixelY);
                cindex = itr / 60;
                pixels[index++] = colorArray[cindex].getRGB();
                currentPixelX = currentPixelX + incrementXBy;
            }
            currentPixelY = currentPixelY - incrementYBy;
        }
        System.out.println("Pixels set successfully");
    }

    Color[] colorArray = {new Color(0, 0, 120), new Color(0, 0, 255), new Color(0, 120, 0), new Color(0, 200, 0), new Color(0, 200, 200), new Color(20, 200, 0), new Color(200, 0, 0), new Color(120, 0, 0), new Color(50, 0, 0), new Color(0, 0, 0)};

    private int mandlebrottest(double a, double b) {
        Complex z = new Complex(a, b);
        Complex c = new Complex(a, b);
        int itr = 0;
        double re, img;
        while (itr < ITERATIONS) {
            re = z.getReal();
            img = z.getImaginary();
            if (re * re + img * img < 4) {
                z.setReal(re * re - img * img + a);
                z.setImaginary(2 * re * img + b);
            } else {
                break;
            }
            itr++;
        }
        return itr;
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

        gg.drawImage(mandlebrotimage, x, y, w, h, 0, 0, mandlebrotimage.getWidth(null), mandlebrotimage.getHeight(null), null);
        if (this.isSelectMode()) {
            BasicStroke s = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            gg.setStroke(s);
            gg.setColor(new Color(255, 255, 255));
            gg.drawRect(sx, sy, sw, sh);
        }
        gg.dispose();
        System.out.println("Image Drawned");
    }

    // creating a zooming facility
    static class BoxMover implements KeyListener {
        public BoxMover() {
            System.out.println("BoxMover object Created");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("Key Pressed");

            if (!(e.getSource() instanceof MandelbrotComponent))
                return;
            MandelbrotComponent comp = (MandelbrotComponent) e.getSource();
            System.out.println("key char :" + e.getKeyChar());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            System.out.println("Key Released");
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // If the typed key are space then show the respective component
            System.out.println("KeyTyped");
        }


        static class MoverThread extends Thread {
            MandelbrotComponent comp;
            public static int LEFT = 1;
            public static int RIGHT = 2;
            public static int TOP = 3;
            public static int BOTTOM = 4;
            boolean pressed;
            int move;

            public MoverThread(MandelbrotComponent comp) {
                super("MoverThread");
                this.comp = comp;
                pressed = false;
            }

            public void setPressed(boolean pressed) {
                this.pressed = pressed;
            }

            public boolean isPressed() {
                return pressed;
            }

            public void setMove(int move) {
                this.move = move;
            }

            public int getMove() {
                return move;
            }

            @Override
            public void run() {
                super.run();
                System.out.println("In the run method");
                while (pressed) {
                    if (this.getMove() == LEFT) {
                        comp.sx = comp.sx - 1;
                    } else if (this.getMove() == RIGHT)
                        comp.sx = comp.sx + 1;
                    else if (this.getMove() == TOP)
                        comp.sy = comp.sy - 1;
                    else if (this.getMove() == BOTTOM)
                        comp.sy = comp.sy + 1;
                    else
                        break;
                    try {
                        Thread.currentThread().sleep(200);
                    } catch (Exception e) {
                        System.out.println("Excpetion cuaght : " + e.getMessage());
                    }
                }
            }
        }
    }
}
 class MyKeyListener implements KeyListener{
    @Override
    public void keyReleased(KeyEvent e) {
        if ( ! (e.getSource() instanceof MandelbrotComponent) )
            return;
        MandelbrotComponent comp  = (MandelbrotComponent)e.getSource();


    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ( !(e.getSource() instanceof MandelbrotComponent))
            return;
        MandelbrotComponent comp = (MandelbrotComponent)e.getSource();
        MoveThread thread;
        char c = e.getKeyChar();
        if ( Character.toUpperCase(c) == KeyEvent.VK_J){
            System.out.println("Up key pressed");
            if ( !comp.isSelectMode())
                return;
            thread = new MoveThread((MandelbrotComponent)e.getSource() , MoveThread.MOVETOLEFT);
            thread.start();
        }
        else if ( Character.toUpperCase(c)  == KeyEvent.VK_K){
            System.out.println("Down Arrow Key Pressed.");
            if ( !(comp.isSelectMode()))
                return;
            thread = new MoveThread( comp, MoveThread.MOVETOBOTTOM);
            thread.start();
        }
        else if ( Character.toUpperCase(c) == KeyEvent.VK_L){
            if ( !comp.isSelectMode())
                return;
            thread = new MoveThread(comp, MoveThread.MOVETORIGHT);
            thread.start();
        }
        else if ( Character.toUpperCase(c) == KeyEvent.VK_I) {
            if ( !comp.isSelectMode())
                return;
            thread = new MoveThread(comp, MoveThread.MOVETOTOP);
            thread.start();
        }
        else if ( Character.toUpperCase(c) == KeyEvent.VK_SPACE){
            System.out.println("Space Pressed.");
            comp.setSelectMode( !comp.isSelectMode());
        }


    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
 class MoveThread extends Thread{
    public static final   int MOVETOLEFT = 1;
    public static  final int MOVETORIGHT = 2;
    public static final int MOVETOTOP = 3;
    public static final int MOVETOBOTTOM = 4;
    private MandelbrotComponent comp ;
    private boolean move;
    private int direction;
    public MoveThread(MandelbrotComponent comp, int direction ){
        this.comp = comp;
        this.direction = direction;
        move = true;
    }

    @Override
    public void run() {
        super.run();
        // System.out.println("Thread called");
        if( !(comp instanceof MandelbrotComponent))
            return;
        int left, right, top , bottom;
        left = comp.getInsets().top;
        right = comp.getInsets().right;
        top = comp.getInsets().top;
        bottom = comp.getInsets().bottom;

        do{
            switch( direction ){
                case MoveThread.MOVETOLEFT :
                    if ( comp.sx > left ){
                        comp.sx = comp.sx-1;
                    }
                    else{
                        break;
                    }
                case MoveThread.MOVETORIGHT :
                    if ( comp.sx < comp.width - right -comp.sw){
                        comp.sx = comp.sx + 1;
                    }
                    else{
                        break;
                    }
                case MoveThread.MOVETOTOP :
                    if ( comp.sy > top ){
                        comp.sy = comp.sy -1;
                    }
                    else
                        break;
                case MoveThread.MOVETOBOTTOM :
                    if ( comp.sy < comp.height - comp.sh - bottom){
                        comp.sy = comp.sy = comp.sy +1 ;
                    }
                    else{
                        break;
                    }
                default :
                    System.out.println("do nothing ");
                    break;
            }
        }while(this.isMove());
        System.out.println("Taking exit from move Thread");
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public boolean isMove() {
        return move;
    }
}
class Complex{
    double re,img;
    public Complex(double re, double img){
        this.re = re;
        this.img = img;
    }

    public double getReal() {
        return re;
    }

    public double getImaginary() {
        return img;
    }

    public void setReal(double re) {
        this.re = re;
    }

    public void setImaginary(double img) {
        this.img = img;
    }

}