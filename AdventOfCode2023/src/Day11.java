import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class Day11 extends Solver {
    @Override
    // Have a matrix of points, got to get all paths between them. can only move cardinally
    // so doesn't actually need movement, only coordinates.
    public Object solve() {
        long sum = 0L;
        var lines = GetTranslatedMap();

        var points = new ArrayList<Point>();
        for (int y = 0; y < lines.size(); y++) {
            var line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                var foundPoint = line.charAt(x);
                if (foundPoint == '#') {
                    for (var point: points) {
                        sum += (long) (y - point.getY() + Math.abs(x - point.getX()));
                    }
                    points.add(new Point(x, y));
                }
            }
        }

        return sum;
    }

    private ArrayList<String> GetTranslatedMap() {
        var lines = new ArrayList<String>(getDataLines());
        //should do it bitwise probably, but who cares;
        var colCheckHasGalaxy = new boolean[lines.get(0).length()];
        for (var line : lines) {
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '#') {
                    colCheckHasGalaxy[i] = true;
                }
            }
        }
        for (int i = 0; i < lines.size(); i++) {
            var sb = new StringBuilder(lines.get(i));
            var addedColCount = 0;
            for (int j = 0; j < colCheckHasGalaxy.length; j++) {
                if (!colCheckHasGalaxy[j]) {
                    sb.insert(j + addedColCount, '.');
                    addedColCount++;
                }
            }
            lines.set(i, sb.toString());
        }
        var rowCheckHasGalaxy = new ArrayList<Integer>();
        for (int i = 0; i < lines.size(); i++) {
            if (!lines.get(i).contains("#")) {
                rowCheckHasGalaxy.add(i);
            }
        }
        var rowsAddedCount = 0;
        var singleRowLength = lines.get(0).length();
        var stringToAdd = ".".repeat(lines.get(0).length());
        for (var addRow : rowCheckHasGalaxy) {
            lines.add(rowsAddedCount + addRow, stringToAdd);
            rowsAddedCount++;
        }
        return lines;
    }

    @Override
    // same as 1, only now empty rows are worth 1kk points.
    // Do I really have to do actual pathfinding now? NO! I WILL NOT
    // How the points are set out, I can still just calculate,
    // cus it doesn't matter how I move my pathfinder (because only 4 cardinal directions), so can go like only y axis, and then only x axis
    // Now just got to figure out what empty voids it has passed and add 1kk-1 to it for each one.
    public Object solve2() {
        long sum = 0L;
        var lines = getDataLines();
        var xVoids = new ArrayList<Integer>();
        for (int i = 0; i < lines.size(); i++) {
            if (!lines.get(i).contains("#")) {
                xVoids.add(i);
            }
        }

        var yVoids = new ArrayList<Integer>();
        for (int y = 0; y < lines.get(0).length(); y++) {
            var isVoid = true;
            for (var line : lines) {
                if (line.charAt(y) == '#') {
                    isVoid = false;
                    break;
                }
            }

            if (isVoid) {
                yVoids.add(y);
            }
        }

        var points = new ArrayList<Point>();
        for (int y = 0; y < lines.size(); y++) {
            var line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                var foundPoint = line.charAt(x);
                if (foundPoint == '#') {
                    for (var point: points) {
                        sum += calculateDistance(xVoids, y, (int)point.getY())
                            + calculateDistance(yVoids, x, (int)point.getX());

                    }
                    points.add(new Point(x, y));
                }
            }
        }

        return sum;
    }

    private long calculateDistance(ArrayList<Integer> voidOfSpace, int point1, int point2) {
        var amountOfVoidInBetween = point2 > point1
                ? voidOfSpace.stream().filter(x -> point2 > x && x > point1).count()
                : voidOfSpace.stream().filter(x -> point1 > x && x > point2).count();

        return Math.abs(point1 - point2) + (1000000L * amountOfVoidInBetween) - amountOfVoidInBetween;
    }
}