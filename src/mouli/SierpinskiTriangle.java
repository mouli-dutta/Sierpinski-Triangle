package mouli;

import java.util.List;

public class SierpinskiTriangle {
    public static void main(String[] args) {
        var p1 = new Printer(List.of("% ", ". "));
        p1.printSierpinski(32);

        var p2 = new Printer(List.of("$ ", "· "));
        p2.printMod3(18);
        p2.printMod5(20);
    }
}

