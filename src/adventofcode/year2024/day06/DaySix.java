package adventofcode.year2024.day06;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adventofcode.utilities.Coordinate;
import adventofcode.utilities.FileLineReader;

public class DaySix {

    private enum CompletionStatus {
        EXIT, LOOP;
    }

    public static void main(String[] args) {
        FileLineReader reader = new FileLineReader();
//        List<String> lines = reader.readLines(DaySix.class.getResource("test_input.txt"));
        List<String> lines = reader.readLines(DaySix.class.getResource("input.txt"));

        long part1Start = System.currentTimeMillis();
        
        Coordinate position = findInitialPosition(lines);
        traverseMap(lines, new Coordinate(position));

        long part2Start = System.currentTimeMillis();
        
        findLoopPositions(lines, new Coordinate(position));
        
        long end = System.currentTimeMillis();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %dms", part2Start - part1Start));
        System.out.println(String.format("Part 2 duration: %dms", end - part2Start));
    }
    
    private static void traverseMap(List<String> lines, Coordinate position) {
        Direction direction = Direction.UP;
        while (true) {
            int x = position.getX();
            int y = position.getY();
            
            String line = lines.get(y);
            String newLine = line.substring(0, x) + "X" + line.substring(x + 1);
            lines.remove(y);
            lines.add(y, newLine);

            int xAhead = x + direction.getMoveX();
            int yAhead = y + direction.getMoveY();
            if (
                    xAhead < 0 || xAhead >= lines.get(0).length()
                    ||
                    yAhead < 0 || yAhead >= lines.size()
                    ) {
                break;
            }
            
            String ahead = lines.get(yAhead).substring(xAhead, xAhead + 1);
            if (ahead.equals("#")) {
                direction = direction.getNextDirection();
            } else {
                position.setX(xAhead);
                position.setY(yAhead);
            }
        }
        
        long uniquePositions = lines.stream().flatMap(line -> Arrays.asList(line.split("")).stream())
        .filter("X"::equals)
        .count();
        
        System.out.println(String.format("Unique positions traversed: %d", uniquePositions));
    }
    
    private static void findLoopPositions(List<String> lines, Coordinate position) {
        Map locations = new Map();
        
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                String pos = line.substring(x, x + 1);
                boolean blocked = pos.equals("#");
                Location location = new Location(blocked);
                
                locations.addLocation(x, y, location);
            }
        }

        Set<Coordinate> loopPositions = new HashSet<>();
        Direction direction = Direction.UP;
        while (true) {
            int x = position.getX();
            int y = position.getY();

            int xAhead = x + direction.getMoveX();
            int yAhead = y + direction.getMoveY();
            if (
                    xAhead < 0 || xAhead >= locations.getWidth()
                    ||
                    yAhead < 0 || yAhead >= locations.getLength()
                    ) {
                break;
            }

            Location here = locations.getLocation(x, y);
            Location ahead = locations.getLocation(xAhead, yAhead);
            
            if (ahead.isBlocked()) {
                here.addTraversal(direction);
                direction = direction.getNextDirection();
            } else {
                List<Direction> aheadTraversals = ahead.getTraversals();
                if (aheadTraversals.isEmpty()) {
                    Map modifiedMap = new Map(locations);
                    modifiedMap.getLocation(xAhead, yAhead).setBlocked(true);
                    if (findCompletionStatus(modifiedMap, position, direction) == CompletionStatus.LOOP) {
                        loopPositions.add(new Coordinate(xAhead, yAhead));
                    }
                }
                
                here.addTraversal(direction);
                position.setX(xAhead);
                position.setY(yAhead);
            }
        }
        
        System.out.println(String.format("Total loops available: %d", loopPositions.size()));
    }
    
    private static CompletionStatus findCompletionStatus(Map locations, Coordinate position, Direction direction) {
        while (true) {
            int x = position.getX();
            int y = position.getY();
            
            Location here = locations.getLocation(x, y);
            
            if (here.getTraversals().contains(direction)) {
                return CompletionStatus.LOOP;
            }
            
            here.addTraversal(direction);
            
            int xAhead = x + direction.getMoveX();
            int yAhead = y + direction.getMoveY();
            if (
                    xAhead < 0 || xAhead >= locations.getWidth()
                    ||
                    yAhead < 0 || yAhead >= locations.getLength()
                    ) {
                break;
            }
            
            Location ahead = locations.getLocation(xAhead, yAhead);
            if (ahead.isBlocked()) {
                direction = direction.getNextDirection();
            } else {
                position.setX(xAhead);
                position.setY(yAhead);
            }
        }
        
        return CompletionStatus.EXIT;
    }

    private static Coordinate findInitialPosition(List<String> lines) {
        for (int y = 0; y < lines.get(0).length(); y++) {
            int x = lines.get(y).indexOf("^");
            if (x != -1) {
                return new Coordinate(x, y);
            }
        }
        
        throw new IllegalArgumentException("No initial position found");
    }

}
