package adventofcode.year2024.day15;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import adventofcode.utilities.Coordinate;
import adventofcode.utilities.Direction;
import adventofcode.utilities.FileLineReader;
import adventofcode.utilities.Grid;
import adventofcode.utilities.Orientation;
import adventofcode.utilities.Pair;

public class DayFifteen {
    
    static class Input {
        
        private Coordinate robot;
        private Grid<String> grid;
        private List<Direction> directions;
        
        public Input(Coordinate robot, Grid<String> grid, List<Direction> directions) {
            this.robot = robot;
            this.grid = grid;
            this.directions = directions;
        }
        
        public Coordinate getRobot() {
            return robot;
        }
        
        public Grid<String> getGrid() {
            return grid;
        }
        
        public List<Direction> getDirections() {
            return directions;
        }
        
    }

    public static void main(String[] args) {
//        Input input = readInput("test_input.txt");
        Input input = readInput("input.txt");
        Pair<Grid<Boolean>, List<Box>> transformedInput = transformInput(input.getGrid());
        Grid<Boolean> wideGrid = transformedInput.getFirst();
        List<Box> boxes = transformedInput.getSecond();
        Coordinate wideRobot = new Coordinate(input.getRobot().getX() * 2, input.getRobot().getY());
        
        System.out.println(input.getGrid());
        System.out.println();
        printGrid(wideRobot, wideGrid, boxes);

        Instant partOneStart = Instant.now();

//        traverseGrid(input.getRobot(), input.getGrid(), input.getDirections(), true);
        traverseGrid(input.getRobot(), input.getGrid(), input.getDirections(), false);

        Instant partTwoStart = Instant.now();

//        traverseGrid(wideRobot, wideGrid, boxes, input.getDirections(), true);
        traverseGrid(wideRobot, wideGrid, boxes, input.getDirections(), false);

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
    
    //////////////
    // PART ONE //
    //////////////
    
    private static void traverseGrid(Coordinate robot, Grid<String> grid, List<Direction> directions, boolean verbose) {
        for (Direction direction : directions) {
            robot = move(robot, grid, direction);
            
            if (verbose) {
                System.out.println(grid);
                System.out.println();
            }
        }
        
        long totalGps = 0;
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getLength(); y++) {
                String item = grid.getCell(x, y);
                if (item != null && (item.equals("O") || item.equals("["))) {
                    totalGps += x + y * 100;
                }
            }
        }
        
        System.out.println(String.format("Total GPS: %d", totalGps));
    }
    
    private static Coordinate move(Coordinate robot, Grid<String> grid, Direction direction) {
        int length = countShunt(robot, grid, direction);
        
        if (length == 0) {
            return robot;
        }
        
        int x = robot.getX();
        int y = robot.getY();
        int moveX = direction.getMoveX();
        int moveY = direction.getMoveY();
        
        for (int i = length - 1; i >= 0; i--) {
            grid.moveCell(x + moveX * i, y + moveY * i, x + moveX * (i + 1), y + moveY * (i + 1));
        }
        
        return new Coordinate(x + moveX, y + moveY);
    }
    
    private static int countShunt(Coordinate robot, Grid<String> grid, Direction direction) {
        int x = robot.getX();
        int y = robot.getY();
        
        int length = 1;
        boolean wallFound = false;
        while (!wallFound) {
            String nextItem = grid.getCell(x + length * direction.getMoveX(), y + length * direction.getMoveY());
            if (nextItem == null) {
                break;
            } else if (nextItem.equals("#")) {
                length = 0;
                wallFound = true;
            } else {
                length++;
            }
        }
        
        return length;
    }
    
    //////////////
    // PART TWO //
    //////////////
    
    private static void traverseGrid(Coordinate robot, Grid<Boolean> grid, List<Box> boxes, List<Direction> directions, boolean verbose) {
        for (Direction direction : directions) {
            move(robot, grid, boxes, direction);
            
            if (verbose) {
                printGrid(robot, grid, boxes);
            }
        }
        
        long totalGps = 0;
        for (Box box : boxes) {
            totalGps += box.getPosition().getX() + box.getPosition().getY() * 100;
        }
        
        System.out.println(String.format("Total GPS: %d", totalGps));
    }
    
    private static void move(Coordinate robot, Grid<Boolean> grid, List<Box> boxes, Direction direction) {
        Optional<List<Box>> shunt = identifyShunt(robot, grid, boxes, direction);
        
        if (shunt.isEmpty()) {
            return;
        }
        
        for (Box box : shunt.get()) {
            box.move(direction.getMoveX(), direction.getMoveY());
        }
        
        robot.move(direction.getMoveX(), direction.getMoveY());
    }
    
    private static Optional<List<Box>> identifyShunt(Coordinate robot, Grid<Boolean> grid, List<Box> boxes, Direction direction) {
        int x = robot.getX();
        int y = robot.getY();

        List<Box> shuntBoxes = new ArrayList<>();
        List<Coordinate> shuntFronts = new ArrayList<>();
        shuntFronts.add(new Coordinate(x + direction.getMoveX(), y + direction.getMoveY()));
        boolean wallFound = false;
        while (!wallFound) {
            do {
                if (shuntFronts.stream().anyMatch(front -> grid.getCell(front))) {
                    wallFound = true;
                    break;
                }
                
                Set<Box> hitBoxes = new HashSet<>();
                for (Coordinate front : shuntFronts) {
                    hitBoxes.addAll(boxes.stream().filter(box -> box.occupies(front.getX(), front.getY())).toList());
                }
                shuntBoxes.addAll(hitBoxes);

                shuntFronts.clear();
                for (Box hitBox : hitBoxes) {
                    for (int i = 0; i < (direction.findOrientation().equals(Orientation.HORIZONTAL) ? hitBox.getHeight() : hitBox.getWidth()); i++) {
                        if (direction == Direction.UP) {
                            shuntFronts.add(new Coordinate(hitBox.getPosition().getX() + direction.getMoveX() + i, hitBox.getPosition().getY() + direction.getMoveY()));
                        } else if (direction == Direction.DOWN) {
                            shuntFronts.add(new Coordinate(hitBox.getPosition().getX() + direction.getMoveX() + i, hitBox.getPosition().getY() + direction.getMoveY() + hitBox.getHeight() - 1));
                        } else if (direction == Direction.LEFT) {
                            shuntFronts.add(new Coordinate(hitBox.getPosition().getX() + direction.getMoveX(), hitBox.getPosition().getY() + direction.getMoveY() + i));
                        } else if (direction == Direction.RIGHT) {
                            shuntFronts.add(new Coordinate(hitBox.getPosition().getX() + direction.getMoveX() + hitBox.getWidth() - 1, hitBox.getPosition().getY() + direction.getMoveY() + i));
                        }
                    }
                }
            } while (!shuntFronts.isEmpty());
            
            if (shuntFronts.isEmpty()) {
                break;
            }
        }
        
        return wallFound ? Optional.empty() : Optional.of(shuntBoxes);
    }
    
    private static void printGrid(Coordinate robot, Grid<Boolean> grid, List<Box> boxes) {
        for (int y = 0; y < grid.getLength(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                if (grid.getCell(x, y)) {
                    System.out.print("#");
                    continue;
                } else if (robot.getX() == x && robot.getY() == y) {
                    System.out.print("@");
                    continue;
                }
                
                int xx = x;
                int yy = y;
                
                Optional<Box> anyBox = boxes.stream().filter(eachBox -> eachBox.occupies(xx, yy)).findFirst();
                if (anyBox.isEmpty()) {
                    System.out.print(".");
                    continue;
                }
                
                Box box = anyBox.get();
                if (x == box.getPosition().getX()) {
                    System.out.print("[");
                } else if (x == box.getPosition().getX() + box.getWidth() - 1) {
                    System.out.print("]");
                } else {
                    System.out.print("=");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    private static Pair<Grid<Boolean>, List<Box>> transformInput(Grid<String> grid) {
        Grid<Boolean> wallGrid = new Grid<>(grid.getWidth() * 2, grid.getLength());
        List<Box> boxes = new ArrayList<>();
        
        for (int y = 0; y < grid.getLength(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                String item = grid.getCell(x, y);
                if (item == null) {
                    wallGrid.addCell(x * 2, y, false);
                    wallGrid.addCell(x * 2 + 1, y, false);
                } else if (!item.equals("#")) {
                    wallGrid.addCell(x * 2, y, false);
                    wallGrid.addCell(x * 2 + 1, y, false);
                    
                    if (item.equals("O")) {
                        boxes.add(new Box(new Coordinate(x * 2, y), 2, 1));
                    }
                } else {
                    wallGrid.addCell(x * 2, y, true);
                    wallGrid.addCell(x * 2 + 1, y, true);
                }
            }
        }
        
        return new Pair<>(wallGrid, boxes);
    }
    
    private static Input readInput(String filePath) {
        FileLineReader reader = new FileLineReader();
        List<String> lines = reader.readLines(DayFifteen.class.getResource(filePath));
        
        Coordinate robot = null;
        Grid<String> grid = new Grid<>();
        List<Direction> directions = new ArrayList<>();
        boolean gridComplete = false;
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            if (line.isEmpty()) {
                gridComplete = true;
                continue;
            }
            
            for (int x = 0; x < line.length(); x++) {
                String item = line.substring(x, x + 1);
                if (!gridComplete) {
                    if (item.equals("@")) {
                        robot = new Coordinate(x, y);
                    }
                    if (!item.equals(".")) {
                        grid.addCell(x, y, item);
                    }
                } else {
                    if (item.equals("v")) {
                        directions.add(Direction.DOWN);
                    } else if (item.equals("<")) {
                        directions.add(Direction.LEFT);
                    } else if (item.equals(">")) {
                        directions.add(Direction.RIGHT);
                    } else if (item.equals("^")) {
                        directions.add(Direction.UP);
                    } else {
                        throw new IllegalArgumentException("Cannot identify direction for: " + item);
                    }
                }
            }
        }
        
        if (robot == null) {
            throw new IllegalArgumentException("Robot not found");
        }
        
        return new Input(robot, grid, directions);
    }

}
