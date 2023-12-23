import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Day22 extends Solver {
    @Override
    // Got a bunch of bricks in 3D, got to find which ones can be removed without the supported ones falling down
    // Only got to find one by one, so can delete brick if it is supporting bricks that are supported by 1 other brick
    public Object solve() {
        var supportGraph = GetGraphForMap();

        return Arrays.stream(supportGraph).filter(s -> s.supporting.isEmpty()
                || s.supporting.stream().allMatch(x -> supportGraph[x].supportedBy.size() > 1))
                .count();
    }

    @Override
    // Get maximum carnage, topple the jenga that topples the most bricks
    // and then count all of the carnage
    public Object solve2() {
        var supportGraph = GetGraphForMap();
        var sum = 0;

        for (var pushedNode : supportGraph) {
            var cache = new ArrayList<Integer>();
            var queue = new LinkedList<>(pushedNode.supporting);
            cache.add(pushedNode.index);
            while (!queue.isEmpty()) {
                var node = supportGraph[queue.poll()];
                if (cache.containsAll(node.supportedBy) && !cache.contains(node.index)) {
                    cache.add(node.index);
                    var nodesToAdd = node.supporting.stream()
                            .filter(x -> !cache.contains(x) && !queue.contains(x))
                            .toList();
                    queue.addAll(nodesToAdd);
                }
            }
            sum += cache.size() - 1;
        }
        
        return sum;
    }

    private SupportGraph[] GetGraphForMap() {
        var map = getDataLines()
                .stream()
                .map(x -> Arrays.stream(x.replace('~', ',').split(","))
                        .map(Integer::parseInt).toArray(Integer[]::new))
                .sorted(new Comparator<Integer[]>() {
                    @Override
                    public int compare(Integer[] o1, Integer[] o2) {
                        return o1[2].equals(o2[2])
                                ? o2[5] - o1[5]
                                : o1[2] - o2[2];
                    }
                }).collect(Collectors.toList());

        for (int i = 0; i < map.size(); i++) {
            var brick = map.get(i);

            var previousZ = brick[2];
            do {
                previousZ = brick[2];
                var hasSupportingStructure = false;
                for (int j = i - 1; j >= 0; j--) {
                    var belowBrick = map.get(j);
                    if (belowBrick[5].equals(previousZ - 1)) {
                        var isXMisaligned = belowBrick[0] > brick[3] || brick[0] > belowBrick[3];
                        var isYMisaligned = belowBrick[1] > brick[4] || brick[1] > belowBrick[4];
                        if (!isXMisaligned && !isYMisaligned) {
                            hasSupportingStructure = true;
                            break;
                        }
                    }
                }
                if (!hasSupportingStructure && brick[2] > 1) {
                    brick[2]--;
                    brick[5]--;
                }
            } while(!previousZ.equals(brick[2]));
        }


        map.sort(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                return o1[2] - o2[2];
            }
        });

        var supportGraph = new SupportGraph[map.size()];
        for (int i = 0; i < supportGraph.length; i++) {
            supportGraph[i] = new SupportGraph(i);
        }

        for (int i = 0; i < map.size(); i++) {
            var brick = map.get(i);
            for (int j = i + 1; j < map.size(); j++) {
                var aboveBrick = map.get(j);
                if (aboveBrick[2].equals(brick[5] + 1)) {
                    var isXMisaligned = aboveBrick[0] > brick[3] || brick[0] > aboveBrick[3];
                    var isYMisaligned = aboveBrick[1] > brick[4] || brick[1] > aboveBrick[4];
                    if (!isXMisaligned && !isYMisaligned) {
                        supportGraph[i].supporting.add(j);
                        supportGraph[j].supportedBy.add(i);
                    }
                }
            }
        }
        return supportGraph;
    }
}

class SupportGraph {
    public SupportGraph(int index) {
        this.index = index;
        supporting = new ArrayList<>();
        supportedBy = new ArrayList<>();
    }
    public int index;
    public ArrayList<Integer> supporting;
    public ArrayList<Integer> supportedBy;
}