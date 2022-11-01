package mouli;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Pascal {

    public static int[][] getPascal(int depth) {
        return Stream.iterate(
                new int[]{1}, Pascal::getNextRow)
                .limit(depth)
                .toArray(int[][]::new);
    }

    private static int[] getNextRow(int[] row) {
        int[] nextRow = new int[row.length + 1];
        nextRow[0] = nextRow[nextRow.length - 1] = 1;

        IntStream.range(0, row.length - 1)
                .forEach(i -> nextRow[i + 1] = row[i] + row[i + 1]);

        return nextRow;
    }

    private Pascal() {
    }

}
