import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day14 extends Solver {
    @Override
    // rocks in map can move north if not obstructed, make the move north and then count how close they are to north
    public Object solve() {
        long sum = 0L;
        var lines = getDataLines();
        var map = new char[lines.size()][lines.get(0).length()];

        for (int i = 0; i < lines.size(); i++) {
            map[i] = lines.get(i).toCharArray();
        }

        for (int x = 0; x < map[0].length; x++) {
            var amountMovable = 0;
            for (int y = 0; y < map.length; y++) {
                var current = map[y][x];
                if (current == 'O') {
                    if (amountMovable > 0) {
                        var newPosY = y - amountMovable;
                        map[newPosY][x] = 'O';
                        map[y][x] = '.';
                    }
                } else if (current == '#') {
                    amountMovable = 0;
                } else {
                    amountMovable++;
                }
            }
        }

        for (int y = map.length; y > 0; y--) {
//            System.out.println(new String(map[map.length - y]));
            for (var current : map[map.length - y]) {
                if (current == 'O') {
                    sum += y;
                }
            }
        }
        return sum;
    }

    @Override
    // movement logic same as 1, but got to do it a lot of times and in 4 directions
    public Object solve2() {
        long sum = 0L;
        var lines = getDataLines();
        var map = new char[lines.size()][lines.get(0).length()];
        var cycleDetection = new ArrayList<String>();
        for (int i = 0; i < lines.size(); i++) {
            map[i] = lines.get(i).toCharArray();
        }

        int currentCycle = 0;
        String cycleStart = null;
        int searchingCycle = 1;
        int cycleCount = 0;

        while (cycleCount == 0) {
            var historyInsert = tumble(map);

            if (cycleStart == null) {
                for (var history: cycleDetection) {
                    if (history.equals(historyInsert)) {
                        cycleStart = historyInsert;
                        break;
                    }
                }

                if (cycleStart == null) {
                    cycleDetection.add(historyInsert);
                }
            } else if (cycleStart.equals(historyInsert)) {
                cycleCount = searchingCycle;
            } else {
                searchingCycle++;
            }

            currentCycle++;
        }

        var cyclesRemaing = (1000000000 - currentCycle) % searchingCycle;
        for (int i = 0; i < cyclesRemaing; i++) {
            tumble(map);
        }

        for (int y = map.length; y > 0; y--) {
            for (var current : map[map.length - y]) {
                if (current == 'O') {
                    sum += y;
                }
            }
        }
        return sum;
    }

    private String tumble(char[][] map) {
        north(map);
        west(map);
        south(map);
        east(map);

        var sb = new StringBuilder();
        for (var current: map) {
            sb.append(current);
        }
        return sb.toString();
    }

    private void north(char[][] map) {
        for (int x = 0; x < map[0].length; x++) {
            var amountMovable = 0;
            for (int y = 0; y < map.length; y++) {
                var current = map[y][x];
                if (current == 'O') {
                    if (amountMovable > 0) {
                        var newPosY = y - amountMovable;
                        map[newPosY][x] = 'O';
                        map[y][x] = '.';
                    }
                } else if (current == '#') {
                    amountMovable = 0;
                } else {
                    amountMovable++;
                }
            }
        }
    }

    private void south(char[][] map) {
        for (int x = 0; x < map[0].length; x++) {
            var amountMovable = 0;
            for (int y = map.length - 1; y >= 0; y--) {
                var current = map[y][x];
                if (current == 'O') {
                    if (amountMovable > 0) {
                        var newPosY = y + amountMovable;
                        map[newPosY][x] = 'O';
                        map[y][x] = '.';
                    }
                } else if (current == '#') {
                    amountMovable = 0;
                } else {
                    amountMovable++;
                }
            }
        }
    }

    private void west(char[][] map) {
        for (int y = 0; y < map.length; y++) {
            var amountMovable = 0;
            for (int x = 0; x < map[0].length; x++) {
                var current = map[y][x];
                if (current == 'O') {
                    if (amountMovable > 0) {
                        var newPosX = x - amountMovable;
                        map[y][newPosX] = 'O';
                        map[y][x] = '.';
                    }
                } else if (current == '#') {
                    amountMovable = 0;
                } else {
                    amountMovable++;
                }
            }
        }
    }

    private void east(char[][] map) {
        for (int y = 0; y < map.length; y++) {
            var amountMovable = 0;
            for (int x = map[0].length - 1; x >= 0; x--) {
                var current = map[y][x];
                if (current == 'O') {
                    if (amountMovable > 0) {
                        var newPosX = x + amountMovable;
                        map[y][newPosX] = 'O';
                        map[y][x] = '.';
                    }
                } else if (current == '#') {
                    amountMovable = 0;
                } else {
                    amountMovable++;
                }
            }
        }
    }
}