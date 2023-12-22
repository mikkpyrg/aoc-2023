import java.util.Arrays;
import java.util.List;

public class Day18 extends Solver {
    @Override
    // Got instructions to build a trench, got to calculate the edges and all of it's insides
    public Object solve() {
        long sum = 0L;
        int x = 0;
        int y = 0;
        for (var line: getDataLines()) {
            var content = line.split(" ");
            var direction = content[0];
            var count = Integer.parseInt(content[1]);
            int newX = 0;
            int newY = 0;
            switch(direction) {
                case "U":
                    newX = x;
                    newY = y - count;
                    break;
                case "D":
                    newX = x;
                    newY = y + count;
                    break;
                case "R":
                    newX = x + count;
                    newY = y;
                    break;
                case "L":
                    newX = x - count;
                    newY = y;
                    break;
            }

            sum += (long) (y + newY) * (x - newX);
            sum += count;

            y = newY;
            x = newX;
        }
        return Math.abs(sum) / 2 + 1;
    }



    @Override
    // Same as 1 only distances bigger
    public Object solve2() {
        long sum = 0L;
        long x = 0;
        long y = 0;
        for (var line: getDataLines()) {
            var content = line.split(" ")[2];
            content = content.substring(2, 8);
            var direction = content.charAt(5);
            var count = Integer.parseInt(content.substring(0,5), 16);
            long newX = 0;
            long newY = 0;
            switch(direction) {
                case '3':
                    newX = x;
                    newY = y - count;
                    break;
                case '1':
                    newX = x;
                    newY = y + count;
                    break;
                case '0':
                    newX = x + count;
                    newY = y;
                    break;
                case '2':
                    newX = x - count;
                    newY = y;
                    break;
            }

            sum += (long) (y + newY) * (x - newX);
            sum += count;

            y = newY;
            x = newX;
        }
        return Math.abs(sum) / 2 + 1;
    }
}