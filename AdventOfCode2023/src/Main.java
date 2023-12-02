public class Main {
    public static void main(String[] args) throws Exception {
        Solver day1 = Solver.getSolver(1);
        System.out.printf("Day 1: %s\n", day1.solve());
        System.out.printf("Day 1B: %s\n", day1.solve2());
    }
}