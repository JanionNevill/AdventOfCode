package adventofcode.year2024.day10;

import static java.lang.Math.abs;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import adventofcode.utilities.Coordinate;
import adventofcode.utilities.Direction;
import adventofcode.utilities.FileLineReader;
import adventofcode.utilities.Grid;

import java.util.Set;

public class DayTen {

    public static void main(String[] args) throws InterruptedException {
//        Grid<Integer> grid = readInput("tiny_test_input.txt");
//        Grid<Integer> grid = readInput("tiny_test_input2.txt");
//        Grid<Integer> grid = readInput("test_input.txt");
        Grid<Integer> grid = readInput("input.txt");

        long part1Start = System.currentTimeMillis();
        
        Map<Coordinate, Set<Coordinate>> possiblePaths = findTotalPathScoreToAllEnds(grid);

        long part2Start = System.currentTimeMillis();
        
        findTotalPathScoreViaAllPaths(grid, possiblePaths);

        long end = System.currentTimeMillis();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %dms", part2Start - part1Start));
        System.out.println(String.format("Part 2 duration: %dms", end - part2Start));
    }
    
    private static Map<Coordinate, Set<Coordinate>> findTotalPathScoreToAllEnds(Grid<Integer> grid) {
        Map<Coordinate, Set<Coordinate>> possiblePaths = findPossiblePaths(grid);
        
        int totalPathScore = 0;
        for (Entry<Coordinate, Set<Coordinate>> entry : possiblePaths.entrySet()) {
            Coordinate zero = entry.getKey();
            Set<Coordinate> nines = entry.getValue();
            
            int ninesFound = traverseGridToAllEnds(grid, new LinkedHashSet<>(nines), zero);
            totalPathScore += ninesFound;
        }
        
        System.out.println(String.format("Total path score: %d", totalPathScore));
        
        return possiblePaths;
    }
    
    private static int traverseGridToAllEnds(Grid<Integer> grid, Set<Coordinate> nines, Coordinate startingPosition) {
        int possibleNines = nines.size();
        recursivelyTraverseGridToAllEnds(grid, nines, startingPosition, 0, null);
        
        return possibleNines - nines.size();
    }
    
    private static void recursivelyTraverseGridToAllEnds(Grid<Integer> grid, Set<Coordinate> nines, Coordinate currentPosition, int currentElevation, Direction lastMove) {
        if (currentElevation == 9) {
            nines.remove(currentPosition);
            return;
        } else if (nines.isEmpty()) {
            return;
        }
        
        boolean anyNinePossible = false;
        for (Coordinate nine : nines) {
            if (abs(nine.getX() - currentPosition.getX()) + abs(nine.getY() - currentPosition.getY()) <= 9 - currentElevation) {
                anyNinePossible = true;
                break;
            }
        }
        if (!anyNinePossible) {
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
                recursivelyTraverseGridToAllEnds(grid, nines, new Coordinate(nextX, nextY), nextElevation, direction);
                
                if (nines.isEmpty()) {
                    return;
                }
            }
        }
    }
    
    private static void findTotalPathScoreViaAllPaths(Grid<Integer> grid, Map<Coordinate, Set<Coordinate>> possiblePaths) {
        int totalPathScore = 0;
        for (Entry<Coordinate, Set<Coordinate>> entry : possiblePaths.entrySet()) {
            Coordinate zero = entry.getKey();
            Set<Coordinate> nines = entry.getValue();
            
            int ninesFound = traverseGridViaAllPaths(grid, new LinkedHashSet<>(nines), zero);
            totalPathScore += ninesFound;
        }
        
        System.out.println(String.format("Total path score: %d", totalPathScore));
    }
    
    private static int traverseGridViaAllPaths(Grid<Integer> grid, Set<Coordinate> nines, Coordinate startingPosition) {
        return recursivelyTraverseGridViaAllPaths(grid, nines, startingPosition, 0, null, 0);
    }
    
    private static int recursivelyTraverseGridViaAllPaths(Grid<Integer> grid, Set<Coordinate> nines, Coordinate currentPosition, int currentElevation, Direction lastMove, int currentCount) {
        if (currentElevation == 9) {
            return currentCount + 1;
        }
        
//        boolean anyNinePossible = false;
//        for (Coordinate nine : nines) {
//            if (abs(nine.getX() - currentPosition.getX()) + abs(nine.getY() - currentPosition.getY()) <= 9 - currentElevation) {
//                anyNinePossible = true;
//                break;
//            }
//        }
//        if (!anyNinePossible) {
//            return currentCount;
//        }
        
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
                currentCount = recursivelyTraverseGridViaAllPaths(grid, nines, new Coordinate(nextX, nextY), nextElevation, direction, currentCount);
            }
        }
        
        return currentCount;
    }
    
    private static Map<Coordinate, Set<Coordinate>> findPossiblePaths(Grid<Integer> grid) {
        Set<Coordinate> zeros = new LinkedHashSet<>();
        Set<Coordinate> nines = new LinkedHashSet<>();
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getLength(); y++) {
                int elevation = grid.getCell(x, y);
                if (elevation == 0) {
                    zeros.add(new Coordinate(x, y));
                } else if (elevation == 9) {
                    nines.add(new Coordinate(x, y));
                }
            }
        }
        
        Map<Coordinate, Set<Coordinate>> possiblePaths = new LinkedHashMap<>();
        for (Coordinate zero : zeros) {
            for (Coordinate nine : nines) {
                if (abs(nine.getX() - zero.getX()) + abs(nine.getY() - zero.getY()) <= 9) {
                    possiblePaths.computeIfAbsent(zero, ignored -> new LinkedHashSet<>()).add(nine);
                }
            }
        }
        
        return possiblePaths;
    }
    
    private static Grid<Integer> readInput(String filePath) {
        FileLineReader reader = new FileLineReader();
        List<String> lines = reader.readLines(DayTen.class.getResource(filePath));
        
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
