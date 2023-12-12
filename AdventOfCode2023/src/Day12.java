import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 extends Solver {
    @Override
    // Got to find permutations of sequences.
    // Basically have known and unknown values and got to find how many valid responses I can have
    public Object solve() {
        long sum = 0L;
        for (String line : getDataLines()) {
            var problemAndHint = line.split(" ");
            var problem = problemAndHint[0];
            var hint = Arrays.stream(problemAndHint[1].split(","))
                    .filter(x -> !x.isEmpty())
                    .map(Integer::parseInt)
                    .map(x -> "#".repeat(x) + ".")
                    .collect(Collectors.joining());
            hint = hint.substring(0, hint.length() - 1);
            sum += Arrays.stream(findAmountThatFits(problem, hint, false, "")).distinct().count();
        }

        return sum;
    }

    private String[] findAmountThatFits(String problem, String remainingHint, boolean isSequential, String history) {
        if (problem.length() < remainingHint.length()) {
            return new String[0];
        }
        if (problem.isEmpty()) {
            return new String[] { history };
        }
        var current = problem.charAt(0);
        if (current == '.') {
            if (remainingHint.isEmpty()) {
                return wont(problem, remainingHint, history);
            } else if (remainingHint.charAt(0) == '.') {
                return canMightNot(problem, remainingHint, history);
            } else if (isSequential && remainingHint.charAt(0) == '#') {
                return new String[0];
            } else {
                return wont(problem, remainingHint, history);
            }
        } else if (current == '?') {
            if (remainingHint.isEmpty()) {
                return wont(problem, remainingHint, history);
            } else if (remainingHint.charAt(0) == '.') {
                return canMightNot(problem, remainingHint, history);
            } else if (isSequential && remainingHint.charAt(0) == '#') {
                return hasTo(problem, remainingHint, history);
            } else {
                return canCommitOrNot(problem, remainingHint, history);
            }
        } else {
            if (remainingHint.isEmpty() || remainingHint.charAt(0) == '.') {
                return new String[0];
            } else {
                return hasTo(problem, remainingHint, history);
            }
        }
    }

    private String[] canMightNot(String problem, String remainingHint, String history) {
        var result1 = wont(problem, remainingHint, history);
        var result2 = might(problem, remainingHint, history);
        return Stream.of(result2, result1)
            .flatMap(Stream::of)
            .distinct()
            .toArray(String[]::new);
    }

    private String[] wont(String problem, String remainingHint, String history) {
        return findAmountThatFits(problem.substring(1), remainingHint, false, history + '.');
    }

    private String[] might(String problem, String remainingHint, String history) {
        var newChar = remainingHint.charAt(0);
        return findAmountThatFits(problem.substring(1), remainingHint.substring(1), false, history + newChar);
    }

    private String[] canCommitOrNot(String problem, String remainingHint, String history) {
        var result1 = wont(problem, remainingHint, history);
        var result2 = hasTo(problem, remainingHint, history);
        return Stream.of(result2, result1)
                .flatMap(Stream::of)
                .distinct()
                .toArray(String[]::new);
    }

    private String[] hasTo(String problem, String remainingHint, String history) {
        return findAmountThatFits(problem.substring(1), remainingHint.substring(1), true, history + '#');
    }

    @Override
    //
    public Object solve2() {
        long sum = 0L;

        return sum;
    }
}