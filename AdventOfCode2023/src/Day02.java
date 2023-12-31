import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day02 extends Solver {
    @Override
    // enumerate ball game and see if the dealer is cheating
    public Object solve() {
        int solvableIndexSum = 0;
        for (String line : getDataLines()) {
            boolean correctGame = true;
            Matcher m = Pattern.compile("(\\d*) (green|red|blue)(,|;|$)")
                .matcher(line);
            while(m.find())
            {
                int amount = Integer.parseInt(m.group(1));
                String colour = m.group(2);

                if ((colour.equals("red") && amount > 12)
                    || (colour.equals("green") && amount > 13)
                    || (colour.equals("blue") && amount > 14)) {
                    correctGame = false;
                    break;
                }
            }
            if (correctGame) {
                solvableIndexSum += getGameIndex(line);
            }
        }
        return solvableIndexSum;
    }

    private int getGameIndex(String line)
    {
        Matcher m = Pattern.compile("([\\d]*)(?=:)")
                .matcher(line);
        m.find();
        return Integer.parseInt(m.group());
    }

    @Override
    // multiply the fewest colors in a game together (6 red * 2 green * 12 blue)
    public Object solve2() {
        int sum = 0;
        for (String line : getDataLines()) {
            int red = 1;
            int green = 1;
            int blue = 1;
            Matcher m = Pattern.compile("(\\d*) (green|red|blue)(,|;|$)")
                    .matcher(line);
            while(m.find())
            {
                int amount = Integer.parseInt(m.group(1));
                String colour = m.group(2);
                if (colour.equals("red") && amount > red)
                {
                    red = amount;
                } else if (colour.equals("green") && amount > green) {
                    green = amount;
                } else if (colour.equals("blue") && amount > blue) {
                    blue = amount;
                }
            }
            sum += (green * red * blue);
        }
        return sum;
    }
}
