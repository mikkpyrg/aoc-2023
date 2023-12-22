import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Day19 extends Solver {
    @Override
    // Got rows of data and rules to sort them. Got to sum the data that reach A, discard R
    public Object solve() {
        long sum = 0L;
        var startData = false;
        var rules = new HashMap<String, Rule[]>();
        var queue = new LinkedList<Input>();

        for (var line: getDataLines()) {
            if (line.isEmpty()) {
                startData = true;
                continue;
            }

            if (startData) {
                queue.add(new Input(line));
            } else {
                var nameAndData = line.split("\\{");
                var refinedData = nameAndData[1].substring(0, nameAndData[1].length() - 1);
                var dataPoints = refinedData.split(",");
                var data = new Rule[dataPoints.length];
                for (int i = 0; i < dataPoints.length; i++) {
                    if (i == dataPoints.length - 1) {
                        data[i] = new Rule("a>0:" + dataPoints[i]);
                    } else {
                        data[i] = new Rule(dataPoints[i]);
                    }
                }
                rules.put(nameAndData[0], data);
            }
        }

        while (!queue.isEmpty()) {
            var input = queue.pop();
            if (input.destination.equals("R")) {
            } else if (input.destination.equals("A")) {
                for (var categoryValue : input.categories.keySet()) {
                    sum += input.categories.get(categoryValue);
                }
            } else {
                var foundRules = rules.get(input.destination);
                for (var rule: foundRules) {
                    var inputValue = input.categories.get(rule.category);
                    if ((rule.greaterThen && inputValue > rule.limit)
                        || (!rule.greaterThen && inputValue < rule.limit)) {
                        input.destination = rule.destination;
                        queue.add(input);
                        break;
                    }
                }
            }
        }

        return sum;
    }

    @Override
    // Now we're finding all possible combinations, so like a previous problem, got to deal with ranges.
    public Object solve2() {
        long sum = 0L;
        var rules = new HashMap<String, Rule[]>();
        var queue = new LinkedList<Input2>();

        for (var line: getDataLines()) {
            if (line.isEmpty()) {
                break;
            }

            var nameAndData = line.split("\\{");
            var refinedData = nameAndData[1].substring(0, nameAndData[1].length() - 1);
            var dataPoints = refinedData.split(",");
            var data = new Rule[dataPoints.length];
            for (int i = 0; i < dataPoints.length; i++) {
                if (i == dataPoints.length - 1) {
                    data[i] = new Rule("a>-1:" + dataPoints[i]);
                } else {
                    data[i] = new Rule(dataPoints[i]);
                }
            }
            rules.put(nameAndData[0], data);
        }
        int[][] allIncluded = {{1, 4000}, {1, 4000},{1, 4000},{1, 4000}};
        queue.add(new Input2("in", allIncluded));
        while (!queue.isEmpty()) {
            var input = queue.pop();
            if (input.destination.equals("R")) {
            } else if (input.destination.equals("A")) {
//                printArray(input.categories);
                var x = input.categories[0][1] - input.categories[0][0] + 1;
                var m = input.categories[1][1] - input.categories[1][0] + 1;
                var a = input.categories[2][1] - input.categories[2][0] + 1;
                var s = input.categories[3][1] - input.categories[3][0] + 1;
                sum += (long) x * m * a * s;
            } else {
                var foundRules = rules.get(input.destination);
                var inputCategories = input.categories;
                for (var rule: foundRules) {
                    var inputMin = inputCategories[rule.categoryInt][0];
                    var inputMax = inputCategories[rule.categoryInt][1];
                    if (rule.greaterThen && inputMax > rule.limit) {
                        int[][] copy = Arrays.stream(inputCategories).map(int[]::clone).toArray(int[][]::new);
                        copy[rule.categoryInt][0] = Math.max(rule.limit + 1, inputMin);
                        copy[rule.categoryInt][1] = inputMax;
                        queue.add(new Input2(rule.destination, copy));
                        inputCategories[rule.categoryInt][0] = Math.min(rule.limit, inputMin);
                        inputCategories[rule.categoryInt][1] = rule.limit;
                    } else if (!rule.greaterThen && inputMin < rule.limit) {
                        int[][] copy = Arrays.stream(inputCategories).map(int[]::clone).toArray(int[][]::new);
                        copy[rule.categoryInt][0] = inputMin;
                        copy[rule.categoryInt][1] = Math.min(rule.limit - 1, inputMax);
                        queue.add(new Input2(rule.destination, copy));
                        inputCategories[rule.categoryInt][0] = rule.limit;
                        inputCategories[rule.categoryInt][1] = Math.max(rule.limit, inputMax);
                    }
                }
            }
        }

        return sum;
    }

    private void printArray(int[][] array) {
        for (var categoryValues : array) {
            System.out.print(categoryValues[0] + "-" + categoryValues[1] + "|||");
        }
        System.out.println();
    }
}

class Rule {
    public Rule(String input) {
        category = input.charAt(0);
        greaterThen = input.charAt(1) == '>';
        var limitAndDest = input.substring(2).split(":");
        limit = Integer.parseInt(limitAndDest[0]);
        destination = limitAndDest[1];
        switch (category) {
            case 'x':
                categoryInt = 0;
                break;
            case 'm':
                categoryInt = 1;
                break;
            case 'a':
                categoryInt = 2;
                break;
            case 's':
                categoryInt = 3;
                break;
        }
    }
    public char category;
    public int categoryInt;
    public boolean greaterThen;
    public int limit;
    public String destination;
}

class Input {
    public Input (String data) {
        data = data.substring(1, data.length() - 1);
        categories = new HashMap<>();
        for (var category: data.split(",")) {
            var categoryValue = category.split("=");
            categories.put(categoryValue[0].charAt(0), Integer.parseInt(categoryValue[1]));
        }
        destination = "in";
    }

    public String destination;
    public HashMap<Character, Integer> categories;
}

class Input2 {
    public Input2 (String destination, int[][] categories) {
        this.destination = destination;
        this.categories = categories;
    }

    public String destination;
    public int[][] categories;
}