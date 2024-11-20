# Java Mandelbrot Approximator

Program used to visually approximate fractals in the Mandelbrot set using complex numbers.


## Installation



## Application


### Preview


## Documentation
The program consists of two class files `MandelBrot.java`, `Complex.java` and an external library `StdDraw.java`.

### Class file `Complex.java`
The `Complex.java` class describes a complex number and contains two private class fields, where `re` is the real coefficient and `im` is the imaginary coefficient.
```Java
private double re, im;
```

The class contains three different constructors, taking either no argument, two doubles `re` and `im` as the argument or another `Complex` instance as the argument. When called using two doubles or another `Complex`, the values `re` and `im` are set as respectively the arguments `re` and `im`, or the `re`- and `im`-values of the `Complex` instance argument.

```Java
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
```

To get the values `re` and `im` from a `Complex` instance, the methods `getRe()` and `getIm()` methods are used, which simply return respectively `re` and `im`.
```Java
public double getRe() {
    return re;
}

public double getIm() {
    return im;
}
```

For arithmetics, the class contains three methods: `abs()`, `plus (Complex other)` and `times (Complex other)`. These are all simple adaptations of the mathematical operations for the absolute value of a complex number, sum of two complex numbers and product of two complex numbers.

1. $\text{abs} (C) = \text{abs} (a + b \ i) = \sqrt{a^2 + b^2}$.
2. $C_1 + C_2 = (a + b \ i) + (c + d \ i) = (a + c) + (b + d) \ i$.
3. $C_1 \times C_2 = (a + b \ i) \ (c + d \ i) = (a \ c - b \ d) + (b \ c + a \ d) \ i$.

```Java
public double abs () {
    return Math.sqrt(Math.pow(this.re, 2.0) + Math.pow(this.im, 2.0) );
}

public Complex plus (Complex other) {
    return new Complex(this.re + other.re, this.im + other.im);
}

public Complex times (Complex other) {
    double t_re = this.re * other.re - this.im * other.im;
    double t_im = this.im * other.re + this.re * other.im;
    return new Complex(t_re, t_im);
}
```

The last method in the `Complex` class is the `toString()` method, which returns the complex number represented in cartesian form $a + b \ i$.

```Java
@Override
public String toString() {
    return this.re + " + " + this.im + " i";
}
```

### Class file `MandelBrot.java`
Three private constant class fields are defined: `MAX`, `GRIDSIZE` and `COLOURS_PATH`. Where `MAX` is the maximum number of iterations that will be done in the `iterate()` method. By default `MAX` set to 256. `GRIDSIZE` defines the number of points the grid should be split into. As such the grid will be split into `GRIDSIZE` $\times$ `GRIDSIZE` matrix of points. By default `GRIDSIZE` is set to 512. The last field `COLOURS_PATH` is a String containing the local path to the .mnd file containing the colours scheme for the render. By default `COLOURS_PATH` is set to "mnd/mandel.mnd". All of these fields can be tweaked to produce varying results.
```Java
private static final int MAX = 256;
private static final int GRIDSIZE = 512;
private static final String COLOURS_PATH = "mnd/mandel.mnd";
```

Three more private class fields `sidelength`, `center` and `grid` are declared. The `Complex` field `center` acts as a centerpoint for where we view the fractal, while the `double` field `sidelength` acts as the horizontal and vertical sidelengths of the square we view the fractal in. A twodimensional list (i.e. a matrix) of `Complex` instances is declared with size `GRIDSIZE` $\times$ `GRIDSIZE`.
```Java
private static double sidelength;
private static Complex center;
private static Complex[][] grid = new Complex[GRIDSIZE][GRIDSIZE];
```




## Libraries
The program makes use of the [Princeton University Standard Library](https://introcs.cs.princeton.edu/java/stdlib/) `StdDraw.java`. The documentation for the library can be accessed [here](https://introcs.cs.princeton.edu/java/stdlib/StdDraw.java.html).

