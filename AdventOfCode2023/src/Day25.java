import java.util.*;

public class Day25 extends Solver {
    @Override
    // Got bunch of edges and vertices. Got to find the 3 edges that are holding the 2 groups together
    public Object solve() {
        var vertices = new ArrayList<String>();
        var edges = new ArrayList<String[]>();
        for (var line : getDataLines()) {
            var objectAndRelations = line.split(":");
            var node1 = objectAndRelations[0];
            if (!vertices.contains(node1)) {
                vertices.add(node1);
            }

            for (var node2: Arrays.stream(objectAndRelations[1].split(" "))
                    .filter(x -> !x.isEmpty())
                    .map(String::trim)
                    .toArray(String[]::new)) {

                if (!vertices.contains(node2)) {
                    vertices.add(node2);
                }

                if (edges.stream().noneMatch(x -> (x[0].equals(node1) && x[1].equals(node2))
                        || (x[1].equals(node1) && x[0].equals(node2)))) {
                    edges.add(new String[] {node1, node2});
                }
            }
        }

        return kargersAlgorithm(vertices, edges);
    }

    private long kargersAlgorithm(ArrayList<String> vertices, ArrayList<String[]> edges) {
        var subsets = new ArrayList<ArrayList<String>>();
        do
        {
            subsets = new ArrayList<>();

            for (var vertex : vertices) {
                var temp = new ArrayList<String>();
                temp.add(vertex);
                subsets.add(temp);
            }

            int i;
            ArrayList<String> subset1, subset2;

            var m = new Random();

            while (subsets.size() > 2)
            {;
                i = m.nextInt(edges.size());
                var edge = edges.get(i);
                subset1 = subsets.stream().filter(s -> s.contains(edge[0])).findFirst().get();
                subset2 = subsets.stream().filter(s -> s.contains(edge[1])).findFirst().get();

                if (subset1.size() == subset2.size() && subset1.containsAll(subset2)) continue;
                var subset2Index = -1;
                for (int j = 0; j < subsets.size(); j++) {
                    if (subsets.get(j).contains(edge[1])) {
                        subset2Index = j;
                        break;
                    }
                }
                subsets.remove(subset2Index);
                subset1.addAll(subset2);
            }

        } while (CountCuts(subsets, edges) != 3);

        return (long) subsets.get(0).size() * subsets.get(1).size();
    }

    private int CountCuts(ArrayList<ArrayList<String>> subsets, ArrayList<String[]> edges)
    {
        var cuts = 0;
        for (var edge : edges) {
            var subset1 = subsets.stream().filter(s -> s.contains(edge[0])).findFirst().get();
            var subset2 = subsets.stream().filter(s -> s.contains(edge[1])).findFirst().get();

            if (subset1.size() != subset2.size() || !subset1.containsAll(subset2)) cuts++;
        }

        return cuts;
    }

    private boolean compareSubsets(ArrayList<String> a, ArrayList<String> b) {
        return b.containsAll(a);
    }

    @Override
    //
    public Object solve2() {
        var map = getDataLines().stream().map(String::toCharArray).toArray(char[][]::new);
        return 0;
    }
}