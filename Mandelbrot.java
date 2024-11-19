import java.util.Arrays;

public class Mandelbrot {
    private static final int MAX = 20;
    // Constant class field to determine how many itterations each point should make, higher value = higher accuracy and longer process time.

    private static final int GRIDSIZE = 5;
    // Constant class field determining how many fields the < sidelength > by < sidelength > matrix should be split into. 

    private static int sidelength;
    // Class field for the sidelength of the fractal.

    private static Complex center;
    // Class field for the center-point of the fractal in the complex number plane.

    private static Complex[][] Grid = new Complex[GRIDSIZE][GRIDSIZE];
    // Class field for the twodeimgrid of complex numbers found within the < sidelength > by < sidelength > matrix.


    public static void main(String[] args) {
        int[] args_int = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();
        // Convert type String[] args to type int[] args_int.

        Complex center = new Complex(args_int[0], args_int[1]);
        // Define a complex number which acts as the fractal's center-point in the complex number plane.

        int sidelength = args_int[2];
        // Define a sidelength to generate the fractal.

        
    }

    private static Complex[] determineMatrixCoordinates

    private static void draw (Complex[] C) {

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
