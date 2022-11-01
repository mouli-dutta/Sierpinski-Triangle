package mouli;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Printer {
    private final List<String> sym;

    public Printer(List<String> sym) {
        this.sym = sym;
    }

    public void printSierpinski(int depth) {
        System.out.printf("%5sSierpinski Triangle (N = %d)\n\n", "", depth);

        printTriangle(Pascal.getPascal(depth), 2);
    }

    public void printMod3(int depth) {
        System.out.printf( "\n\n%8sMod 3 Triangle (N = %d)\n\n", "", depth);

        printTriangle(Pascal.getPascal(depth), 3);
    }


    public void printMod5(int depth) {
        System.out.printf("\n\n%8sMod 5 Triangle (N = %d)\n\n", "", depth);

        printTriangle(Pascal.getPascal(depth), 5);
    }

    private void printTriangle(int[][] p, int mod) {

        IntStream.range(0, p.length)
                .forEach(i -> {
                    System.out.print(" ".repeat(p.length - i - 1));

                    Arrays.stream(p[i])
                            .mapToObj(n -> n % mod != 0 ? sym.get(0) : sym.get(1))
                            .forEach(System.out::print);

                    System.out.println();
                });
    }
}
