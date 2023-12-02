import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day01 extends Solver {
    @Override
    // Sum of every line's first and last digit
    public Object solve() {
        int max = 0;

        for (String line : getDataLines()) {
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

    @Override
    // Same as first, only take into accounts words 'one', 'two' etc
    public Object solve2() {
        int max = 0;

        for (String line : getDataLines()) {
            List<String> regexResults = new ArrayList<String>();
            Matcher m = Pattern.compile("\\d|one|two|three|four|five|six|seven|eight|nine")
                    .matcher(line.toLowerCase());
            while (m.find()) {
                regexResults.add(m.group());
            }
            max += Integer.parseInt("" + stringToNumber(regexResults.getFirst()) + stringToNumber(regexResults.getLast()));
        }

        return max;
    }

    private int stringToNumber(String word) {
        return switch (word) {
            case "one" -> 1;
            case "two" -> 2;
            case "three" -> 3;
            case "four" -> 4;
            case "five" -> 5;
            case "six" -> 6;
            case "seven" -> 7;
            case "eight" -> 8;
            case "nine" -> 9;
            default -> Integer.parseInt(word);
        };
    }
}
