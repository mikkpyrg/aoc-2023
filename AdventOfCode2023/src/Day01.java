import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

public class Day01 extends Solver {
    @Override
    // Sum of every line's first and last digit
    public Object solve() {
        List<String> lines = getDataLines();
        int max = 0;

        for (String line : lines) {
            char firstDigit = 0;
            char lastDigit = 0;

            for (int i = 0; i < line.length(); i++) {
                char character = line.charAt(i);
                if (Character.isDigit(character))
                {
                    lastDigit = character;
                    if (firstDigit == 0)
                    {
                        firstDigit = character;
                    }
                }
            }
            max += Integer.parseInt("" + firstDigit + lastDigit);
        }

        return max;
    }
}
