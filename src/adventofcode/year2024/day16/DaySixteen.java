package adventofcode.year2024.day16;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import adventofcode.utilities.Coordinate;
import adventofcode.utilities.Direction;
import adventofcode.utilities.FileLineReader;
import adventofcode.utilities.Grid;

public class DaySixteen {
    
    private static class Input {
        
        private final Grid<Boolean> wallGrid;
        private final Coordinate start;
        private final Coordinate end;
        
        public Input(Grid<Boolean> wallGrid, Coordinate start, Coordinate end) {
            this.wallGrid = wallGrid;
            this.start = start;
            this.end = end;
        }
        public Grid<Boolean> getWallGrid() {
            return wallGrid;
        }
        public Coordinate getStart() {
            return start;
        }
        public Coordinate getEnd() {
            return end;
        }
        
    }

    public static void main(String[] args) throws InterruptedException {
//        Input input = readInput("test_input.txt");
//        Input input = readInput("test_input2.txt");
        Input input = readInput("input.txt");

        Instant partOneStart = Instant.now();
        
        findLowestScoringPath(input.getWallGrid(), input.getStart(), input.getEnd(), true);

        Instant partTwoStart = Instant.now();

        Instant end = Instant.now();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %d ms", partOneStart.until(partTwoStart, ChronoUnit.MILLIS)));
        System.out.println(String.format("Part 2 duration: %d ms", partTwoStart.until(end, ChronoUnit.MILLIS)));

//        System.out.println();
//
//        Benchmarker<Grid<String>, List<GardenRegion>> benchmarker = new Benchmarker<>(
//                input -> DayFifteen.calculateFenceCostByPerimeter(input),
//                input -> DayFifteen.calculateFenceCostBySides(input));
//
//        benchmarker.runBenchmark(garden, regions, 10, 100);
    }
    
    private static void findLowestScoringPath(Grid<Boolean> wallGrid, Coordinate startPosition, Coordinate endPosition, boolean verbose) {
        long bestPathScore = traverseGrid(wallGrid, startPosition, endPosition, verbose);
        
        System.out.println(String.format("Best path score: %d", bestPathScore));
    }
    
    private static long traverseGrid(Grid<Boolean> wallGrid, Coordinate startPosition, Coordinate endPosition, boolean verbose) {
        return recursivelyTraverseGrid(wallGrid, startPosition, endPosition, Direction.RIGHT, new ArrayList<>(), 0, Long.MAX_VALUE, verbose);
    }
    
    private static long recursivelyTraverseGrid(Grid<Boolean> wallGrid, Coordinate currentPosition, Coordinate endPosition, Direction lastMove, List<Coordinate> currentPath, long currentScore, long currentBestScore, boolean verbose) {
        if (currentScore >= currentBestScore) {
            return currentBestScore;
        } else if (currentPosition.equals(endPosition)) {
            if (verbose) {
                System.out.println(currentScore);
            }
            return currentScore;
        }
        
        for (Direction direction : Direction.ORTHOGONAL_DIRECTIONS) {
            // Don't turn back on yourself if you've already made your first move
            if (currentScore > 0 && direction == lastMove.turnBackwards()) {
                continue;
            }

            int nextX = currentPosition.getX() + direction.getMoveX();
            int nextY = currentPosition.getY() + direction.getMoveY();
            
            // Don't walk off the map or into a wall
            if (nextX < 0 || nextX >= wallGrid.getWidth()
                    || nextY < 0 || nextY >= wallGrid.getLength()
                    || wallGrid.getCell(nextX, nextY)) {
                continue;
            }
            
            Coordinate nextPosition = new Coordinate(nextX, nextY);
            if (currentPath.contains(nextPosition)) {
                continue;
            }
            
            long newScore = currentScore + 1;
            if (!direction.equals(lastMove)) {
                newScore += 1000;
                if (direction.equals(lastMove.turnBackwards())) {
                    newScore += 1000;
                }
            }
            
            currentPath.add(nextPosition);
            currentBestScore = recursivelyTraverseGrid(wallGrid, new Coordinate(nextX, nextY), endPosition, direction, currentPath, newScore, currentBestScore, verbose);
            currentPath.remove(nextPosition);
        }
        
        return currentBestScore;
    }
    
    private static Input readInput(String filePath) {
        FileLineReader reader = new FileLineReader();
        List<String> lines = reader.readLines(DaySixteen.class.getResource(filePath));
        
        Grid<Boolean> wallGrid = new Grid<>();
        Coordinate start = null;
        Coordinate end = null;
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                String cell = line.substring(x, x + 1);
                wallGrid.addCell(x, y, cell.equals("#"));
                
                if (cell.equals("S")) {
                    start = new Coordinate(x, y);
                } else if (cell.equals("E")) {
                    end = new Coordinate(x, y);
                }
            }
        }
        
        return new Input(wallGrid, start, end);
    }

}
