import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Day13 extends Solver {
    @Override
    // Find horizontal and vertical pattern match
    public Object solve() {
        long sum = 0L;
        var maps = new ArrayList<ArrayList<String>>();
        var index = 0;
        maps.add(new ArrayList<>());
        for (var line: getDataLines()) {
            if (line.isEmpty()) {
                index++;
                maps.add(new ArrayList<>());
            } else {
                maps.get(index).add(line);
            }
        }

        for (var map : maps) {
            var colMirror = -1;
            outerloop:
            for (int x = 0; x < map.get(0).length() - 1; x++) {
                var nextCol = x + 1;
                var isMirror = true;
                for (int y = 0; y < map.size(); y++) {
                    if (map.get(y).charAt(x) != map.get(y).charAt(nextCol)) {
                        isMirror = false;
                        break;
                    }
                }

                if (isMirror) {
                    var testResult = true;
                    for (int testX = 1; testX <= x ; testX++) {
                        var nextCompare = testX + 1 + x;
                        var previousCompare = x - testX;

                        if (nextCompare >= map.get(0).length()) {
                            break;
                        }

                        for (int y = 0; y < map.size(); y++) {
                            if (map.get(y).charAt(previousCompare) != map.get(y).charAt(nextCompare)) {
                                testResult = false;
                                break;
                            }
                        }
                    }
                    if (testResult) {
                        colMirror = x;
                        break outerloop;
                    }
                }
            }

            var rowMirror = -1;
            outerloop:
            for (int y = 0; y < map.size() - 1; y++) {
                var nextRow = y + 1;
                var isMirror = true;
                for (int x = 0; x < map.get(0).length(); x++) {
                    if (map.get(y).charAt(x) != map.get(nextRow).charAt(x)) {
                        isMirror = false;
                        break;
                    }
                }
                if (isMirror) {
                    var testResult = true;
                    for (int testY = 1; testY <= y ; testY++) {
                        var nextCompare = testY + 1 + y;
                        var previousCompare = y - testY;

                        if (nextCompare >= map.size()) {
                            break;
                        }

                        for (int x = 0; x < map.get(0).length(); x++) {
                            if (map.get(nextCompare).charAt(x) != map.get(previousCompare).charAt(x)) {
                                testResult = false;
                                break;
                            }
                        }
                    }
                    if (testResult) {
                        rowMirror = y;
                        break outerloop;
                    }
                }
            }

            System.out.println("Problem: colIndex: " + colMirror + ", rowIndex: " + rowMirror);
            for (var row : map) {
                System.out.println(row);
            }

            if (rowMirror >= 0) {
                sum += 100L * (rowMirror + 1);
            } else if (colMirror >= 0) {
                sum += colMirror + 1;
            }
        }

        return sum;
    }

    @Override
    // Like 1, only now it has to encounter a single smudge, that stops it being a perfect reflection
    public Object solve2() {
        long sum = 0L;
        var maps = new ArrayList<ArrayList<String>>();
        var index = 0;
        maps.add(new ArrayList<>());
        for (var line: getDataLines()) {
            if (line.isEmpty()) {
                index++;
                maps.add(new ArrayList<>());
            } else {
                maps.get(index).add(line);
            }
        }

        for (var map : maps) {
            var colMirror = -1;
            outerloop:
            for (int x = 0; x < map.get(0).length() - 1; x++) {
                var nextCol = x + 1;
                var isMirror = true;
                var foundSmudge = false;
                for (int y = 0; y < map.size(); y++) {
                    if (map.get(y).charAt(x) != map.get(y).charAt(nextCol)) {
                        if (!foundSmudge) {
                            foundSmudge = true;
                        } else {
                            isMirror = false;
                            break;
                        }
                    }
                }

                if (isMirror) {
                    var testResult = true;
                    for (int testX = 1; testX <= x ; testX++) {
                        var nextCompare = testX + 1 + x;
                        var previousCompare = x - testX;

                        if (nextCompare >= map.get(0).length()) {
                            break;
                        }

                        for (int y = 0; y < map.size(); y++) {
                            if (map.get(y).charAt(previousCompare) != map.get(y).charAt(nextCompare)) {
                                if (!foundSmudge) {
                                    foundSmudge = true;
                                } else {
                                    testResult = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (testResult && foundSmudge) {
                        colMirror = x;
                        break outerloop;
                    }
                }
            }

            var rowMirror = -1;
            outerloop:
            for (int y = 0; y < map.size() - 1; y++) {
                var nextRow = y + 1;
                var isMirror = true;
                var foundSmudge = false;
                for (int x = 0; x < map.get(0).length(); x++) {
                    if (map.get(y).charAt(x) != map.get(nextRow).charAt(x)) {
                        if (!foundSmudge) {
                            foundSmudge = true;
                        } else {
                            isMirror = false;
                            break;
                        }
                    }
                }
                if (isMirror) {
                    var testResult = true;
                    for (int testY = 1; testY <= y ; testY++) {
                        var nextCompare = testY + 1 + y;
                        var previousCompare = y - testY;

                        if (nextCompare >= map.size()) {
                            break;
                        }

                        for (int x = 0; x < map.get(0).length(); x++) {
                            if (map.get(nextCompare).charAt(x) != map.get(previousCompare).charAt(x)) {
                                if (!foundSmudge) {
                                    foundSmudge = true;
                                } else {
                                    testResult = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (testResult && foundSmudge) {
                        rowMirror = y;
                        break outerloop;
                    }
                }
            }

            System.out.println("Problem: colIndex: " + colMirror + ", rowIndex: " + rowMirror);
            for (var row : map) {
                System.out.println(row);
            }

            if (rowMirror >= 0) {
                sum += 100L * (rowMirror + 1);
            } else if (colMirror >= 0) {
                sum += colMirror + 1;
            }
        }

        return sum;
    }
}