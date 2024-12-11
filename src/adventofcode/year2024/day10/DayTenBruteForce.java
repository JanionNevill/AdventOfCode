package adventofcode.year2024.day10;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import adventofcode.utilities.Coordinate;
import adventofcode.utilities.Direction;
import adventofcode.utilities.FileLineReader;
import adventofcode.utilities.Grid;
import adventofcode.utilities.benchmark.Benchmarker;

public class DayTenBruteForce {

    public static void main(String[] args) throws InterruptedException {
//        Grid<Integer> grid = readInput("tiny_test_input.txt");
//        Grid<Integer> grid = readInput("tiny_test_input2.txt");
//        Grid<Integer> grid = readInput("test_input.txt");
//        Grid<Integer> grid = readInput("reddit_input.txt");
        Grid<Integer> grid = readInput("input.txt");

        Instant partOneStart = Instant.now();

        findTotalPathScoreToAllEnds(grid);

        Instant partTwoStart = Instant.now();

        findTotalPathScoreViaAllPaths(grid);

        Instant end = Instant.now();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %d ms", partOneStart.until(partTwoStart, ChronoUnit.MILLIS)));
        System.out.println(String.format("Part 2 duration: %d ms", partTwoStart.until(end, ChronoUnit.MILLIS)));

        System.out.println();

        Benchmarker<Grid<Integer>, Grid<Integer>> benchmarker = new Benchmarker<>(
                DayTenBruteForce::findTotalPathScoreToAllEnds,
                DayTenBruteForce::findTotalPathScoreViaAllPaths);

        benchmarker.runBenchmark(grid, grid, 10, 1000);
    }
    
    private static void findTotalPathScoreToAllEnds(Grid<Integer> grid) {
        int totalPathScore = 0;

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getLength(); y++) {
                int elevation = grid.getCell(x, y);
                if (elevation != 0) {
                    continue;
                }

                Set<Coordinate> nines = new LinkedHashSet<>();
                recursivelyTraverseGrid(grid, new Coordinate(x, y), 0, null, nines);
                
                totalPathScore += nines.size();
            }
        }
        
        System.out.println(String.format("Total path score: %d", totalPathScore));
    }
    
    private static void findTotalPathScoreViaAllPaths(Grid<Integer> grid) {
        int totalPathScore = 0;
        
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getLength(); y++) {
                int elevation = grid.getCell(x, y);
                if (elevation != 0) {
                    continue;
                }

                List<Coordinate> nines = new ArrayList<>();
                recursivelyTraverseGrid(grid, new Coordinate(x, y), 0, null, nines);
                
                totalPathScore += nines.size();
            }
        }

        System.out.println(String.format("Total path score: %d", totalPathScore));
    }
    
    private static void recursivelyTraverseGrid(Grid<Integer> grid, Coordinate currentPosition, int currentElevation, Direction lastMove, Collection<Coordinate> nines) {
        if (currentElevation == 9) {
            nines.add(currentPosition);
            return;
        }
        
        for (Direction direction : Direction.ORTHOGONAL_DIRECTIONS) {
            if (lastMove != null && direction == lastMove.turnBackwards()) {
                continue;
            }

            int nextX = currentPosition.getX() + direction.getMoveX();
            int nextY = currentPosition.getY() + direction.getMoveY();
            
            if (nextX < 0 || nextX >= grid.getWidth() || nextY < 0 || nextY >= grid.getLength()) {
                continue;
            }
            
            int nextElevation = grid.getCell(nextX, nextY);
            if (nextElevation == currentElevation + 1) {
                recursivelyTraverseGrid(grid, new Coordinate(nextX, nextY), nextElevation, direction, nines);
            }
        }
    }
    
    private static Grid<Integer> readInput(String filePath) {
        FileLineReader reader = new FileLineReader();
        List<String> lines = reader.readLines(DayTenBruteForce.class.getResource(filePath));
        
        Grid<Integer> grid = new Grid<>();
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                grid.addCell(x, y, Integer.parseInt(line.substring(x, x + 1)));
            }
        }
        
        return grid;
    }

}
