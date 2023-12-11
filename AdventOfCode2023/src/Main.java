public class Main {
    public static void main(String[] args) throws Exception {
        var currentlySolving = 10;

        Solver day = Solver.getSolver(currentlySolving);
        System.out.printf("Day %02d: %s\n", currentlySolving, day.solve());
        System.out.printf("Day %02dB: %s\n", currentlySolving, day.solve2());
    }
}