import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;

public class Day21 extends Solver {
    public static final int[][] directions = new int[][] {
            new int[] {-1, 0},
            new int[] {1, 0},
            new int[] {0, -1},
            new int[] {0, 1}
    };

    @Override
    // So you start at s and kind of toggle move, like game of life or something.
    // every new cycle you take as many steps as possible, and so you go back and forth a lot
    public Object solve() {
        var map = getDataLines().stream().map(String::toCharArray).toArray(char[][]::new);
        var points = new HashMap<String, int[]>();
        var lengthY = map.length;
        var lengthX = map[0].length;
        for (int y = 0; y < lengthY; y++) {
            for (int x = 0; x < lengthX; x++) {
                if (map[y][x] == 'S') {
                    points.put(y + "-" + x, new int[] {y, x});
                }
            }
        }

        for (int i = 0; i < 64; i++) {
            var tempPoints = new HashMap<String, int[]>();
            for (var pointName : points.keySet()) {
                var step = points.get(pointName);
                for (var direction : directions) {
                    var newY = step[0] + direction[0];
                    var newX = step[1] + direction[1];
                    var key = newY + "-" + newX;
                    if (newY < 0 || newX < 0 || newY >= lengthY || newX >= lengthX || map[newY][newX] == '#' || tempPoints.containsKey(key)) {
                        continue;
                    }
                    tempPoints.put(key, new int[] {newY, newX});
                }
            }
            points = tempPoints;
        }
        return points.keySet().size();
    }

    @Override
    // so same as 1, but a lot of steps so you move to infinity on the map scale
    // to be honest I don't know how this works in the confines of this problem,
    // It's a weird combination of starting at the center of map,
    // move out-wards in a diamond shape
    // and how the total step count relates to the width of the map
    public Object solve2() {
        var lines = getDataLines();
        var width = lines.get(0).length();
        var steps = 26501365L;
        var reminder = steps % width;
        var v1 = GetHumangousSteps(reminder);
        var v2 = GetHumangousSteps(reminder + width);
        var v3 = GetHumangousSteps(reminder + 2L * width);
        var n = (long)(steps / width);

        // I hate how simple this looks. I hate how complicated the problem was.
        // I hate how many assumptions I needed to make about the input data to even start arriving at a formula for the answer.
        // I hate how these assumptions were not a part of the specifications or the example data.
        // I hate how the formula I arrived at didn't work.
        // I hate how even other people's formulas didn't work for my input.
        // I hate how I know after all of this that the answer is the result of some quadratic equation that I couldn't derive myself and that I don't even know the coefficients of.
        // I hate, hate, hate, hate, hate, hate, hate this problem. Get it out of my sight.
        var b0 = v1;
        var b1 = v2-v1;
        var b2 = v3-v2;
        return b0 + b1*n + (n*(n-1)/2)*(b2-b1);

    }

    private long GetHumangousSteps(long stepCount) {
        var lines = getDataLines();
        var map = new char[lines.size() * 5][lines.size() * 5];
        for (int i = 0; i < lines.size() - 1; i++) {
            var line = lines.get(i) + lines.get(i) + lines.get(i) + lines.get(i) + lines.get(i);
            map[i] = line.toCharArray();
            map[lines.size() + i] = line.toCharArray();
            map[lines.size() * 2 + i] = line.toCharArray();
            map[lines.size() * 3 + i] = line.toCharArray();
            map[lines.size() * 4 + i] = line.toCharArray();
        }

        var points = new HashMap<String, int[]>();
        var lengthY = map.length;
        var lengthX = map[0].length;
        var x = lengthY / 2;
        var y = lengthX / 2;
        points.put(y + "-" + x, new int[] {y, x});

        for (int i = 0; i < stepCount; i++) {
            var tempPoints = new HashMap<String, int[]>();
            for (var pointName : points.keySet()) {
                var step = points.get(pointName);
                for (var direction : directions) {
                    var newY = step[0] + direction[0];
                    var newX = step[1] + direction[1];
                    var key = newY + "-" + newX;
                    if (newY < 0 || newX < 0 || newY >= lengthY || newX >= lengthX || map[newY][newX] == '#' || tempPoints.containsKey(key)) {
                        continue;
                    }
                    tempPoints.put(key, new int[] {newY, newX});
                }
            }
            points = tempPoints;
        }
        return points.keySet().size();
    }
}