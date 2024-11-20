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

1. $\ \ \text{abs} (C) = \text{abs} (a + b \ i) = \sqrt{a^2 + b^2}$.
2. $\ \ C_1 + C_2 = (a + b \ i) + (c + d \ i) = (a + c) + (b + d) \ i$.
3. $\ \ C_1 \times C_2 = (a + b \ i) \ (c + d \ i) = (a \ c - b \ d) + (b \ c + a \ d) \ i$.

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

Besides the public `main(String[] args)` method, the `MandelBrot.java` class contains five other private methods: `determineMatrixCoordinates()`, `draw (Complex[][] G)`, `getCoordinateColour (Color[] CS, Complex C)`, `getColourScheme (String path)` and `iterate (Complex z0)`. For the sake of simplicity, the methods will presented such that no method calls one that has not been documented.

The `iterate` method takes a `Complex z0` as the argument, and is an adaptation of the following mathematical algorithm, which returns the index $i$ of whichever absolute value of $z_i$ exceeds 2.0. If no absolute value of $z$ exceeds 2.0 the method returns `MAX`, i.e. the greatest/last possible index of $z$.

- $\ \ z_1 = z_0 \times z_0 + z_0$.
- $\ \ z_2 = z_1 \times z_1 + z_0$.
- $\ \ z_3 = z_2 \times z_2 + z_0$.
- $\ \ \dots$
- $\ \ z_{\text{MAX}} = z_{\text{MAX} - 1} \times z_{\text{MAX} - 1} + z_0$.

```Java
private static int iterate (Complex z0) {
    Complex z = new Complex(z0);

    for (int i = 0; i < MAX; i++) {
        if (z.abs() > 2.0) {
            return i;
        }
        
        z = z.times(z).plus(z0);
    }

    return MAX;
}
```

The `getColourScheme` method takes a `String path` as the argument. First the method checks if the length of `path` is 0, i.e. if `path` is an empty string. If this is the case, the program notifies the user that the colour scheme path is empty and terminates with code -1.
```Java
if (path.length() == 0) {
    System.out.println("Error: Colour scheme path is empty.");
    System.exit(-1);
}
```
If `path` is not empty the program continues, and a `File` object `origin` is instantiated using `path` as the constructor argument. The method now checks if `origin` is an existing file. If it is not, the program notifies the user that the path could not be located. Another check is done, to determine if the user console is working in the `WindowsPowerShell` directory, which VSCode usually does. If this is the case then the local path does not exist in the working directory. If the current working directory is the `WindowsPowerShell` directory, the user is notified of this specifically. The program is then terminated with code -1.
```Java
if (!origin.exists() || origin.isDirectory()) {
    System.out.println("Error: Colour scheme for path \"" + origin.getAbsolutePath() + "\" could not be located.");

    if ("C:\\WINDOWS\\System32\\WindowsPowerShell\\v1.0".equals(System.getProperty("user.dir"))) {
        System.out.println("NOTE: The program is currently being executed from the \"" + System.getProperty("user.dir") + "\" working directory, which likely does not contain the mnd directory.");
    }
    System.exit(-1);
}
```

If the `origin` file **does** exist, two new `Scanner` objects `input` and `lineCounter` are defined using `origin` as the constructor argument. An integer `fileLength` is also declared and set equal to 0. The `lineCounter` `Scanner` is used to count the number of lines in the `origin` file using a for loop which iterates `fileLength` by 1 every time `lineCounter` has a next non-empty line. At the end the `Scanner lineCounter` is closed to avoid any potential resource leaks.
```Java
Scanner input = new Scanner(origin);

int fileLength = 0;
Scanner lineCounter = new Scanner(origin);

while (lineCounter.hasNext()) {
    fileLength += lineCounter.nextLine().length() != 0 ? 1 : 0;
}

lineCounter.close();
```

A new array of `Color` objects `colours` is declared with length `fileLength`. A loop is then started for an iterating index `i` which repeats spanning from 0 to `fileLength`. As such the loop is repeated for each line in the `origin` file. For each line in `origin`, the line itself is split into a `String[]` using the `String.split(" ")` to split at each whitespace, the resulting `String[]` is stored in a variable `rawLine`. Note that splitting at each whitespaces can cause `rawLine` to contain empty elements. To fix this a new `String[] rgbValues` is declared with length 3, along with an `int iterator` which is set to 0. A new loop is then started which repeats over each element `s` in `rawLine`. In the loop the method checks that `s` is not empty and that `iterator` less than 3. In this case `s` is written to `rgbValues` at position `iterator`, and `iterator` is iterated. In the case where `s` is an empty element, the loop simply skips to the next element in `rawLine`. Once this loop is completed, `rgbValues` now consists of three `String` values representing integers between 0 and 255. An `int[] rgbInt` is defined as the mapping of `rgbValues` using the `Integer::ParseInt` method, i.e. the array is converted from type `String[]` to type `int[]`. A new `Color` object is then defined using the values from `rgbInt` and is stored in the `colours` array at position `i`. The loop is then repeated. Once the loop is finished running, the `Scanner input` is closed to avoid any potential resource leaks and `colours` is returned.
```Java
Color[] colours = new Color[fileLength];

for (int i = 0; i < fileLength; i++) {
    String[] rawLine = input.nextLine().split(" ");

    String[] rgbValues = new String[3];
    int iterator = 0;

    for (String s : rawLine) {
        if (!"".equals(s) && iterator < 3) {
            rgbValues[iterator] = s;
            iterator ++;
        }
    }
    
    int[] rgbInt = Arrays.stream(rgbValues).mapToInt(Integer::parseInt).toArray();

    colours[i] = new Color(rgbInt[0], rgbInt[1], rgbInt[2]);
}

input.close();

return colours;
```




The `getCoordinateColour` method takes a `Color[] CS` and a `Complex C` as the arguments. 



## Libraries
The program makes use of the external [Princeton University Standard Library](https://introcs.cs.princeton.edu/java/stdlib/) `StdDraw.java`. The documentation for the library can be accessed [here](https://introcs.cs.princeton.edu/java/stdlib/StdDraw.java.html).

