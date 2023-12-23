import java.util.*;

public class Day23 extends Solver {
    public static final int[][] directions = new int[][] {
            new int[] {-1, 0},
            new int[] {1, 0},
            new int[] {0, -1},
            new int[] {0, 1}
    };

    private int getDirectionBlock(int index) {
        return switch (index) {
            case 1 -> 0;
            case 2 -> 3;
            case 3 -> 2;
            default -> 1;
        };
    }

    private int getDestinationDirection(char slope) {
        return switch (slope) {
            case 'v' -> 1;
            case '<' -> 2;
            case '>' -> 3;
            default -> 0;
        };
    }

    @Override
    // Get longest path from A to B;
    public Object solve() {
        var map = getDataLines().stream().map(String::toCharArray).toArray(char[][]::new);
        var walked = new ArrayList<boolean[][]>();
        var queue = new LinkedList<Integer[]>();
        var lengthY = map.length;
        var lengthX = map[0].length;

        walked.add(new boolean[lengthY][lengthX]);
        walked.get(0)[0][1] = true;
        queue.add(new Integer[] {0 , 1 , 1});

        while (!queue.isEmpty()) {
            var nextMap = queue.poll();
            var i = nextMap[0];
            var x = nextMap[2];
            var y = nextMap[1];
            var currentStep = map[y][x];
            var history = walked.get(i);
            history[y][x] = true;

            if (x.equals(lengthX - 2) && y.equals(lengthY - 1)) {
                continue;
            }

            if (currentStep == '^' || currentStep == 'v' || currentStep == '<' || currentStep == '>') {
                var direction = directions[getDestinationDirection(currentStep)];
                var newY = y + direction[0];
                var newX = x + direction[1];
                if (history[newY][newX]) {
                    continue;
                }

            }

            var needsNewPath = false;
            for (int d = 0; d < directions.length; d++) {
                var direction = directions[d];
                var newX = x + direction[1];
                var newY = y + direction[0];
                var path = map[newY][newX];
                if (path != '#'
                    && !(path == '>' && d == 2)
                    && !(path == '<' && d == 3)
                    && !(path == 'v' && d == 0)
                    && !(path == '^' && d == 1)
                    && !history[newY][newX]) {
                    if (needsNewPath) {
                        var newHistory = new boolean[lengthY][lengthX];
                        for (int j = 0; j < newHistory.length; j++) {
                            newHistory[j] = history[j].clone();
                        }
                        walked.add(newHistory);
                        queue.add(new Integer[] {walked.size() - 1, newY, newX});
                    } else {
                        needsNewPath = true;
                        queue.add(new Integer[] {i, newY, newX});
                    }
                }
            }
        }
        var sum = 0;

        for (var walk : walked) {
            var tempSum = 0;
            for (var row : walk) {
                for (var step : row) {
                    if (step) {
                        tempSum++;
                    }
                }
            }
            sum = Math.max(sum, tempSum);
        }

        return sum - 1;
    }

    @Override
    // can climb up slopes. Slow solution is so slow, that I turned the map into a graph of intersections and just did
    // a brute force depth first search
    public Object solve2() {
        var map = getDataLines().stream().map(String::toCharArray).toArray(char[][]::new);
        var queue = new LinkedList<Integer[]>();
        var extraCost = 0;
        var intersections = new ArrayList<PathGraph>();
        intersections.add(new PathGraph(0, map.length - 1, map[0].length - 2));
        var firstIntersection = pathTillNextIntersection(map, 1, 1, 0, 1);
        extraCost = firstIntersection[0];
        intersections.add(new PathGraph(intersections.size(), firstIntersection[2], firstIntersection[3]));
        for (var d : findDirections(map, firstIntersection[2], firstIntersection[3], firstIntersection[1])) {
            queue.add(new Integer[] {intersections.size() - 1, d});
        }

        while (!queue.isEmpty()) {
            var trip = queue.poll();
            var fromIntersection = intersections.get(trip[0]);
            var intersection = pathTillNextIntersection(map,
                    fromIntersection.y + directions[trip[1]][0],
                    fromIntersection.x + directions[trip[1]][1],
                    getDirectionBlock(trip[1]), 1);
            if (intersection == null) {
                continue;
            }
            if (intersection[1] == -1) {
                var goal = intersections.get(0);
                goal.addRelation(fromIntersection.index, intersection[0]);
                fromIntersection.addRelation(goal.index, intersection[0]);
            } else {
                var toIntersection = intersections.stream()
                        .filter(i -> i.y == intersection[2] && i.x == intersection[3])
                        .findFirst();
                PathGraph foundIntersection;
                if (toIntersection.isPresent()) {
                    foundIntersection = toIntersection.get();
                } else {
                    foundIntersection = new PathGraph(intersections.size(), intersection[2], intersection[3]);
                    intersections.add(foundIntersection);

                    for (var d : findDirections(map, intersection[2], intersection[3], intersection[1])) {
                        queue.add(new Integer[] {foundIntersection.index, d});
                    }
                }

                foundIntersection.addRelation(fromIntersection.index, intersection[0]);
                fromIntersection.addRelation(foundIntersection.index, intersection[0]);
            }
        }

        var searchQueue = new LinkedList<int[]>();
        var history = new ArrayList<Integer>();
        var sum = 0;
        searchQueue.add(new int[] {-1, 1, extraCost});
        while (!searchQueue.isEmpty()) {
            var node = searchQueue.pollLast();

            var historyIndex = node[0];
            var nodeIndex = node[1];
            var count = node[2];
            var object = intersections.get(nodeIndex);

            var toRemove = history.size() - (historyIndex + 1);
            for (int i = 0; i < toRemove; i++) {
                history.remove(historyIndex + 1);
            }

            historyIndex++;
            history.add(nodeIndex);

            for (var nextNode: object.relations.stream().filter(x -> !history.contains(x[0])).toList()) {
                if (nextNode[0] == 0) {
                    sum = Math.max(sum, count + nextNode[1]);
                    continue;
                }
                searchQueue.add(new int[] {historyIndex, nextNode[0], count + nextNode[1]});
            }
        }

        return sum;
    }

    private ArrayList<Integer> findDirections(char[][] map, int y, int x, int block) {
        var allowedDirections = new ArrayList<Integer>();

        for (int d = 0; d < 4; d++) {
            if (d == block) {
                continue;
            }
            var newX = x + directions[d][1];
            var newY = y + directions[d][0];
            if (newY < map.length && map[newY][newX] != '#') {
                allowedDirections.add(d);
            }
        }

        return allowedDirections;
    }

    private int[] pathTillNextIntersection(char[][] map, int y, int x, int block, int count) {
        var allowedDirections = findDirections(map, y, x, block);

        if (allowedDirections.size() == 1) {
            var d = allowedDirections.getFirst();
            return pathTillNextIntersection(map, y + directions[d][0],x + directions[d][1],
                    getDirectionBlock(d), count + 1);
        } else if (allowedDirections.isEmpty() && y == map.length - 1 && x == map[0].length - 2) {
            return new int[] {count, -1, y, x};
        } else if (allowedDirections.isEmpty()) {
            return null;
        } else {
            return new int[] {count, block, y, x};
        }
    }

    private int slowSolution() {
        var map = getDataLines().stream().map(String::toCharArray).toArray(char[][]::new);
        var lengthY = map.length;
        var lengthX = map[0].length;
        var walked = new ArrayList<int[]>();
        walked.add(new int[] {0, 1});
        var waiting = new ArrayList<int[]>();
        waiting.add(new int[] {0, 1 , 1});
        var finished = 0;

        while (!waiting.isEmpty()) {
            var nextStartPoint = waiting.getLast();
            waiting.remove(waiting.size() - 1);
            var historyI = nextStartPoint[0];
            var y = nextStartPoint[1];
            var x = nextStartPoint[2];

            var toRemove = walked.size() - (historyI + 1);
            for (int i = 0; i < toRemove; i++) {
                walked.remove(historyI + 1);
            }
            while(true) {
                historyI = historyI + 1;
                walked.add(new int[] {y, x});

                if (x == (lengthX - 2) && y == (lengthY - 1)) {
                    finished = Math.max(finished, historyI);
                    if (finished == historyI) {
                        System.out.println(finished);
                    }

                    break;
                }
                var mainPathSelected = false;
                var tempX = 0;
                var tempY = 0;
                for (int d = 0; d < 4; d++) {
                    var newX = x + directions[d][1];
                    var newY = y + directions[d][0];
                    var path = map[newY][newX];
                    if (path != '#' && walked.stream().noneMatch(w -> w[0] == newY && w[1] == newX)) {
                        if (mainPathSelected) {
                            waiting.add(new int[] {historyI, newY, newX});
                        } else {
                            mainPathSelected = true;
                            tempY = newY;
                            tempX = newX;
                        }
                    }
                }

                if (mainPathSelected) {
                    x = tempX;
                    y = tempY;
                } else {
                    break;
                }
            }
        }

        return finished;
    }
}

class PathGraph {
    public PathGraph(int index, int y, int x) {
        this.index = index;
        this.x = x;
        this.y = y;
        relations = new ArrayList<>();
    }
    public int index;
    public int x;
    public int y;
    public ArrayList<int[]> relations;

    public void addRelation(int otherIndex, int count) {
        if (!relations.stream().anyMatch(x -> x[0] == otherIndex)) {
            relations.add(new int[] {otherIndex, count});
        }
    }
}
