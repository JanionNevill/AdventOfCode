package adventofcode.year2024.day13;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adventofcode.utilities.FileLineReader;
import adventofcode.utilities.LongCoordinate;

public class DayThirteen {

    public static void main(String[] args) {
//        List<ClawMachine> machines = Arrays.asList(new ClawMachine(new Coordinate(3, 3), new Coordinate(1, 1), new Coordinate(1, 1)));
        
//        List<ClawMachine> machines = readInput("test_input.txt");
        List<ClawMachine> machines = readInput("input.txt");
        
        Instant partOneStart = Instant.now();

        calculateCosts(machines, false);

        Instant partTwoStart = Instant.now();

        machines.forEach(machine -> machine.movePrize(10000000000000L, 10000000000000L));
        calculateCosts(machines, false);

        Instant end = Instant.now();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %d ms", partOneStart.until(partTwoStart, ChronoUnit.MILLIS)));
        System.out.println(String.format("Part 2 duration: %d ms", partTwoStart.until(end, ChronoUnit.MILLIS)));

//        System.out.println();
//
//        Benchmarker<Grid<String>, List<GardenRegion>> benchmarker = new Benchmarker<>(
//                input -> DayThirteen.calculateFenceCostByPerimeter(input),
//                input -> DayThirteen.calculateFenceCostBySides(input));
//
//        benchmarker.runBenchmark(garden, regions, 10, 100);
    }
    
    private static void calculateCosts(List<ClawMachine> machines, boolean verbose) {
        long totalCost = 0;
        
        for (ClawMachine machine : machines) {
            totalCost += machine.calculateCostToWin(3, 1, verbose);
        }
        
        System.out.println(String.format("Total cost for prizes: %d", totalCost));
    }
    
    private static List<ClawMachine> readInput(String filePath) {
        FileLineReader reader = new FileLineReader();
        List<String> lines = reader.readLines(DayThirteen.class.getResource(filePath));

        Pattern pattern = Pattern.compile("X[+=]([0-9]+), Y[+=]([0-9]+)");

        LongCoordinate incrementA = null;
        LongCoordinate incrementB = null;
        LongCoordinate prize = null;
        List<ClawMachine> machines = new ArrayList<>();
        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }

            Matcher matcher = pattern.matcher(line);
            if (!matcher.find()) {
                throw new IllegalArgumentException("Cannot extract coordinates from line: " + line);
            }
            
            if (line.startsWith("Button A")) {
                incrementA = new LongCoordinate(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)));
            } else if (line.startsWith("Button B")) {
                incrementB = new LongCoordinate(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)));
            } else {
                prize = new LongCoordinate(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)));
            }
            
            if (incrementA != null && incrementB != null && prize != null) {
                machines.add(new ClawMachine(prize, incrementA, incrementB));

                incrementA = null;
                incrementB = null;
                prize = null;
            }
        }
        
        return machines;
    }

}
