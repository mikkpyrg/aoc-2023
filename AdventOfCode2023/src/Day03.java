import java.util.ArrayList;
import java.util.List;

public class Day03 extends Solver {
    @Override
    // Sum numbers that are next to symbols in a matrix
    public Object solve() {
        char[] previouseLine = null;
        char[] currentLine = null;
        char[] nextLine = null;
        int sum = 0;

        for (String line : getDataLines()) {
            previouseLine = currentLine;
            currentLine = nextLine;
            nextLine = line.toCharArray();

            sum += getLineSum(previouseLine, currentLine, nextLine);
        }
        previouseLine = currentLine;
        currentLine = nextLine;
        nextLine = null;

        sum += getLineSum(previouseLine, currentLine, nextLine);

        return sum;
    }

    private int getLineSum(char[] previous, char[] current, char[] next) {
        int sum = 0;
        if (current == null) {
            return sum;
        }

        int currentValue = 0;
        boolean hasSymbolAround = false;

        for (int i = 0; i < current.length; i++) {
            char currentChar = current[i];

            if (!Character.isDigit(currentChar)) {
                continue;
            }

            currentValue = currentValue * 10 + Integer.parseInt(String.valueOf(currentChar));

            if (previous != null) {
                hasSymbolAround = hasSymbolAround
                    || isSymbol(previous[i])
                    || (i != 0 && isSymbol(previous[i - 1]))
                    || (i != (current.length - 1) && isSymbol(previous[i + 1]));
            }

            hasSymbolAround = hasSymbolAround
                || (i != 0 && isSymbol(current[i - 1]))
                || (i != (current.length - 1) && isSymbol(current[i + 1]));

            if (next != null) {
                hasSymbolAround = hasSymbolAround
                    || isSymbol(next[i])
                    || (i != 0 && isSymbol(next[i - 1]))
                    || (i != (current.length - 1) && isSymbol(next[i + 1]));
            }

            if (i == (current.length - 1) || !Character.isDigit(current[i + 1])) {
                if (hasSymbolAround) {
                    sum += currentValue;
                }
                currentValue = 0;
                hasSymbolAround = false;
            }
        }

        return sum;
    }

    private boolean isSymbol(char c) {
        return !Character.isDigit(c) && c != '.';
    }

    @Override
    // Multiply the numbers that are around gear symbols. Exactly 2 self containing numbers
    public Object solve2() {
        int sum = 0;

        List<String> dataLines = getDataLines();
        char [][] matrix = new char[dataLines.size()][dataLines.getFirst().length()];

        for (int i = 0; i < dataLines.size(); i++) {
            matrix[i] = dataLines.get(i).toCharArray();
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                char current = matrix[i][j];
                if (current != '*') {
                    continue;
                }

                List<int[]> numberCoordinates = findNumberCoordinates(matrix, i, j);

                if (numberCoordinates.size() == 2)
                {
                    int[] firstCoordinates = numberCoordinates.getFirst();
                    int[] secondCoordinates = numberCoordinates.getLast();
                    int firstNumber = findNumber(matrix, firstCoordinates[0], firstCoordinates[1]);
                    int secondNumber = findNumber(matrix, secondCoordinates[0], secondCoordinates[1]);
                    sum += firstNumber * secondNumber;
                }
            }
        }

        return sum;
    }

    private List<int[]> findNumberCoordinates(char[][] matrix, int centerI, int centerJ) {
        List<int[]> foundNumberCoordinates = new ArrayList<>();

        for (int i = centerI - 1; i <= centerI + 1; i++) {
            if (i < 0 || i == matrix.length) {
                continue;
            }
            for (int j = centerJ - 1; j <= centerJ + 1; j++) {
                if (j < 0 || j == matrix[i].length
                        || (i == centerI && j == centerJ)
                        || !Character.isDigit(matrix[i][j])) {
                    continue;
                }
                foundNumberCoordinates.add(new int[]{i, j});
                // avoid situation where 2 digits of the same number are attached to the gear,
                // as it counts as only 1 number
                if (j != (matrix[i].length - 1) && Character.isDigit(matrix[i][j + 1])) {
                    break;
                }
            }
        }

        return foundNumberCoordinates;
    }

    private int findNumber(char[][] matrix, int i, int j) {
        String foundValue = findAdjacentNumberCharacters(matrix[i], j - 1, false)
            + findAdjacentNumberCharacters(matrix[i], j, true);
        return Integer.parseInt(foundValue);
    }

    private String findAdjacentNumberCharacters(char[] line, int i, boolean positiveDirection) {
        if (i == line.length || i == -1 || !Character.isDigit(line[i]))
        {
            return "";
        }

        char foundCharacter = line[i];
        if (positiveDirection) {
            return foundCharacter + findAdjacentNumberCharacters(line, i + 1, positiveDirection);
        } else {
            return findAdjacentNumberCharacters(line, i - 1, positiveDirection) + foundCharacter;
        }
    }
}
