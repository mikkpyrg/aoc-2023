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
    // like 1 only sequence and hints are 5 times longer
    public Object solve2() {
        long sum = 0L;
        for (String line : getDataLines()) {
            var problemAndHint = line.split(" ");

//            var problem = problemAndHint[0];
//            var hintCounts = Arrays.stream(problemAndHint[1].split(","))
//                    .filter(x -> !x.isEmpty())
//                    .map(Integer::parseInt)
//                    .toArray(Integer[]::new);

            var problem = (problemAndHint[0] + "?").repeat(5);
            problem = problem.substring(0, problem.length() - 1);
            var hint = (problemAndHint[1] + ",").repeat(5);
            hint = hint.substring(0, hint.length() - 1);
            var hintCounts = Arrays.stream(hint.split(","))
                    .filter(x -> !x.isEmpty())
                    .map(Integer::parseInt)
                    .toArray(Integer[]::new);

//            sum += betterSolution(problem + '.', hintCounts, 0, 0, 0);

            var cache = new Long[problem.length() + 1][hintCounts.length + 1];

            for (int i = 0; i < cache.length; i++) {
                for (int k = 0; k < cache[0].length; k++) {
                    cache[i][k] = -1L;
                }
            }
            sum += betterSolutionV2(problem, hintCounts, 0, 0, cache);
        }

        return sum;
    }

    private long betterSolution(String problem, Integer[] hints, int hintSizeDone, int problemIndex, int hintIndex) {
        if (problem.length() <= problemIndex) {
            return hints.length <= hintIndex && hintSizeDone == 0 ? 1 : 0;
        }

        var sum = 0L;
        var path =  problem.charAt(problemIndex);

        if (path == '.' || path == '?') {
            if (hintSizeDone > 0) {
                if (hintIndex < hints.length && hintSizeDone == hints[hintIndex]) {
                    sum += betterSolution(problem, hints , 0, problemIndex + 1, hintIndex + 1);
                }
            } else {
                sum += betterSolution(problem, hints, hintSizeDone, problemIndex + 1, hintIndex);
            }
        }
        if (path == '#' || path == '?'){
            sum += betterSolution(problem, hints, hintSizeDone + 1, problemIndex + 1, hintIndex);
        }
        return sum;
    }

    private long betterSolutionV2(String problem, Integer[] hints, int problemIndex, int hintIndex, Long[][] cache) {
        if (problemIndex >= problem.length()) {
            if (hintIndex < hints.length) {
                return 0;
            }
            return 1;
        }
        if (cache[problemIndex][hintIndex] != -1) {
            return cache[problemIndex][hintIndex];
        }

        var sum = 0L;
        if (problem.charAt(problemIndex) == '.') {
            sum = betterSolutionV2(problem, hints, problemIndex + 1, hintIndex, cache);
        } else {
            if (problem.charAt(problemIndex) == '?') {
                sum += betterSolutionV2(problem, hints, problemIndex + 1, hintIndex, cache);
            }
            // look ahead and solve next sequence of hint
            if (hintIndex < hints.length && problem.length() >= problemIndex + hints[hintIndex]) {
                var newProblemIndex = problemIndex + hints[hintIndex];
                if (!problem.substring(problemIndex, newProblemIndex).contains(".")
                        && (newProblemIndex >= problem.length()  || problem.charAt(newProblemIndex) != '#')) {
                    if (newProblemIndex >= problem.length()) {
                        sum += betterSolutionV2(problem, hints, newProblemIndex, hintIndex + 1, cache);
                    } else {
                        sum += betterSolutionV2(problem, hints, newProblemIndex + 1, hintIndex + 1, cache);
                    }
                }
            }
        }

        cache[problemIndex][hintIndex] = sum;

        return sum;
    }
}