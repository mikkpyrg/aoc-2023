import java.util.*;


public class Day17 extends Solver {
    public static final int[][] directions = new int[][] {
        new int[] {-1, 0},
        new int[] {1, 0},
        new int[] {0, -1},
        new int[] {0, 1}
    };

    private boolean isBackTrack(int currentDirection, int desiredDirection) {
        return (currentDirection == 1 && desiredDirection == 0)
                || (currentDirection == 0 && desiredDirection == 1)
                || (currentDirection == 2 && desiredDirection == 3)
                || (currentDirection == 3 && desiredDirection == 2);
    }

    @Override
    // map of distances, got to get to lower right shortest path, can't go in a single direction more then 3 times in a row
    public Object solve() {
        var lines = getDataLines();
        var map = new int[lines.size()][lines.getFirst().length()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.getFirst().length(); j++) {
                map[i][j] = Character.getNumericValue(lines.get(i).charAt(j));
            }
        }

        return calculateMinDistance(map);
    }

    private int calculateMinDistance(int[][] map) {
        var distance = new int[map.length][map[0].length][4][3];
        var queue = new LinkedList<LavaData>();
        queue.add(new LavaData(0, 0, 1, -1, 0));
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                for (int k = 0; k < 4; k++) {
                    for (int l = 0; l < 3; l++) {
                        distance[i][j][k][l] = Integer.MAX_VALUE;
                    }
                }
            }
        }
        for (int k = 0; k < 4; k++) {
            for (int l = 0; l < 3; l++) {
                distance[0][0][k][l] = 0;
            }
        }

        var lengthY = map.length - 1;
        var lengthX = map[0].length - 1;
        while (!queue.isEmpty()) {
            var step = queue.poll();

            for (int i = 0; i < directions.length; i++) {
                var direction = directions[i];
                var newY = step.y + direction[0];
                var newX = step.x + direction[1];
                if (newY < 0 || newX < 0 || newY > lengthY || newX > lengthX
                        || isBackTrack(step.direction, i)
                        || (step.direction == i && step.directionCount == 2)) {
                    continue;
                }
                var newValue = step.currentValue + map[newY][newX];
                var directionCount = step.direction == i
                        ? step.directionCount + 1
                        : 0;
                if (distance[newY][newX][i][directionCount] > newValue) {
                    distance[newY][newX][i][directionCount] = newValue;
                    if (newY != lengthY || newX != lengthX) {
                        queue.add(new LavaData(newX, newY, i, directionCount, newValue));
                    }
                }
            }
        }
        var min = Integer.MAX_VALUE;
        for (int k = 0; k < 4; k++) {
            for (int l = 0; l < 3; l++) {
                if (min > distance[lengthY][lengthX][k][l]) {
                    min = distance[lengthY][lengthX][k][l];
                }
            }
        }
        return min;
    }

    @Override
    // like 1, but now minimal 4 spaces of movement, maximum is 10
    public Object solve2() {
        var lines = getDataLines();
        var map = new int[lines.size()][lines.getFirst().length()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.getFirst().length(); j++) {
                map[i][j] = Character.getNumericValue(lines.get(i).charAt(j));
            }
        }

        return calculateMinDistance2(map);
    }

    private int calculateMinDistance2(int[][] map) {
        var distance = new int[map.length][map[0].length][4][10];
        var queue = new LinkedList<LavaData>();
        queue.add(new LavaData(0, 0, 1, -1, 0));
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                for (int k = 0; k < 4; k++) {
                    for (int l = 0; l < 10; l++) {
                        distance[i][j][k][l] = Integer.MAX_VALUE;
                    }
                }
            }
        }
        for (int k = 0; k < 4; k++) {
            for (int l = 0; l < 10; l++) {
                distance[0][0][k][l] = 0;
            }
        }

        var lengthY = map.length - 1;
        var lengthX = map[0].length - 1;
        while (!queue.isEmpty()) {
            var step = queue.poll();

            for (int i = 0; i < directions.length; i++) {
                // god damn it, that 0 0 start aaaahhhhh
                if (!(step.x == 0 && step.y == 0) && (isBackTrack(step.direction, i) || step.direction == i)) {
                    continue;
                }
                var direction = directions[i];
                var newValue = step.currentValue;
                for (int d = 0; d < 10; d++) {
                    var newY = direction[0] == 0 ? step.y : step.y + (direction[0] * (d + 1));
                    var newX = direction[1] == 0 ? step.x : step.x + (direction[1] * (d + 1));
                    if (newY < 0 || newX < 0 || newY > lengthY || newX > lengthX) {
                        continue;
                    }
                    newValue += map[newY][newX];
                    if (d < 3) {
                        continue;
                    }

                    if (distance[newY][newX][i][d] > newValue) {
                        distance[newY][newX][i][d] = newValue;
                        if (newY != lengthY || newX != lengthX) {
                            queue.add(new LavaData(newX, newY, i, d, newValue));
                        }
                    }
                }
            }
        }
        var min = Integer.MAX_VALUE;
        for (int k = 0; k < 4; k++) {
            for (int l = 0; l < 10; l++) {
                min = Math.min(min, distance[lengthY][lengthX][k][l]);
            }
        }

        return min;
    }

}

class LavaData {
    public LavaData(int x, int y, int direction, int directionCount, int currentValue) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.directionCount = directionCount;
        this.currentValue = currentValue;
    }
    public int x;
    public int y;
    public int direction;
    public int directionCount;
    public int currentValue;
}