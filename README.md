# Java Mandelbrot Approximator

Program used to visually approximate fractals in the Mandelbrot set using complex numbers.


## Installation



## Application


### Preview


## Documentation
The program consists of two class files `MandelBrot.java`, `Complex.java` and an external library `StdDraw.java`.

### Class `Complex.java`
The `Complex.java` class describes a complex number and contains two private class fields, where `re` is the real coefficient and `im` the imaginary coefficient.
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

For arithmetics, the class contains three methods: `abs()`, `plus (Complex other)` and `times (Complex other)`. These are all simple adaptations of the mathematical operations.

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

The last method in the `Complex` class is the `toString()` method, which returns the values of the complex number in cartesian form $a + b \, i$.

```Java
@Override
public String toString() {
    return this.re + " + " + this.im + " i";
}
```

### Class `MandelBrot.java`



## Libraries
The program makes use of the Princeton University Standard Library `StdDraw.java`. The documentation for the library can be accessed [here](https://introcs.cs.princeton.edu/java/stdlib/StdDraw.java.html).

