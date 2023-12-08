import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Day08 extends Solver {
    @Override
    // I've got a list of options (a = (b,z)) and a list of instructions.
    // Instructions tell me the order of taking options (left, right, right, left etc)
    // got to traverse the options to reach point ZZZ, return count of actions
    public Object solve() {
        long sum = 0L;
        var lines = getDataLines();
        var instructions = lines.getFirst();
        var options = new HashMap<String, String[]>();
        var currentLocation = "AAA";
        var maxInstructionCount = instructions.length() - 1;
        var currentInstructionCount = 0;

        for (int i = 2; i < lines.size(); i++) {
            var splitOption = lines.get(i).split("=");
            var destinations = splitOption[1].trim().substring(1,9).split(",");
            options.put(splitOption[0].trim(), new String[] {destinations[0], destinations[1].trim()});
        }

        while (!currentLocation.equals("ZZZ")) {
            if (currentInstructionCount > maxInstructionCount) {
                currentInstructionCount = 0;
            }
            currentLocation = instructions.charAt(currentInstructionCount) == 'R'
                ? options.get(currentLocation)[1]
                : options.get(currentLocation)[0];

            sum++;
            currentInstructionCount++;
        }

        return sum;
    }

    @Override
    // same as 1 only now got to traverse with multiple start points simultaneously
    // and find a solution at the same time, looping, synchronization, LCM...
    public Object solve2() {
        var lines = getDataLines();
        var instructionString = lines.getFirst().toCharArray();
        var instructions = new int[instructionString.length];
        for (int i = 0; i < instructionString.length; i++) {
            instructions[i] = instructionString[i] == 'R' ? 1 : 0;
        }

        var options = new HashMap<String, String[]>();
        for (int i = 2; i < lines.size(); i++) {
            var splitOption = lines.get(i).split("=");
            var start = splitOption[0].trim();
            var destinations = splitOption[1].trim().substring(1,9).split(",");
            options.put(start, new String[] {destinations[0], destinations[1].trim()});
        }

        var currentLocations = options.keySet().stream().filter(x -> x.endsWith("A")).toArray(String[]::new);
        var locationSteps = new long[currentLocations.length];

        for (int i = 0; i < currentLocations.length; i++) {
            var current = currentLocations[i];
            var maxInstructionCount = instructions.length - 1;
            var currentInstructionCount = 0;
            var internalSum = 0;
            while (!current.endsWith("Z")) {
                if (currentInstructionCount > maxInstructionCount) {
                    currentInstructionCount = 0;
                }
                var instruction = instructions[currentInstructionCount];
                current = options.get(current)[instruction];
                internalSum++;
                currentInstructionCount++;
            }

            locationSteps[i] = internalSum;
        }

        var sum = locationSteps[0];

        for (int i = 1; i < locationSteps.length; i++) {
            sum = lcm(sum, locationSteps[i]);
        }

        return sum;
    }

    private long lcm(long a, long b) {
        var high = Math.max(a, b);
        var low = Math.min(a, b);
        var lcm = high;
        while (lcm % low != 0) {
            lcm += high;
        }
        return lcm;
    }
}
