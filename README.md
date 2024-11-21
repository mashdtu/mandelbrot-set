# Java Mandelbrot Approximator

Program used to visually approximate fractals in the Mandelbrot set using complex numbers.


## Problem
The Mandelbrot set cannot be described using a single mathematical equation. It is instead defined by an algorithm and should thus be calculated primarily using a computer. The rule to determine if a complex number $z_0$ lies within the Mandelbrot set is as follows.

$$
    z_1 = (z_0)^2 + z_0
$$
$$
    z_2 = (z_1)^2 + z_0
$$
$$
    z_3 = (z_2)^2 + z_0
$$
$$
    z_4 = (z_3)^2 + z_0
$$
$$
    \vdots
$$

It can be said that a complex number $z_0$ is **not** contained in the Mandelbrot set, if the sequence $|z_1|$, $|z_2|$, $|z_3|$, $|z_4|$, $\dots$ goes towards infinity. I.e. if the numbers keep increasing. It can be assumed, that if any $|z_n| > 2$, the sequence **will always** keep increasing, meaning that the complex number $z_0$ is **not** contained in the set. If the absolute values of none of the numbers in the sequence ever exceed 2, the original number $z_0$ *likely* is contained in the set. Using this definition a number $k$ can be decided, such that the algorithm gives a sequence of size $k - 1$, i.e. the sequence goes from $z_0$ to $z_k$. The number $k$ then represents the likelyhood that any $z_0$ is contained by the set.

1. How can a Java class be written to represent a complex number $z_0$.

2. How can this algorithm be implemented in a Java program to visually render the mandelbrot set in the complex number plane.

3. How can the program be expanded to generate multi-coloured renders, depending on how *likely* a complex number is to be contained in the set.

4. How can the colours be loaded from a preexisting .mnd file and applied to the render.


## Description


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

The `getCoordinateColour` method takes a `Color[] CS` and a `Complex C` as the arguments. Note that `CS` stands for Colour Scheme. A `double range` is defined as the ratio between the class constant `MAX` and the length of the argument `CS`, i.e. `CS.length`. This value is used to map return values of the `iterate` method to colours, regardless of how many colours are given in the `.mnd` colour scheme file. An `int iteratorValue` is defined as the return value from the `iterate` method called with `C` as the argument.
```Java
double range = (double) (MAX) / (double) CS.length;
int iteratorValue = iterate(C);
```
A loop is then started with an index `i` spanning from 1 to `CS.length`. For each value `i`, if `iteratorValue` is less than `i` $\times$ `range`, i.e. if `iteratorValue` falls within the `i`$^\text{th}$ index of the colour palette, the index `iteratorValue` of the colour scheme `CS` is returned. 1 is subtracted from `iteratorValue` to avoid having a case where `i` $= 0$, for which the statement (`i` $\times$ `range`) > `iteratorValue` would always return false. Due to the subtraction of 1 from `iteratorValue`, `i` $\times$ `range` may never exceede `iteratorValue` in cases where `MAX` is returned from the `iterate` method. To fix this issue the last element in `CS` is returned, i.e. `CS[CS.length-1]`, if `iteratorValue` exceeds (`CS.length` $- 1$) $\times$ `range`.
```Java
for (int i = 1; i < CS.length; i++) {
    if (i * range > iteratorValue) {
        return CS[i-1];
    }
}

return CS[CS.length - 1];
```

The `draw` method takes a `Complex[][] G` as the argument, and uses the external `StdDraw.java` library to create a visual representation of the calculations, which ends sketching out an appoximation of the fractal shape. The method begins by clearing any preexisting objects on the canvas and then defining a new `Color[] colourScheme` as the return value of the `getColourScheme` method with the class constant `COLOURS_PATH` as the argument.
```Java
StdDraw.clear();
Color[] colourScheme = getColourScheme(COLOURS_PATH);
```

The following code to render out the canvas is wrapped around two `StdDraw.show(0)` commands to make the entire render show on the canvas at once, which radically speeds up processing time. The actual code to render out the approximation consists of two for loops of respectively `x` and `y`, where they both span from 0 to the class constant `GRIDSIZE`. For each repitition of the inner loop, the pen colour for the `StdDraw.java` library is set using the `StdDraw.setPenColor` method with the return value of `getCoordinateColour(colourScheme, G[x][y])` as the argument. The point $(x, y)$ is now filled using the new pen colour.
```Java
StdDraw.show(0);

for (int x = 0; x < GRIDSIZE; x++) {
    for (int y = 0; y < GRIDSIZE; y++) {
        StdDraw.setPenColor(getCoordinateColour(colourScheme, G[x][y]));
        StdDraw.point(x, y);
    }
}

StdDraw.show(0);
```

The last non-`main` method is the `determineMatrixCoordinates` method, which determines a twodimensional matrix of the complex coordinates of each point in the grid. The method does not take any arguments. First a `Complex[][] coordinates` dummy matrix is defined with size `GRIDSIZE` $\times$ `GRIDSIZE`. A twodimensional loop is then started for `x` and `y`, spanning from 0 to `GRIDSIZE`. Two `double` values `x0` and `y0` are then calculated using the expression, where $C$ is the `Complex center`, $S$ is the `double sidelength` and $G$ is the constant `int GRIDSIZE`.

$$
    \begin{pmatrix}
        x_0 \\ y_0
    \end{pmatrix}
    =
    \begin{pmatrix}
        \Re \ (C) - \frac{S}{2} + \frac{S \times x}{G - 1} \\
        \Im \ (C) - \frac{S}{2} + \frac{S \times y}{G - 1}
    \end{pmatrix}
$$

A complex number instantiated using `x0` and `y0` as the arguments, and written to the `coordinates` array at position `[x][y]`, and the loop is repeated until all the complex coordinates are written to `coordinates`. The new `coordinates` array is then returned.
```Java
Complex[][] coordinates = new Complex[GRIDSIZE][GRIDSIZE];

for (int x = 0; x < GRIDSIZE; x++) {
    for (int y = 0; y < GRIDSIZE; y++) {
        double x0 = center.getRe() - (double) sidelength / 2.0 + (double) (sidelength * x) / ((double) GRIDSIZE - 1);
        double y0 = center.getIm() - (double) sidelength / 2.0 + (double) (sidelength * y) / ((double) GRIDSIZE - 1);
        coordinates[x][y] = new Complex(x0, y0);
    }
}

return coordinates;
```

The `main` method itself has to be called with three numbers as the arguments, i.e. the program should be initiated with:
```bash
java Mandelbrot.java a b c
```

where $a$, $b$ and $c$ are decimal numbers representing the following.

- $a$ : The **real** part of the complex center point.
- $b$ : The **imaginary** part of the complex center point.
- $c$ : The sidelength of the visible square.

The `main` method starts by declaring a `double[] args_double` with size 3 and then, using a try-catch block, attempts to parse the `String[] args` given from the console as `double` values. I.e. type `String[]` is converted to type `double[]` and stored in `args_double`. If the conversion fails, the program notifices the user that there is an error with the given arguments, and that the program should be called with 3 numeric values before terminating with code -1.
```Java
double[] args_double = new double[3];

try {
    args_double = Arrays.stream(args).mapToDouble(Double::parseDouble).toArray();

} catch (Exception e) {
    System.out.println("Error: The program should be called with 3 numeric values.");
    System.exit(-1);
}
```

If the conversion suceedes the method continues, and the class field `center` is assigned a complex number instantiated using $a$ and $b$ as arguments.
```Java
center = new Complex(args_double[0], args_double[1]);
```

The method then checks, if $c$ is greater than 0. If $c$ is not greater than 0, the program notifies the user that the sidelength argument must be greater than 0, and the program is terminated with code -1. If $c$ is greater than 0, the class field `sidelength` is assigned the value of $c$. 
```Java
if (args_double[2] <= 0) {
    System.out.println("Error: The sidelength argument must be greater than 0.");
    System.exit(-1);
}

sidelength = args_double[2];
```

The scales and pen radius used by the `StdDraw.java` library are set using the void methods `setXscale`, `setYscale` and `setPenRadius` from the library.
```Java
StdDraw.setXscale(0, GRIDSIZE);
StdDraw.setYscale(0, GRIDSIZE);
StdDraw.setPenRadius(0.8/(double)GRIDSIZE);
```

The class field `grid` is then assigned the return value from calling the `determineMatrixCoordinates` method, and finally the `draw` method is called with `grid` as the argument to generate the final render.
```Java
grid = determineMatrixCoordinates();
draw(grid);
```

Furthermore, the class makes use of the following packages.
```Java
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
```



## Efficiency

### Memory usage

To determine the program's memory usage the `Runtime.getRuntime().totalMemory` method is used. As the method returns a `long` value equal to the amount of bits of memory the program has used. To convert the memory to megabytes (MB), the `long` value is divided by 1,000,000. As such, to measure the memory usage the following command is added on the end of the `main` method.
```Java
System.out.println("Memory usage: " + (double) (Runtime.getRuntime().totalMemory())/1000000 + " MB.");
```

By running the program 10 times using the starting arguments `-0.5 0 2`, a decent approximation of the program's average memory usage for the arguments `-0.5 0 2` can be determined. Note that all measurements are made with the default values `MAX = 256`, `GRIDSIZE = 512` and with the colour scheme `mandel.mnd`.
```
Memory usage: 318.767104 MB
Memory usage: 444.596224 MB
Memory usage: 402.653184 MB
Memory usage: 385.875968 MB
Memory usage: 419.4304 MB
Memory usage: 408.94464 MB
Memory usage: 322.961408 MB
Memory usage: 402.653184 MB
Memory usage: 421.527552 MB
Memory usage: 408.94464 MB
```
The mean memory usage is calculated to be 393.635 MB for the arguments `-0.5 0 2`. Note that the memory usage can vary, depending on what arguments `Mandelbrot.java` is called with. As an example the memory usage can be calculated when the program is called with arguments `-1 0.3 0.05`.
```
Memory usage: 373.293056 MB
Memory usage: 358.612992 MB
Memory usage: 392.167424 MB
Memory usage: 394.264576 MB
Memory usage: 318.767104 MB
Memory usage: 383.778816 MB
Memory usage: 373.293056 MB
Memory usage: 318.767104 MB
Memory usage: 371.195904 MB
Memory usage: 387.973120 MB
```
The mean mamory usage is calculated to be 367.211 MB when the program is initiated with the arguments `-1 0.3 0.05`. Initiating the program with the arguments `-100 0 2` (or any other square which lies completely outside the fractal), results in a memory usage output of exactly **264.241152 MB** every time, this is the minimum memory usage for the program. On the other hand, if the square lies completely within the fractal, for instance with arguments `-0.2 0 0.0001`, the memory usage is at a maximum. The mean memory usage for arguments `-0.2 0 0.0001` is determined by running the program 10 times.
```
Memory usage: 685.768704 MB
Memory usage: 694.157312 MB
Memory usage: 765.46048 MB
Memory usage: 664.797184 MB
Memory usage: 639.63136 MB
Memory usage: 681.5744 MB
Memory usage: 715.128832 MB
Memory usage: 731.906048 MB
Memory usage: 704.643072 MB
Memory usage: 725.614592 MB
```
The mean memory usage is calculated to be 700.868 MB. This means that the general memory usage is usually somewhere between 260 MB and 700 MB depending on the arguments the program is initiated with.


### Runtime

The program runtime $P$ can be measured using the `System.nanoTime` method, and saving the return values before and after the process. To do this two `long` variables are declared: `t1` and `t2`. First `t1` is set equal to the return value of `System.nanoTime` before the process is started. The program is then run, and `t2` is set equal to the `System.nanoTime` return value at the time the program finishes. The runtime $P$ can now be determined as the difference between `t1` and `t2`, i.e. $P = \Delta t = $ `t2` $-$ `t1`. Note that the `System.nanoTime` method returns a long value in the form of nanoseconds - not milliseconds - for this reason $\Delta t$ has to be divided by $10^6$ or 1,000,000, in order to store the runtime in milliseconds. As such method of determining the runtime is as follows:
```Java
long t1, t2;
t1 = System.nanoTime();

...
// Process code
...

t2 = System.nanoTime();

System.out.println("Execution time: " + (t2 - t1) / 1000000 + "ms");
```

Using this method, the runtime for the program when initiated with arguments `-0.5 0 2` can be determined. Note that the program is first compiled with the command `javac Mandelbrot.java` and then run with the command `java Mandelbrot -0.5 0 2`.
```
Execution time: 541ms
Execution time: 668ms
Execution time: 575ms
Execution time: 870ms
Execution time: 837ms
Execution time: 784ms
Execution time: 859ms
Execution time: 727ms
Execution time: 690ms
Execution time: 571ms
```

The mean runtime is calculated to be 712.2 ms. The mean runtime is then calculated when called with the arguments `-100 0 2`, which lies completely outside the fractal.
```
Execution time: 337ms
Execution time: 401ms
Execution time: 435ms
Execution time: 495ms
Execution time: 425ms
Execution time: 426ms
Execution time: 442ms
Execution time: 407ms
Execution time: 373ms
Execution time: 422ms
```

The mean runtime is calculated to be 416.3 ms. Lastly the runtime for a square completely inside the fractal is calculated., to do this the arguments `-0.2 0 0.0001` are used.
```
Execution time: 828ms
Execution time: 851ms
Execution time: 936ms
Execution time: 1408ms
Execution time: 1471ms
Execution time: 1426ms
Execution time: 1264ms
Execution time: 1271ms
Execution time: 1371ms
Execution time: 1235ms
```

The mean runtime is calculated to be 1206.1 ms. As such the general runtime will usually be between 400 ms and 1400 ms depending on the arguments the program is initiated with.



## Libraries
The program makes use of the external [Princeton University Standard Library](https://introcs.cs.princeton.edu/java/stdlib/) `StdDraw.java`. The documentation for the library can be accessed [here](https://introcs.cs.princeton.edu/java/stdlib/StdDraw.java.html).

