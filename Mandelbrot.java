import java.util.Arrays;

public class Mandelbrot {
    private static final int MAX = 20;

    public static void main(String[] args) {
        int[] args_int = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();
        Complex center = new Complex(args_int[0], args_int[1]);
        int sidelength = args_int[2];
    }

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
}
