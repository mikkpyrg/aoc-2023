import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day16 extends Solver {
    @Override
    // Got to traverse map, certain obstacles change direction and split traverser.
    // Got to find how many spots are traversed, before everyone exits the map
    // Has loops, got to memorize, so ignore loops
    public Object solve() {
        long sum = 0L;
        var lines = getDataLines();
        var energized = new Boolean[lines.size() + 1][lines.get(0).length() + 1];
        var cache = new Integer[lines.size() + 1][lines.get(0).length() + 1][4];
        emptySupportUnits(energized, cache);

        MoveBeam(lines, 1, 0, 0, energized, cache);

        for (var line : energized) {
            sum += Arrays.stream(line).filter(x -> x).count();
        }
        return sum;
    }

    private int MoveBeam(List<String> map, int direction, int posY, int posX, Boolean[][] energized, Integer[][][] cache) {
//        System.out.printf("Y: %s, X: %s, d: %s\n", posY, posX, direction);
        if (posX <= -1 || posY <= -1 || posY >= map.size() || posX >= map.get(0).length()) {
            return 1;
        }

        if (cache[posY][posX][direction] != -1) {
            return cache[posY][posX][direction];
        }

        energized[posY][posX] = true;
        cache[posY][posX][direction] = 1;

//        for (var line : energized) {
//            var a = Arrays.stream(line).map(x -> x ? "0" : ".").toArray(String[]::new);
//            System.out.println(String.join("", a));
//        }

        var sum = 0;
        var path = map.get(posY).charAt(posX);

        if (path == '/' || path == '\\') {
            if ((direction == 1 && path == '/') || (direction == 3 && path == '\\')) {
                sum += MoveBeam(map, 0, posY - 1, posX, energized, cache);
            } else if ((direction == 3 && path == '/') || (direction == 1 && path == '\\')) {
                sum += MoveBeam(map, 2, posY + 1, posX, energized, cache);
            } else if ((direction == 0 && path == '/') || (direction == 2 && path == '\\')) {
                sum += MoveBeam(map, 1, posY, posX + 1, energized, cache);
            } else if ((direction == 2 && path == '/') || (direction == 0 && path == '\\')) {
                sum += MoveBeam(map, 3, posY, posX - 1, energized, cache);
            }
        } else if (path == '-' && (direction == 0 || direction == 2)) {
            sum += MoveBeam(map, 3, posY, posX - 1, energized, cache);
            sum += MoveBeam(map, 1, posY, posX + 1, energized, cache);
        } else if (path == '|' && (direction == 1 || direction == 3)) {
            sum += MoveBeam(map, 0, posY - 1, posX, energized, cache);
            sum += MoveBeam(map, 2, posY + 1, posX, energized, cache);
        } else {
            var newPosX = direction == 3 || direction == 1
                ? (direction == 3 ? posX - 1 : posX + 1)
                : posX;
            var newPosY = direction == 0 || direction == 2
                ? (direction == 0 ? posY - 1 : posY + 1)
                : posY;
            sum += MoveBeam(map, direction, newPosY, newPosX, energized, cache);
        }

        return sum;
    }

    private void emptySupportUnits(Boolean[][] energized, Integer[][][] cache) {
        for (int i = 0; i < cache.length; i++) {
            for (int k = 0; k < cache[0].length; k++) {
                energized[i][k] = false;
                for (int j = 0; j < cache[0][0].length; j++) {
                    cache[i][k][j] = -1;
                }
            }
        }
    }

    private long getAnswer(Boolean[][] energized, long sum) {
        var current = 0l;
        for (var line : energized) {
            current += Arrays.stream(line).filter(x -> x).count();
        }
        return Math.max(sum, current);
    }

    @Override
    // same as 1, only now can enter from any direction, so got to find the longest traversal
    public Object solve2() {
        long sum = 0L;
        var lines = getDataLines();
        var energized = new Boolean[lines.size() + 1][lines.get(0).length() + 1];
        var cache = new Integer[lines.size() + 1][lines.get(0).length() + 1][4];

        var yLength = lines.size();
        var xLength = lines.get(0).length();

        for (int x = 0; x < xLength; x++) {
            emptySupportUnits(energized, cache);
            MoveBeam(lines, 2, 0, x, energized, cache);
            sum = getAnswer(energized, sum);

            emptySupportUnits(energized, cache);
            MoveBeam(lines, 0, yLength - 1, x, energized, cache);
            sum = getAnswer(energized, sum);
        }

        for (int y = 0; y < yLength; y++) {
            emptySupportUnits(energized, cache);
            MoveBeam(lines, 1, y, 0, energized, cache);
            sum = getAnswer(energized, sum);

            emptySupportUnits(energized, cache);
            MoveBeam(lines, 3, y, xLength - 1, energized, cache);
            sum = getAnswer(energized, sum);
        }

        return sum;
    }
}