import java.util.ArrayList;

public class Day15 extends Solver {
    @Override
    //
    public Object solve() {
        long sum = 0L;
        var lines = getDataLines();
        for (var input:
             lines.get(0).split(",")) {
            sum += hashLable(input);
        }
        return sum;
    }

    private int hashLable(String label) {
        if (label.isEmpty()) {
            return 0;
        }
        var current = 0;
        for (var character: label.toCharArray()) {
            current += (int)character;
            current *= 17;
            current = current % 256;
        }
        return current;
    }

    @Override
    // input gives label and instruction, hash label for box index, follow instruction
    // I don't know either
    public Object solve2() {
        long sum = 0L;
        ArrayList<Lens>[] boxes = new ArrayList[256];

        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new ArrayList<>();
        }
        
        var lines = getDataLines();
        for (var input:
                lines.get(0).split(",")) {
            if (input.contains("=")) {
                var labelInstruction = input.split("=");
                var boxIndex = hashLable(labelInstruction[0]);
                var foundElement = false;
                for (int i = 0; i < boxes[boxIndex].size(); i++) {
                    if (boxes[boxIndex].get(i).label.equals(labelInstruction[0])) {
                        boxes[boxIndex].set(i, new Lens(labelInstruction[0], Integer.parseInt(labelInstruction[1])));
                        foundElement = true;
                        break;
                    }
                }
                if (!foundElement) {
                    boxes[boxIndex].add(new Lens(labelInstruction[0], Integer.parseInt(labelInstruction[1])));
                }
            } else {
                var labelInstruction = input.split("-");
                var boxIndex = hashLable(labelInstruction[0]);
                for (int i = 0; i < boxes[boxIndex].size(); i++) {
                    if (boxes[boxIndex].get(i).label.equals(labelInstruction[0])) {
                        boxes[boxIndex].remove(i);
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < boxes.length; i++) {
            var box = boxes[i];
            if (box.isEmpty()) {
                continue;
            }
            for (int j = 0; j < box.size(); j++) {
                var lensValue = (1 + i) * (1 + j) * box.get(j).focalLength;
//                System.out.println(i + " " + j + " " + lensValue);
                sum += lensValue;
            }
        }

        return sum;
    }

}

class Lens {
    public Lens(String label, int focalLength) {
        this.label = label;
        this.focalLength = focalLength;
    }
    public String label;
    public int focalLength;
}