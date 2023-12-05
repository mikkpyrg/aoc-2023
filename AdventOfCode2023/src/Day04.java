import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day04 extends Solver {
    @Override
    // For each card you've got numbers and attempts, each match gives 2 to the power points so 1, 2, 4 etc
    public Object solve() {
        double sum = 0;
        for (String line : getDataLines()) {
            Matcher m = Pattern.compile(".*:(.*)[|](.*)$")
                    .matcher(line);
            m.find();
            var winningNumbers = Arrays.stream(m.group(1).trim().split(" "))
                    .filter(x -> !x.isEmpty())
                    .map(Integer::parseInt);

            var attemptNumbers = Arrays.stream(m.group(2).trim().split(" "))
                    .filter(x -> !x.isEmpty())
                    .map(Integer::parseInt)
                    .toList();
            long matches = winningNumbers.filter(attemptNumbers::contains).count();

            if (matches > 0) {
                sum += Math.pow(2, matches - 1);
            }
        }
        return sum;
    }



    @Override
    // Each won card duplicates next cards, 1 won 3, so +1 2, +1 3,+1 4
    // count all cards (2 won 1, so +2 3, cus from 1 got +1)
    public Object solve2() {
        int sum = 0;
        HashMap<Integer, Integer> duplicates = new HashMap<>();

        for (String line : getDataLines()) {
            Matcher m = Pattern.compile(".*[ ](\\d*)[:](.*)[|](.*)$")
                    .matcher(line);
            m.find();
            var index = Integer.parseInt(m.group(1));

            var winningNumbers = Arrays.stream(m.group(2).trim().split(" "))
                    .filter(x -> !x.isEmpty())
                    .map(Integer::parseInt);

            var attemptNumbers = Arrays.stream(m.group(3).trim().split(" "))
                    .filter(x -> !x.isEmpty())
                    .map(Integer::parseInt)
                    .toList();
            long matches = winningNumbers.filter(attemptNumbers::contains).count();
            int amountOfCurrentCards = 1 + duplicates.getOrDefault(index, 0);

            if (matches > 0)
            {
                for (int i = 1; i <= matches; i++) {
                    int nextIndex = index + i;
                    duplicates.put(nextIndex, duplicates.getOrDefault(nextIndex, 0) + amountOfCurrentCards);
                }
            }

            sum += amountOfCurrentCards;
        }

        return sum;
    }
}
