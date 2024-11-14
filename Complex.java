public class Complex {
    private double re, im;

    public Complex () {
        this(0,0);
    }

    public Complex (double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex (Complex z) {
        this.re = z.re;
        this.im = z.im;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    public double abs () {
        // abs(a + b i) = sqrt(a^2 + b^2).
        return Math.sqrt(Math.pow(this.re, 2.0) + Math.pow(this.im, 2.0) );
    }

    public Complex plus (Complex other) {
        // (a + b i) + (c + d i) = (a + c) + (b + d) i
        return new Complex(this.re + other.re, this.im + other.im);
    }

    public Complex times (Complex other) {
        // (a + b i) * (c + d i) = (ac - bd) * (bc + ad) i
        double t_re = this.re * other.re - this.im * other.im;
        double t_im = this.im * other.re + this.re * other.im;
        return new Complex(t_re, t_im);
    }

    @Override
    public String toString() {
        return this.re + " + " + this.im + " i";
    }
}