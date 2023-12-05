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
        imaginary = a.getReal() * b.getImaginary() + b.getImaginary() * b.getReal();
        return new ComplexNumber(real, imaginary);
    }

    public static ComplexNumber add(ComplexNumber a, ComplexNumber b) {
        double real, imaginary;
        real = a.getReal() + b.getReal();
        imaginary = a.getImaginary() + b.getImaginary();
        return new ComplexNumber(real, imaginary);
    }
}