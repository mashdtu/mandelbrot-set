public class Complex {
    private double re, im;

    public Complex () {
        // Default costructor for a complex number 0 + 0 i.
        this(0,0);
    }

    public Complex (double re, double im) {
        // Construct a complex number object from two decimal values.
        this.re = re;
        this.im = im;
    }

    public Complex (Complex z) {
        // Construct a complex number object from another complex number object.
        this.re = z.re;
        this.im = z.im;
    }

    public double getRe() {
        // Returns the real coefficient of the complex number.
        return re;
    }

    public double getIm() {
        // Returns the imaginary coefficient of the complex number.
        return im;
    }

    public double abs () {
        // Returns the absolute value of the complex number using the expression abs(a + b i) = sqrt(a^2 + b^2).
        return Math.sqrt(Math.pow(this.re, 2.0) + Math.pow(this.im, 2.0) );
    }

    public Complex plus (Complex other) {
        // Returns the summation of the complex number and another complex number taken as the argument.
        return new Complex(this.re + other.re, this.im + other.im);
    }

    public Complex times (Complex other) {
        // Returns the summation of the complex number and another complex number taken as the argument.
        double t_re = this.re * other.re - this.im * other.im;
        double t_im = this.im * other.re + this.re * other.im;
        return new Complex(t_re, t_im);
    }

    @Override
    public String toString() {
        // Returns the complex number represented in cartesian form.
        return this.re + " + " + this.im + " i";
    }
}