import java.awt.*;

public class MandelbrotColor {


    private int paletteLength;
    private MandelbrotComponent mandelbrotComponent;
    private Color[] colorArray;
    MandelbrotColor(MandelbrotComponent mandelbrotComponent, Color[] colorArray) throws Exception{
        if (colorArray == null || colorArray.length < 1){
            throw new RuntimeException("There should be at least one color in the colorArray");
        }
        this.colorArray = colorArray;
        this.paletteLength = 500;
        this.mandelbrotComponent = mandelbrotComponent;
    }

    public void setPaletteLength(int paletteLength) {
        this.paletteLength = paletteLength;
    }

    public int getPaletteLength() {
        return paletteLength;
    }
    public Color getColor(int iterations){
        if ( iterations >= mandelbrotComponent.getMaxIterations()){
            return Color.BLACK;
        }
        double colorLength = (double)paletteLength/colorArray.length;
        iterations = iterations%paletteLength;
        int index = (int)(iterations/colorLength);
        return colorArray[index];
    }
}