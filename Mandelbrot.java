import java.util.Arrays;

public class Mandelbrot {
    private static final int MAX = 250;
    // Constant class field to determine how many itterations each point should make, higher value = higher accuracy and longer process time.

    private static final int GRIDSIZE = 100;
    // Constant class field determining how many fields the < sidelength > by < sidelength > matrix should be split into. 

    private static double sidelength;
    // Class field for the sidelength of the fractal.

    private static Complex center;
    // Class field for the center-point of the fractal in the complex number plane.

    private static Complex[][] Grid = new Complex[GRIDSIZE][GRIDSIZE];
    // Class field for the twodimensional grid of complex numbers found within the < sidelength > by < sidelength > matrix.


    public static void main(String[] args) {
        double[] args_double = Arrays.stream(args).mapToDouble(Double::parseDouble).toArray();
        // Convert type String[] args to type double[] args_double.

        center = new Complex(args_double[0], args_double[1]);
        // Define a complex number which acts as the fractal's center-point in the complex number plane.

        sidelength = args_double[2];
        // Define a sidelength to generate the fractal.

        StdDraw.setXscale(-1, GRIDSIZE);
        StdDraw.setYscale(-1, GRIDSIZE);
        StdDraw.setPenRadius(0.8/(double)GRIDSIZE);
        // Set dimensions and pen radius for the stdDraw library for use in visually representing the grid.

        Grid = determineMatrixCoordinates();
        // Determine the grid using the determineMatrixCoordinates() method.

        draw(Grid);
        // Draw the points of the matrix which fall within the mandelbrot set, determined using the iterate() method.
    }

    private static Complex[][] determineMatrixCoordinates () {
        Complex[][] coordinates = new Complex[GRIDSIZE][GRIDSIZE];
        // Define a dummy grid < coordinates > of size GRIDSIZE * GRIDSIZE to write to.

        for (int x = 0; x < GRIDSIZE; x++) {
            for (int y = 0; y < GRIDSIZE; y++) {
                // Iterate (x, y) over a twodimensional plane of size GRIDSIZE * GRIDSIZE. 

                double x0 = center.getRe() - (double) sidelength / 2.0 + (double) (sidelength * x) / ((double) GRIDSIZE - 1);
                double y0 = center.getIm() - (double) sidelength / 2.0 + (double) (sidelength * y) / ((double) GRIDSIZE - 1);
                // Use expression (1) as described in the documentation to determine the coordinates (x0 and y0) of the center-point of the specific (x, y) sub-grid.

                
                coordinates[x][y] = new Complex(x0, y0);
                // Write the value of the calculated coordinates to the grid as a complex number object.
            }
        }

        return coordinates;
        // Return the new rewritten dummy variable < coordinates >.
    }

    private static void draw (Complex[][] G) {
        StdDraw.clear();
        // Clear any preexisting objects on the StdDraw canvas.

        for (int x = 0; x < GRIDSIZE; x++) {
            for (int y = 0; y < GRIDSIZE; y++) {
                // Iterate (x, y) over a twodimensional plane of size GRIDSIZE * GRIDSIZE. 

                if (iterate(G[x][y]) == MAX) {
                    StdDraw.point(x, y);
                    // If the iterate method returns MAX, the complex number likely falls within the mandelbrot set, as such it is drawn on the canvas.
                }
            }
        }
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
