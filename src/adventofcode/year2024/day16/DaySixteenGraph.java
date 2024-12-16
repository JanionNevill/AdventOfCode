package adventofcode.year2024.day16;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.ToDoubleBiFunction;

import adventofcode.utilities.Coordinate;
import adventofcode.utilities.Direction;
import adventofcode.utilities.FileLineReader;
import adventofcode.utilities.Grid;
import adventofcode.utilities.graph.AStarPathfinder;
import adventofcode.utilities.graph.Edge;
import adventofcode.utilities.graph.Graph;
import adventofcode.utilities.graph.Node;

public class DaySixteenGraph {
    
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
//        Input input = readInput("tiny_input.txt");
//        Input input = readInput("test_input.txt");
//        Input input = readInput("test_input2.txt");
        Input input = readInput("input.txt");

        Instant partOneStart = Instant.now();
        
        Graph<Location> graph = constructGraph(input.getWallGrid(), input.getStart(), input.getEnd());
        System.out.println();
        
        findLowestScoringPath(graph, input.getStart(), input.getEnd());

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
    
    private static void findLowestScoringPath(Graph<Location> graph, Coordinate startPosition, Coordinate endPosition) {
        AStarPathfinder pathfinder = new AStarPathfinder();
        ToDoubleBiFunction<Node<Location>, Node<Location>> heuristic = (a, b) -> {
            Coordinate posA = a.getItem().getPosition();
            Coordinate posB = b.getItem().getPosition();
            double deltaX = Math.abs(posB.getX() - posA.getX());
            double deltaY = Math.abs(posB.getY() - posA.getY());
            return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        };
        Optional<List<Node<Location>>> path = pathfinder.<Location>findShortestPath(graph,
                graph.getNode(new Location(startPosition, Direction.RIGHT)).orElse(null),
                graph.getNode(new Location(endPosition, null)).orElse(null),
                heuristic);
        
        if (path.isPresent()) {
            long score = 0;
            Node<Location> lastNode = null;
            for (Node<Location> node : path.get()) {
                if (lastNode != null) {
                    Optional<Edge<Location>> traversedEdge = lastNode.getEdges().stream().filter(edge -> edge.getEnd().equals(node)).findFirst();
                    if (traversedEdge.isPresent()) {
                        score += traversedEdge.get().getValue();
                    }
                }
                lastNode = node;
            }
            System.out.println(String.format("Best path score: %d", score));
        } else {
            System.out.println("A* failed");
        }
    }
    
    private static Graph<Location> constructGraph(Grid<Boolean> wallGrid, Coordinate startPosition, Coordinate endPosition) {
        Graph<Location> graph = new Graph<>();
        Grid<Map<Direction, Location>> locationGrid = new Grid<>(wallGrid.getWidth(), wallGrid.getLength());
        
        Location startLocation = new Location(startPosition, Direction.RIGHT);
        graph.addNode(startLocation);
        Map<Direction, Location> startLocationMap = new LinkedHashMap<>();
        startLocationMap.put(Direction.RIGHT, startLocation);
        locationGrid.addCell(startPosition, startLocationMap);
        
        ///////////////////
        // CREATE NODES //
        //////////////////
        
        for (int x = 0; x < wallGrid.getWidth(); x++) {
            for (int y = 0; y < wallGrid.getLength(); y++) {
                if (wallGrid.getCell(x, y)) {
                    continue;
                }
                
                for (Direction direction : Direction.ORTHOGONAL_DIRECTIONS) {
                    int toX = x + direction.getMoveX();
                    int toY = y + direction.getMoveY();

                    // Don't walk off the map or into a wall
                    if (toX < 0 || toX >= wallGrid.getWidth()
                            || toY < 0 || toY >= wallGrid.getLength()
                            || wallGrid.getCell(toX, toY)) {
                        continue;
                    }
                    Coordinate to = new Coordinate(toX, toY);
                    
                    Location toLocation;
                    Map<Direction, Location> toLocations = locationGrid.getCell(to);
                    if (toLocations == null) {
                        toLocations = new LinkedHashMap<>();
                        locationGrid.addCell(to, toLocations);
                    }
                    toLocation = toLocations.computeIfAbsent(direction, dir -> new Location(to, dir));
                    graph.addNode(toLocation);
                }
            }
        }
        
        //////////////////
        // CREATE EDGES //
        /////////////////
        
        for (int x = 0; x < locationGrid.getWidth(); x++) {
            for (int y = 0; y < locationGrid.getLength(); y++) {
                if (locationGrid.getCell(x, y) == null) {
                    continue;
                }
                
                for (Location fromLocation : locationGrid.getCell(x, y).values()) {
                    for (Direction direction : Direction.ORTHOGONAL_DIRECTIONS) {
                        int toX = x + direction.getMoveX();
                        int toY = y + direction.getMoveY();
    
                        // Don't walk off the map or into a wall
                        if (toX < 0 || toX >= locationGrid.getWidth()
                                || toY < 0 || toY >= locationGrid.getLength()
                                || locationGrid.getCell(toX, toY) == null) {
                            continue;
                        }
                        Coordinate to = new Coordinate(toX, toY);
                        
                        Map<Direction, Location> toLocations = locationGrid.getCell(to);
                        
                        Location toLocation = toLocations.get(direction);
                        if (toLocation == null) {
                            continue;
                        }
                        
                        int score = 1;
                        if (!fromLocation.getApproachDirection().equals(direction)) {
                            score += 1000;
                            if (fromLocation.getApproachDirection().equals(direction.turnBackwards())) {
                                score += 1000;
                            }
                        }
                        graph.addEdge(fromLocation, toLocation, score);
                    }
                }
            }
        }
        
        Node<Location> targetNode = graph.addNode(new Location(endPosition, null));
        for (Location endLoc : locationGrid.getCell(endPosition).values()) {
            Optional<Node<Location>> endNode = graph.getNode(endLoc);
            
            if (endNode.isEmpty()) {
                throw new IllegalArgumentException("Cannot find location for end position in graph");
            }
            
            graph.addEdge(endNode.get(), targetNode, 0);
        }
        
        return graph;
    }
    
    private static Input readInput(String filePath) {
        FileLineReader reader = new FileLineReader();
        List<String> lines = reader.readLines(DaySixteenGraph.class.getResource(filePath));
        
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
