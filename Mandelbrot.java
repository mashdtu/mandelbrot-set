import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Mandelbrot {
    private static final int MAX = 255;
    // Constant class field to determine how many itterations each point should make, higher value = higher accuracy and longer process time.

    private static final int GRIDSIZE = 512;
    // Constant class field determining how many fields the < sidelength > by < sidelength > matrix should be split into. 

    private static final String COLOURS_PATH = "mnd/volcano.mnd";
    // Constant class field acting as the path to the .mnd file containing the render colours.
    // NOTE: Make sure the program is run from the correct PATH in the console, otherwise the .mnd file path will not be found.
    // NOTE: The .mnd file can have any amount of colours, it does not need to be 256, the program will scale the colour scheme accordingly.

    private static double sidelength;
    // Class field for the sidelength of the fractal.

    private static Complex center;
    // Class field for the center-point of the fractal in the complex number plane.

    private static Complex[][] grid = new Complex[GRIDSIZE][GRIDSIZE];
    // Class field for the twodimensional grid of complex numbers found within the < sidelength > by < sidelength > matrix.


    public static void main(String[] args) throws FileNotFoundException {
        //long t1, t2;
        //Runtime runtime = Runtime.getRuntime();
        //long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        //t1 = System.nanoTime();



        if (args.length != 3) {
            System.out.println("Error: The program should be called with 3 numeric values.");
            // Print an error message if the program is called with a wrong amount of arguments.

            System.exit(-1);
            // Exit the program with code -1.
        }

        double[] args_double = new double[3];
        // Declare an array < args_double > with dimension 3.
        try {
            args_double = Arrays.stream(args).mapToDouble(Double::parseDouble).toArray();
            // Convert type String[] args to type double[] args_double.

        } catch (Exception e) {
            System.out.println("Error: The program should be called with 3 numeric values.");
            // Print an error message if the conversion fails.

            System.exit(-1);
            // Exit the program with code -1.
        }

        center = new Complex(args_double[0], args_double[1]);
        // Define a complex number which acts as the fractal's center-point in the complex number plane.

        if (args_double[2] <= 0) {
            // Test if the sidelength argument is less than or equal to 0, i.e. if the sidelength is negative or equal to 0.

            System.out.println("Error: The sidelength argument must be greater than 0.");
            // Print an error message if the sidelength is negative or equal to 0.

            System.exit(-1);
            // Exit the program with code -1.
        }

        sidelength = args_double[2];
        // Define a sidelength to generate the fractal.

        StdDraw.setXscale(0, GRIDSIZE);
        StdDraw.setYscale(0, GRIDSIZE);
        StdDraw.setPenRadius(0.8/(double)GRIDSIZE);
        // Set dimensions and pen radius for the stdDraw library for use in visually representing the grid.

        grid = determineMatrixCoordinates();
        // Determine the grid using the determineMatrixCoordinates() method.

        draw(grid);
        // Draw the points of the matrix which fall within the mandelbrot set, determined using the iterate() method.

        

        //t2 = System.nanoTime();
        //System.out.println("Memory usage: " + (double) (Runtime.getRuntime().totalMemory())/1000000 + " MB.");
        //System.out.println("Execution time: " + (t2 - t1) / 1000000 + "ms");
    }

    private static Complex[][] determineMatrixCoordinates () {
        Complex[][] coordinates = new Complex[GRIDSIZE][GRIDSIZE];
        // Define a dummy grid < coordinates > of size GRIDSIZE * GRIDSIZE to write to.

        for (int x = 0; x < GRIDSIZE; x++) {
            for (int y = 0; y < GRIDSIZE; y++) {
                // Iterate (x, y) over a twodimensional plane of size GRIDSIZE * GRIDSIZE. 

                double x0 = center.getRe() - (double) sidelength / 2.0 + (double) (sidelength * x) / ((double) GRIDSIZE - 1);
                double y0 = center.getIm() - (double) sidelength / 2.0 + (double) (sidelength * y) / ((double) GRIDSIZE - 1);
                // Using the expression as described in the documentation, the coordinates (x0 and y0) of the specific (x, y) point are determined.
                
                coordinates[x][y] = new Complex(x0, y0);
                // Write the value of the calculated coordinates to the grid as a complex number object.
            }
        }

        return coordinates;
        // Return the new rewritten dummy variable < coordinates >.
    }

    private static void draw (Complex[][] G) throws FileNotFoundException {
        StdDraw.clear();
        // Clear any preexisting objects on the StdDraw canvas.

        Color[] colourScheme = getColourScheme(COLOURS_PATH);
        // The colour scheme is defined as the return value of the getColourScheme method with the path of the .mnd file as the argument.

        StdDraw.show(0);
        // The code to render out the points is wrapped around two "StdDraw.show(0)" commands, which makes the StdDraw library render it all out before showing anything visually - then showing all the rendered points at once.

        for (int x = 0; x < GRIDSIZE; x++) {
            for (int y = 0; y < GRIDSIZE; y++) {
                StdDraw.setPenColor(getCoordinateColour(colourScheme, G[x][y]));
                StdDraw.point(x, y);
            }
        }

        StdDraw.show(0);
        // Ending wrapper-command around the code to show the final render.
    }

    private static Color getCoordinateColour (Color[] CS, Complex C) {
        // *CS = Colour Scheme

        double range = (double) (MAX) / (double) CS.length;
        // Determine a value range, equal to the ratio between the value MAX and the number of entires in the colour scheme.

        int iteratorValue = iterate(C);
        // Determine if C lies within the fractal using the iterate method, the numeric return value is used to colour the points based on how close algorithmically they are to being inside the fractal (the bigger the return value - the closer the point lies).

        for (int i = 1; i < CS.length; i++) {
            // For loop with iterator ranging from 1 to the index of the last colour in the colour scheme.

            if (i * range > iteratorValue) {
                return CS[i-1];
                // If the iterator * the range is greater than the returned value from the iterator, the index of the iterator minus one of the colour scheme is returned. One is subtracted from the iterator to avoid having a case where i = 0, for which the statement i * range > iteratorValue would always return false.
            }
        }

        return CS[CS.length - 1];
        // Due to the subtraction of the iterator by 1, i * range may never exceede iteratorValue in cases where MAX is returned from the iterate() method. To fix this issue, the last element in the colour scheme is returned, if the iteratorValue exceeds (CS.length - 1) * range.
    }
 
    private static Color[] getColourScheme (String path) throws FileNotFoundException {
        if (path.length() == 0) {
            // Test if the path argument is empty.

            System.out.println("Error: Colour scheme path is empty.");
            // Print an error message if the PATH is not found.

            System.exit(-1);
            // Exit the program with code -1.
        }

        File origin = new File(path);
        // A File object < origin > is instantiated using the path argument.

        if (!origin.exists() || origin.isDirectory()) {
            // Check if origin file either does not exist or is a directory.

            System.out.println("Error: Colour scheme for path \"" + origin.getAbsolutePath() + "\" could not be located.");
            // If the origin file does not exist, the colour scheme cannot be located.

            if ("C:\\WINDOWS\\System32\\WindowsPowerShell\\v1.0".equals(System.getProperty("user.dir"))) {
                // Check if the current working directory is the powershell directory.

                System.out.println("NOTE: The program is currently being executed from the \"" + System.getProperty("user.dir") + "\" working directory, which likely does not contain the mnd directory.");
                // If the program is being run from the powershell directory (as VSCode usually does), the mnd directory likely will not be found because it does not exist in the powershell directory.
                // The program should be run from the parent directory of the MandelBrot.java file to avoid errors in locating the .mnd colour scheme file.
            }
            System.exit(-1);
        }

        Scanner input = new Scanner(origin);
        // A Scanner object < input > is instantiated using the origin file.

        int fileLength = 0;
        Scanner lineCounter = new Scanner(origin);
        // An integer < fileLength > is declared and set equal to 0, and a new Scanner object < lineCounter > is instantiated with the same arguments as < input >. These two are used to count the number of lines in the .mnd file, such that the colour palette array can be declared using the correct dimensions.

        while (lineCounter.hasNext()) {
            fileLength += lineCounter.nextLine().length() != 0 ? 1 : 0;
            // The integer fileLength is iterated by 1, each time there is a non-empty line in the .mnd file.
        }

        lineCounter.close();
        // The < lineCounter > Scanner is closed to avoid any potential resource leaks.

        Color[] colours = new Color[fileLength];
        // The colour palette is declared as a array using fileLength (the no. of lines in the .mnd file) as the dimension.

        for (int i = 0; i < fileLength; i++) {
            // For an index i spanning from 0 to fileLength, i.e. for each line in the origin file.

            String[] rawLine = input.nextLine().split(" ");
            // Set a string array < rawLine > as the line in the < input > scanner split at each whitespace.

            String[] rgbValues = new String[3];
            // Declare a new string array with dimension 3, i.e. 3 indices (R, G and B).

            int iterator = 0;
            // Set an iterator equal to 0 to determine which index in < rgbValues > to write to.

            for (String s : rawLine) {
                // For each string < s > in the rawLine string array.

                if (!"".equals(s) && iterator < 3) {
                    // If < s > is not an empty string it should be ignored, likewise if the iterator is above 2, the program will have already written 3 colour values to < rgbValues >, and as such < s > should be ignored.

                    rgbValues[iterator] = s;
                    // If the case passes the if-statement, < s > must be a numeric value from the .mnd file, which should range from 0 to 255. It is added to the rgbValues array at index < iterator >.

                    iterator ++;
                    // The iterator is iterated :)
                }
            }
            
            int[] rgbInt = Arrays.stream(rgbValues).mapToInt(Integer::parseInt).toArray();
            // Type String[] rgbValues is converted to type int[] rgbInt.

            colours[i] = new Color(rgbInt[0], rgbInt[1], rgbInt[2]);
            // The colour at index < i > in the colour scheme array is set equal to a colour object declared using the fetched RGB values.
        }

        input.close();
        // The < input > Scanner is closed to avoid any potential resource leaks.

        return colours;
        // The dummy colour scheme array < colours > is returned. 
    }

    private static int iterate (Complex z0) {
        Complex z = new Complex(z0);
        // Clone the complex number argument z0 and store it in the variable z.

        for (int i = 0; i < MAX; i++) {
        // Iterate for i going from 0 to the class field MAX. 

            if (z.abs() > 2.0) {
                return i;
                // If the absolute value of the complex number, i.e. the distance between z0 and the origin (0, 0) is greater than 2, the function returns the current number of iterations.
            }

            z = z.times(z).plus(z0);
            // Otherwise the number z is set equal to z^2 plus the original argument z0.
        }

        return MAX;
        // If the absolute value never exceeds 2.0, the loop will have been iterated MAX times, and as MAX is returned.
    }
}
