import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;

public class Day05 extends Solver {
    @Override
    // Got numbers and multiple sequential transformation maps, got to get the lowest final value
    public Object solve() {
        var lines = getDataLines();

        var numbersToMap = Arrays.stream(lines
                .getFirst()
                .split(":")[1]
                .trim()
                .split(" "))
            .filter(x -> !x.isEmpty())
            .map(Long::parseLong)
            .toList();

        var mappers = new ArrayList<Long[]>();

        for (String mapLine : lines) {

            // process next set of mappers
            if (mapLine.isEmpty() || !Character.isDigit(mapLine.charAt(0))) {
                if (!mappers.isEmpty()) {
                    numbersToMap = mapNumbers(numbersToMap, mappers);
                    mappers.clear();
                }
                continue;
            }

            var map = Arrays.stream(mapLine
                            .trim()
                            .split(" "))
                    .filter(x -> !x.isEmpty())
                    .map(Long::parseLong)
                    .toArray(Long[]::new);
            mappers.add(map);
        }

        if (!mappers.isEmpty()) {
            numbersToMap = mapNumbers(numbersToMap, mappers);
        }

        return Collections.min(numbersToMap);
    }

    private List<Long> mapNumbers(List<Long> numbersToMap, List<Long[]> mappers) {
        var response = new ArrayList<Long>();
        for (Long toMap : numbersToMap) {
            var foundMap = false;

            for (Long[] map : mappers) {
                if (toMap >= map[1] && toMap <= (map[1] + map[2])) {
                    response.add(map[0] + (toMap - map[1]));
                    foundMap = true;
                    break;
                }
            }

            if (!foundMap) {
                response.add(toMap);
            }
        }
        return response;
    }

    @Override
    // Same as 1, only now the seeds are in range, first number start, second count, and so on.
    public Object solve2() {
        var lines = getDataLines();
        var seeds = lines
            .getFirst()
            .split(":")[1]
            .trim()
            .split(" ");
        Queue<Long> queue = new LinkedList<>(Arrays.stream(seeds)
                .filter(x -> !x.isEmpty())
                .map(Long::parseLong)
                .toList());
        var numberRangesToMap = new ArrayList<Long[]>();

        while(!queue.isEmpty()) {
            numberRangesToMap.add(new Long[] {queue.poll(), queue.poll()});
        }
        var allMappers = getAllMappers(lines);
        var calculatedRanges = mapRanges(numberRangesToMap, allMappers);
        var result = calculatedRanges
                .stream()
                .min(Comparator.comparing(x -> x[0]))
                .orElseThrow(NoSuchElementException::new);

        return result[0];
    }

    private ArrayList<ArrayList<Long[]>> getAllMappers(List<String> lines) {
        var result = new ArrayList<ArrayList<Long[]>>();
        var singleMapperList = new ArrayList<Long[]>();

        for (String mapLine : lines) {
            if (mapLine.isEmpty() || !Character.isDigit(mapLine.charAt(0))) {
                if (!singleMapperList.isEmpty()) {
                    result.add(singleMapperList);
                    singleMapperList = new ArrayList<Long[]>();
                }
                continue;
            }

            var map = Arrays.stream(mapLine
                            .trim()
                            .split(" "))
                    .filter(x -> !x.isEmpty())
                    .map(Long::parseLong)
                    .toArray(Long[]::new);
            singleMapperList.add(map);
        }

        if (!singleMapperList.isEmpty()) {
            result.add(singleMapperList);
        }

        return result;
    }

    private LinkedList<Long[]> mapRanges(ArrayList<Long[]> numbersToMap, ArrayList<ArrayList<Long[]>> allMappers) {
        var waitingMapping = new LinkedList<>(numbersToMap);
        var failedMapping = new LinkedList<Long[]>();
        var calculatedNumbers = new LinkedList<Long[]>();

        for (ArrayList<Long[]> mappers : allMappers) {
            for (Long[] mapper : mappers) {
                while (!waitingMapping.isEmpty()) {
                    var toMap = waitingMapping.poll();
                    var startMap = mapper[1];
                    var endMap = mapper[1] + mapper[2] - 1;
                    var startNumber = toMap[0];
                    var endNumber = toMap[0] + toMap[1] - 1;

                    if (startMap <= endNumber && endMap >= startNumber) {
                        // whole number range fits inside map
                        if (startMap <= startNumber && endMap >= endNumber) {
                            var newStart = mapper[0] + (startNumber - startMap);
                            calculatedNumbers.add(new Long[] {newStart, toMap[1]});
                            // left part of number range is inside map
                        } else if (startMap <= startNumber) {
                            var range = (endMap - startNumber) + 1;
                            var newStart = mapper[0] + (startNumber - startMap);
                            calculatedNumbers.add(new Long[] {newStart, range});
                            var waitingRangeRight = (endNumber - endMap) + 1;
                            failedMapping.add(new Long[] {endMap, waitingRangeRight});
                            // right part of number range is inside map
                        } else if (endMap >= endNumber) {
                            var range = (endNumber - startMap) + 1;
                            var newStart = mapper[0];
                            calculatedNumbers.add(new Long[] {newStart, range});
                            var waitingRangeLeft = (startMap - startNumber) + 1;
                            failedMapping.add(new Long[] {startNumber, waitingRangeLeft});
                            // whole map is in number range
                        } else {
                            calculatedNumbers.add(new Long[] {mapper[0], mapper[2]});
                            var waitingRangeRight = (endNumber - endMap) + 1;
                            failedMapping.add(new Long[] {endMap, waitingRangeRight});
                            var waitingRangeLeft = (startMap - startNumber) + 1;
                            failedMapping.add(new Long[] {startNumber, waitingRangeLeft});
                        }
                    } else {
                        failedMapping.add(toMap);
                    }
                }
                waitingMapping = failedMapping;
                failedMapping = new LinkedList<>();
            }
            waitingMapping.addAll(calculatedNumbers);
            calculatedNumbers = new LinkedList<>();
        }
        return waitingMapping;
    }
}
