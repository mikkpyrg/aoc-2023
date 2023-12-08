import java.util.*;

public class Day06 extends Solver {
    @Override
    // every race lasts certain amount of time, more time you wait the faster you will be, got to beat records
    public Object solve() {
        var lines = getDataLines();
        var sum = 1;
        var gameTimes = Arrays.stream(lines
                .getFirst()
                .split(":")[1]
                .trim()
                .split(" "))
            .filter(x -> !x.isEmpty())
            .map(Integer::parseInt)
            .toArray(Integer[]::new);

        var records = Arrays.stream(lines
                .getLast()
                .split(":")[1]
                .trim()
                .split(" "))
            .filter(x -> !x.isEmpty())
            .map(Integer::parseInt)
            .toArray(Integer[]::new);

        for (int i = 0; i < gameTimes.length; i++) {
            var recordBuffer = 0;
            for (int j = 1; j < gameTimes[i]; j++) {
                var distance = j * (gameTimes[i] - j);
                if (distance > records[i]) {
                   recordBuffer++;
                }
            }
            sum *= recordBuffer;
        }
        
        return sum;
    }


    @Override
    // there's only one race
    public Object solve2() {
        var lines = getDataLines();
        long sum = 0;
        var gameTime = Long.parseLong(lines
            .getFirst()
            .split(":")[1]
            .replaceAll(" ", ""));

        var record = Long.parseLong(lines
            .getLast()
            .split(":")[1]
            .replaceAll(" ", ""));

        for (long i = 1; i < gameTime; i++) {
            if (record < (i * (gameTime - i))) {
                sum++;
            }
        }

        return sum;
    }


}
