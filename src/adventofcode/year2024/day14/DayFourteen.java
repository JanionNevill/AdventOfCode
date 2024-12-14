package adventofcode.year2024.day14;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adventofcode.utilities.Coordinate;
import adventofcode.utilities.FileLineReader;

public class DayFourteen {

    public static void main(String[] args) {
//        List<Robot> robots = readInput("test_input.txt");
        List<Robot> robots = readInput("input.txt");

        Instant partOneStart = Instant.now();

//        calculateSafetyFactor(robots, 100, 11, 7);
        calculateSafetyFactor(robots, 100, 101, 103);

        Instant partTwoStart = Instant.now();
        
        findChristmasTree(robots, 101, 103);

        Instant end = Instant.now();
        System.out.println();
        System.out
                .println(String.format("Part 1 duration: %d ms", partOneStart.until(partTwoStart, ChronoUnit.MILLIS)));
        System.out.println(String.format("Part 2 duration: %d ms", partTwoStart.until(end, ChronoUnit.MILLIS)));

//        System.out.println();
//
//        Benchmarker<Grid<String>, List<GardenRegion>> benchmarker = new Benchmarker<>(
//                input -> DayThirteen.calculateFenceCostByPerimeter(input),
//                input -> DayThirteen.calculateFenceCostBySides(input));
//
//        benchmarker.runBenchmark(garden, regions, 10, 100);
    }

    private static void calculateSafetyFactor(List<Robot> robots, int steps, int gridWidth, int gridLength) {
        List<Coordinate> endPositions = robots.stream()
                .map(robot -> robot.findPositionAfter(100, gridWidth, gridLength)).toList();

        int topLeft = 0;
        int bottomLeft = 0;
        int topRight = 0;
        int bottomRight = 0;

        for (Coordinate position : endPositions) {
            int x = position.getX();
            int y = position.getY();

            if (x == (gridWidth - 1) / 2 || y == (gridLength - 1) / 2) {
                continue;
            }

            boolean leftOfCentre = x <= gridWidth / 2;
            boolean aboveCentre = y <= gridLength / 2;

            if (leftOfCentre && aboveCentre) {
                topLeft++;
            } else if (leftOfCentre) {
                bottomLeft++;
            } else if (aboveCentre) {
                topRight++;
            } else {
                bottomRight++;
            }
        }

        System.out.println(String.format("Total safety factor: %d", topLeft * bottomLeft * topRight * bottomRight));
    }

    private static void findChristmasTree(List<Robot> robots, int gridWidth, int gridLength) {
        int steps = 0;
        while (true) {
            int stepss = steps;
            List<Coordinate> endPositions = robots.stream()
                    .map(robot -> robot.findPositionAfter(stepss, gridWidth, gridLength)).toList();
            
            long aligned = endPositions.stream().map(target -> countNeighbours(target, endPositions)).filter(neighbours -> neighbours >= 1).count();
            boolean mostAligned = aligned > endPositions.size() / 2;
            if (mostAligned) {
                System.out.println(String.format("Steps: %d", steps));
                printGrid(endPositions, gridWidth, gridLength);
                
                // Add breakpoint here
                System.out.println();
            }
            
            if (steps + 1 < steps) {
                throw new IllegalStateException("Integer overflow");
            }
            steps++;
        }
    }
    
    private static int countNeighbours(Coordinate target, List<Coordinate> positions) {
        int neighbours = 0;
        for (Coordinate other : positions) {
            if (target == other) {
                continue;
            }

            int deltaX = Math.abs(target.getX() - other.getX());
            int deltaY = Math.abs(target.getY() - other.getY());
            
            if (deltaX <= 1 && deltaY <= 1 && (deltaX == 0) != (deltaY == 0)) {
                neighbours++;
            }
        }
        
        return neighbours;
    }

    private static void printGrid(List<Coordinate> positions, int gridWidth, int gridLength) {
        for (int y = 0; y < gridLength; y++) {
            int yy = y;
            for (int x = 0; x < gridWidth; x++) {
                int xx = x;
                long robots = positions.stream().filter(coord -> coord.getX() == xx && coord.getY() == yy).count();
                
                System.out.print(robots == 0 ? "." : robots);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static List<Robot> readInput(String filePath) {
        FileLineReader reader = new FileLineReader();
        List<String> lines = reader.readLines(DayFourteen.class.getResource(filePath));

        Pattern pattern = Pattern.compile("p=([-0-9]+),([-0-9]+) v=([-0-9]+),([-0-9]+)");

        List<Robot> robots = new ArrayList<>();
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (!matcher.find()) {
                throw new IllegalArgumentException("Cannot extract robot from line: " + line);
            }

            robots.add(new Robot(new Coordinate(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                    new Coordinate(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)))));
        }

        return robots;
    }

}
