import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Day10 extends Solver {
    @Override
    // Got a 2D matrix, got to find the start of the loop and then travers along it,
    // counting it's length, then just divide by 2 to get the farthest point from the start.
    public Object solve() {
        long sum = 0L;
        var lines = getDataLines();
        var map = lines.stream().map(String::toCharArray).toArray(char[][]::new);

        var currentY = 0;
        var currentX = 0;
        var blockAccess = 'x';
        var currentSymbol = 'S';

        outerloop:
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == 'S') {
                    currentX = x;
                    currentY = y;
                    break outerloop;
                }
            }
        }

        do {
            if (currentY != 0 && blockAccess != 'u'
                && (currentSymbol == 'S' || currentSymbol == '|' || currentSymbol == 'L' || currentSymbol == 'J')
                && (map[currentY - 1][currentX] == 'S' || map[currentY - 1][currentX] == 'F' || map[currentY - 1][currentX] == '7' || map[currentY - 1][currentX] == '|')) {
                currentY--;
                blockAccess = 'd';
            } else if (currentY < (map.length - 1) && blockAccess != 'd'
                    && (currentSymbol == 'S' ||currentSymbol == '|' || currentSymbol == '7' || currentSymbol == 'F')
                    && (map[currentY + 1][currentX] == 'S' || map[currentY + 1][currentX] == 'L' || map[currentY + 1][currentX] == 'J' || map[currentY + 1][currentX] == '|')) {
                currentY++;
                blockAccess = 'u';
            } else if (currentX > 0 && blockAccess != 'l'
                    && (currentSymbol == 'S' ||currentSymbol == '-' || currentSymbol == '7' || currentSymbol == 'J')
                    && (map[currentY][currentX - 1] == 'S' || map[currentY][currentX - 1] == 'F' || map[currentY][currentX - 1] == 'L' || map[currentY][currentX - 1] == '-')) {
                currentX--;
                blockAccess = 'r';
            } else if (currentX < (map[0].length - 1) && blockAccess != 'r'
                    && (currentSymbol == 'S' ||currentSymbol == '-' || currentSymbol == 'L' || currentSymbol == 'F')
                    && (map[currentY][currentX + 1] == 'S' || map[currentY][currentX + 1] == 'J' || map[currentY][currentX + 1] == '7' || map[currentY][currentX + 1] == '-')) {
                currentX++;
                blockAccess = 'l';
            } else {
                System.out.print("System broke");
                break;
            }
            currentSymbol = map[currentY][currentX];
            sum++;
        } while (currentSymbol != 'S');

        return sum / 2;
    }

    @Override
    // Calculate the number of spaces inside the loop, that are not part of the loop, "enclosed spaces" or so
    // Should be simple, where I take a point and try to move towards one end of the map
    // like dragging a straight line from the point to the edge of the paper, if it hits 0 or % 2 lines, it's outside.
    // edge case when touching parallel edge
    public Object solve2() {
        long sum = 0L;
        var lines = getDataLines();
        var map = lines.stream().map(String::toCharArray).toArray(char[][]::new);

        var currentY = 0;
        var currentX = 0;

        outerloop:
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == 'S') {
                    currentX = x;
                    currentY = y;
                    break outerloop;
                }
            }
        }

        var loopCoords = new HashMap<Integer, ArrayList<Integer>>();
        loopCoords.put(currentY, new ArrayList<>());
        loopCoords.get(currentY).add(currentX);
        var blockAccess = 'x';
        var currentSymbol = 'S';
        var sY = currentY;
        var sX = currentX;

        do {
            if (currentY != 0 && blockAccess != 'u'
                    && (currentSymbol == 'S' || currentSymbol == '|' || currentSymbol == 'L' || currentSymbol == 'J')
                    && (map[currentY - 1][currentX] == 'S' || map[currentY - 1][currentX] == 'F' || map[currentY - 1][currentX] == '7' || map[currentY - 1][currentX] == '|')) {
                currentY--;
                blockAccess = 'd';
            } else if (currentY < (map.length - 1) && blockAccess != 'd'
                    && (currentSymbol == 'S' ||currentSymbol == '|' || currentSymbol == '7' || currentSymbol == 'F')
                    && (map[currentY + 1][currentX] == 'S' || map[currentY + 1][currentX] == 'L' || map[currentY + 1][currentX] == 'J' || map[currentY + 1][currentX] == '|')) {
                currentY++;
                blockAccess = 'u';
            } else if (currentX > 0 && blockAccess != 'l'
                    && (currentSymbol == 'S' ||currentSymbol == '-' || currentSymbol == '7' || currentSymbol == 'J')
                    && (map[currentY][currentX - 1] == 'S' || map[currentY][currentX - 1] == 'F' || map[currentY][currentX - 1] == 'L' || map[currentY][currentX - 1] == '-')) {
                currentX--;
                blockAccess = 'r';
            } else if (currentX < (map[0].length - 1) && blockAccess != 'r'
                    && (currentSymbol == 'S' ||currentSymbol == '-' || currentSymbol == 'L' || currentSymbol == 'F')
                    && (map[currentY][currentX + 1] == 'S' || map[currentY][currentX + 1] == 'J' || map[currentY][currentX + 1] == '7' || map[currentY][currentX + 1] == '-')) {
                currentX++;
                blockAccess = 'l';
            } else {
                System.out.print("System broke");
                break;
            }
            currentSymbol = map[currentY][currentX];

            if (!loopCoords.containsKey(currentY)) {
                loopCoords.put(currentY, new ArrayList<>());
            }
            loopCoords.get(currentY).add(currentX);

        } while (currentSymbol != 'S');

        // replace S with it's symbol
        var hasTop = loopCoords.getOrDefault(sY - 1, new ArrayList<>()).contains(sX);
        var topSymbol = map[sY - 1][sX];
        var hasBottom = loopCoords.getOrDefault(sY + 1, new ArrayList<>()).contains(sX);
        var bottomSymbol = map[sY + 1][sX];
        var hasLeft = loopCoords.getOrDefault(sY, new ArrayList<>()).contains(sX - 1);
        var leftSymbol = map[sY][sX - 1];
        if (hasTop && (topSymbol == '|' || topSymbol == '7' || topSymbol == 'F')) {
            if (hasBottom && (bottomSymbol == '|' || bottomSymbol == 'L' || bottomSymbol == 'J')) {
                map[sY][sX] = '|';
            } else if (hasLeft && (leftSymbol == '-' || leftSymbol == 'L' || leftSymbol == 'F')) {
                map[sY][sX] = 'J';
            } else {
                map[sY][sX] = 'L';
            }
        } else if (hasBottom && (bottomSymbol == '|' || bottomSymbol == 'L' || bottomSymbol == 'J')) {
            if (hasLeft && (leftSymbol == '-' || leftSymbol == 'L' || leftSymbol == 'F')) {
                map[sY][sX] = '7';
            } else {
                map[sY][sX] = 'F';
            }
        } else {
            map[sY][sX] = '-';
        }

        for (int y = 1; y < map.length - 1; y++) {
            for (int x = 1; x < map[y].length - 1; x++) {
                if (!loopCoords.containsKey(y)) {
                    continue;
                }

                var matchCoords = loopCoords.get(y);
                if (matchCoords.contains(x)) {
                    continue;
                }

                var finalX = x;
                var loopMatches = matchCoords
                        .stream()
                        .filter(m -> m < finalX)
                        .sorted(Comparator.reverseOrder())
                        .toList();
                var countableMatches = 0;
                var edgeStartCharacter = 'x';

                for (int loopX : loopMatches) {
                    switch (map[y][loopX]) {
                        case '|':
                            countableMatches++;
                            break;
                        case '7':
                            edgeStartCharacter = '7';
                            break;
                        case 'J':
                            edgeStartCharacter = 'J';
                            break;
                        case 'F':
                            if (edgeStartCharacter == 'J') {
                                countableMatches++;
                            }
                            edgeStartCharacter = 'x';
                            break;
                        case 'L':
                            if (edgeStartCharacter == '7') {
                                countableMatches++;
                            }
                            edgeStartCharacter = 'x';
                            break;
                        default:
                    }
                }

                if (countableMatches % 2 == 1) {
                    // to make the enclosed points obvious
                    map[y][x] = '0';
                    sum++;
                }
            }
        }

        //display the thing
//        var minY = loopCoords.keySet().stream().min(Integer::compareTo).get();
//        var maxY= loopCoords.keySet().stream().max(Integer::compareTo).get();
//        var minX = 9999999;
//        var maxX = 0;
//
//        for (int y = 0; y < map.length; y++) {
//            var yElements = loopCoords.getOrDefault(y, new ArrayList<>());
//            for (int x = 0; x < map[y].length; x++) {
//                if (map[y][x] != '0' && !yElements.contains(x)) {
//                    map[y][x] = '.';
//                }
//            }
//        }
//
//        for (int y = 0; y <= map[0].length - 1; y++) {
//            System.out.println(new String(map[y]));
//        }

        return sum;
    }
}