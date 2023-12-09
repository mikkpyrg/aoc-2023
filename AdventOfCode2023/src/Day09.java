import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Day09 extends Solver {
    @Override
    // got a sequence of numbers, got to predict the next value,
    // problem already described the solution, but calculating differences between the values,
    // if all the differences aren't 0, then do the differences of the differences, until you get all zeros
    // then just add a new zero and add that zero with the last number of second to last row's number to get a new number,
    // do that for every row until you get to the top, that's the new predicted value
    public Object solve() {
        long sum = 0L;

        for (String line : getDataLines()) {
            var table = new ArrayList<ArrayList<Integer>>();
            var values = Arrays.stream(line.split(" "))
                .filter(x -> !x.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());

            table.add((ArrayList<Integer>) values);

            while(!table.getLast().stream().allMatch(x -> x == 0)) {
                var differences = new ArrayList<Integer>();
                var lastSet = table.getLast();
                for (int i = 0; i < lastSet.size() - 1; i++) {
                    differences.add(lastSet.get(i + 1) - lastSet.get(i));
                }

                table.add(differences);
            }

            table.getLast().add(0);

            for (int i = table.size() - 2; i >= 0; i--) {
                var traverseRow = table.get(i);
                var bottomRow = table.get(i + 1);
                traverseRow.add(traverseRow.getLast() + bottomRow.getLast());
            }

            sum += table.get(0).getLast();
        }

        return sum;
    }

    @Override
    // like first, only interpolate
    public Object solve2() {
        long sum = 0L;

        for (String line : getDataLines()) {
            var table = new ArrayList<ArrayList<Integer>>();
            var values = Arrays.stream(line.split(" "))
                    .filter(x -> !x.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            table.add((ArrayList<Integer>) values);

            while(!table.getLast().stream().allMatch(x -> x == 0)) {
                var differences = new ArrayList<Integer>();
                var lastSet = table.getLast();
                for (int i = 0; i < lastSet.size() - 1; i++) {
                    differences.add(lastSet.get(i + 1) - lastSet.get(i));
                }

                table.add(differences);
            }

            table.getLast().add(0, 0);

            for (int i = table.size() - 2; i >= 0; i--) {
                var traverseRow = table.get(i);
                var bottomRow = table.get(i + 1);
                traverseRow.add(0, traverseRow.getFirst() - bottomRow.getFirst());
            }

            sum += table.get(0).getFirst();
        }

        return sum;
    }
}
