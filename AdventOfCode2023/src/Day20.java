import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Day20 extends Solver {
    @Override
    // Got instructions, each instruction component has the option of remembering state
    // and changing what it does based on input and remembered state
    public Object solve() {
        var instructions = new HashMap<String, Instruction>();
        var broadcastDestinations = new String[0];
        var queue = new LinkedList<String[]>();
        for (var line: getDataLines()) {
            var sourceAndDestination = line.split(" -> ");
            var sourceBlock = sourceAndDestination[0];

            if (sourceBlock.equals("broadcaster")) {
                broadcastDestinations = Arrays.stream(sourceAndDestination[1].split(","))
                        .filter(x -> !x.isEmpty())
                        .map(String::trim)
                        .toArray(String[]::new);
                continue;
            }

            var action = sourceBlock.charAt(0);
            var source = sourceBlock.substring(1);
            var destinations = Arrays.stream(sourceAndDestination[1].split(","))
                    .filter(x -> !x.isEmpty())
                    .map(String::trim)
                    .toArray(String[]::new);
            instructions.put(source, new Instruction(action, false, destinations));
        }

        for (var sourceKey : instructions.keySet()) {
            var instruction = instructions.get(sourceKey);
            if (instruction.action == '&') {
                for (var key : instructions.keySet()) {
                    var foundInstruction = instructions.get(key);
                    if (Arrays.asList(foundInstruction.destinations).contains(sourceKey)) {
                        instruction.sourceStates.put(key, false);
                    }
                }
            }
        }

        var lowCount = 0L;
        var highCount = 0L;

        for (int i = 0; i < 1000; i++) {
            lowCount++;
            for (var broadcastDestination : broadcastDestinations) {
                queue.add(new String[] { broadcastDestination, "broadcaster", "low" });
                lowCount++;
            }
            while(!queue.isEmpty()) {
                var wave = queue.poll();
                if (!instructions.containsKey(wave[0])) {
                    continue;
                }
                var instruction = instructions.get(wave[0]);
                if (instruction.action == '%' && wave[2].equals("low")) {
                    instruction.state = !instruction.state;
                    for (var destination : instruction.destinations) {
                        if (instruction.state) {
                            queue.add(new String[] { destination, wave[0], "high"});
                            highCount++;
                        } else {
                            queue.add(new String[] { destination, wave[0], "low"});
                            lowCount++;
                        }
                    }
                } else if (instruction.action == '&') {
                    instruction.sourceStates.put(wave[1], !wave[2].equals("low"));
                    var allHigh = true;

                    for (var sourceKey : instruction.sourceStates.keySet()) {
                        if (!instruction.sourceStates.get(sourceKey)) {
                            allHigh = false;
                            break;
                        }
                    }

                    for (var destination : instruction.destinations) {
                        if (allHigh) {
                            queue.add(new String[] { destination, wave[0], "low"});
                            lowCount++;
                        } else {
                            queue.add(new String[] { destination, wave[0], "high"});
                            highCount++;
                        }
                    }
                }
            }
        }

        return lowCount * highCount;
    }



    @Override
    // so count the amount of pulses rx gets, if it's exactly 1 low pulse per button press, then return the counter
    // turns out it doesn't happen that often, so get base part count and lcm
    public Object solve2() {
        var instructions = getInstructions();
        var queue = new LinkedList<String[]>();

        var line = getDataLines().stream().filter(x -> x.startsWith("broadcaster")).findFirst().get();
        var broadcastDestinations = Arrays.stream(line.split(" -> ")[1].split(","))
            .filter(x -> !x.isEmpty())
            .map(String::trim)
            .toArray(String[]::new);


        var conjucatedParts = new String[0];
        for (var sourceKey : instructions.keySet()) {
            var instruction = instructions.get(sourceKey);
            if (instruction.destinations[0].equals("rx")) {
                conjucatedParts = instruction.sourceStates.keySet().toArray(String[]::new);
                break;
            }
        }

        var conjucatedPartCount = new int[conjucatedParts.length];
        for (int i = 0; i < conjucatedParts.length; i++) {
            var partName = conjucatedParts[i];
            var partCount = 0;

            var foundAnswer = false;
            while (!foundAnswer) {
                var lowCount = 0L;
                partCount++;
                for (var broadcastDestination : broadcastDestinations) {
                    queue.add(new String[] { broadcastDestination, "broadcaster", "low" });
                }
                while(!queue.isEmpty()) {
                    var wave = queue.poll();
                    if (wave[0].equals(partName) && wave[2].equals("low")) {
                        lowCount++;
                    }
                    if (!instructions.containsKey(wave[0])) {
                        continue;
                    }
                    var instruction = instructions.get(wave[0]);
                    if (instruction.action == '%' && wave[2].equals("low")) {
                        instruction.state = !instruction.state;
                        for (var destination : instruction.destinations) {
                            if (instruction.state) {
                                queue.add(new String[] { destination, wave[0], "high"});
                            } else {
                                queue.add(new String[] { destination, wave[0], "low"});
                            }
                        }
                    } else if (instruction.action == '&') {
                        instruction.sourceStates.put(wave[1], !wave[2].equals("low"));
                        var allHigh = true;

                        for (var sourceKey : instruction.sourceStates.keySet()) {
                            if (!instruction.sourceStates.get(sourceKey)) {
                                allHigh = false;
                                break;
                            }
                        }



                        for (var destination : instruction.destinations) {
                            if (allHigh) {
                                queue.add(new String[] { destination, wave[0], "low"});
                            } else {
                                queue.add(new String[] { destination, wave[0], "high"});
                            }
                        }
                    }
                }
                if (lowCount > 0) {
                    foundAnswer = true;
                }
            }

            conjucatedPartCount[i] = partCount;
            instructions = getInstructions();
        }

        long sum = conjucatedPartCount[0];
        for (int i = 1; i < conjucatedParts.length; i++) {
            sum = lcm(sum, conjucatedPartCount[i]);
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

    private HashMap<String, Instruction> getInstructions () {
        var instructions = new HashMap<String, Instruction>();

        for (var line: getDataLines()) {
            if (line.startsWith("broadcaster")) {
                continue;
            }

            var sourceAndDestination = line.split(" -> ");
            var sourceBlock = sourceAndDestination[0];
            var action = sourceBlock.charAt(0);
            var source = sourceBlock.substring(1);
            var destinations = Arrays.stream(sourceAndDestination[1].split(","))
                    .filter(x -> !x.isEmpty())
                    .map(String::trim)
                    .toArray(String[]::new);
            instructions.put(source, new Instruction(action, false, destinations));
        }

        for (var sourceKey : instructions.keySet()) {
            var instruction = instructions.get(sourceKey);
            if (instruction.action == '&') {
                for (var key : instructions.keySet()) {
                    var foundInstruction = instructions.get(key);
                    if (Arrays.asList(foundInstruction.destinations).contains(sourceKey)) {
                        instruction.sourceStates.put(key, false);
                    }
                }
            }
        }

        return instructions;
    }
}

class Instruction {
    public Instruction(char action, boolean state, String[] destinations) {
        this.action = action;
        this.state = state;
        this.destinations = destinations;
        sourceStates = new HashMap<>();
    }
    public char action;
    public boolean state;
    public String[] destinations;
    public HashMap<String, Boolean> sourceStates;
}