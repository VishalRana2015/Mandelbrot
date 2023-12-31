public class ComplexNumber {
    double re, img;

    public ComplexNumber(double re, double img) {
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

    public double getMagnitude() {
        return Math.sqrt(this.re * this.re + this.img * this.img);
    }

    public static ComplexNumber multiply(ComplexNumber a, ComplexNumber b) {
        double real, imaginary;
        real = a.getReal() * b.getReal() - a.getImaginary() * b.getImaginary();
        imaginary = a.getReal() * b.getImaginary() + a.getImaginary() * b.getReal();
        return new ComplexNumber(real, imaginary);
    }

    public static ComplexNumber add(ComplexNumber a, ComplexNumber b) {
        double real, imaginary;
        real = a.getReal() + b.getReal();
        imaginary = a.getImaginary() + b.getImaginary();
        return new ComplexNumber(real, imaginary);
    }

    @Override
    public String toString() {
        String s = this.re + "";
        if ( this.img < 0){
            s += "-" + "i" + Math.abs(this.img);
        }
        else{
            s += "+i" + this.img;
        }
        return s;
    }
}