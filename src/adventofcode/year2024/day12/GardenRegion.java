package adventofcode.year2024.day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adventofcode.utilities.Coordinate;
import adventofcode.utilities.Direction;

class GardenRegion {
    
    private final String plant;
    private final List<Coordinate> plots = new ArrayList<>();
    
    public GardenRegion(String plant) {
        this.plant = plant;
    }

    public void addPlot(Coordinate plot) {
        plots.add(plot);
    }
    
    public String getPlant() {
        return plant;
    }
    
    public List<Coordinate> getPlots() {
        return plots;
    }
    
    public boolean containsPlot(Coordinate plot) {
        return plots.contains(plot);
    }
    
    public int getArea() {
        return plots.size();
    }
    
    public long calculatePerimeter() {
        long perimeter = 0;
        for (Coordinate plot : plots) {
            for (Direction direction : Direction.ORTHOGONAL_DIRECTIONS) {
                if (!plots.contains(new Coordinate(plot.getX() + direction.getMoveX(), plot.getY() + direction.getMoveY()))) {
                    perimeter++;
                }
            }
        }
        return perimeter;
    }
    
    private enum Orientation {
        
        VERTICAL("VERTICAL", Arrays.asList(Direction.UP, Direction.DOWN), Arrays.asList(Direction.LEFT, Direction.RIGHT)),
        HORIZONTAL("HORIZONTAL", Arrays.asList(Direction.LEFT, Direction.RIGHT), Arrays.asList(Direction.UP, Direction.DOWN));

        private final String name;
        private final List<Direction> moves;
        private final List<Direction> boundaries;
        
        private Orientation(String name, List<Direction> moves, List<Direction> boundaries) {
            this.name = name;
            this.moves = moves;
            this.boundaries = boundaries;
        }
        
        public List<Direction> getMoves() {
            return moves;
        }
        
        public List<Direction> getBoundaries() {
            return boundaries;
        }
        
        @Override
        public String toString() {
            return name;
        }
        
    }
    
    public long calculateSides() {
        long sides = 0;
        for (Orientation orientation : Orientation.values()) {
            for (Direction boundary : orientation.getBoundaries()) {
                List<Coordinate> plotsInspected = new ArrayList<>();
                for (Coordinate plot : plots) {
                    if (plotsInspected.contains(plot)) {
                        continue;
                    }
                    
                    if (!plots.contains(new Coordinate(plot.getX() + boundary.getMoveX(), plot.getY() + boundary.getMoveY()))) {
                        List<Coordinate> sidePlots = findSidePlots(plot, orientation, boundary);
                        sides++;
                        plotsInspected.addAll(sidePlots);
                    } else {
                        plotsInspected.add(plot);
                    }
                }
            }
        }
        return sides;
    }
    
    private List<Coordinate> findSidePlots(Coordinate startPlot, Orientation orientation, Direction boundary) {
        List<Coordinate> sidePlots = new ArrayList<>();
        sidePlots.add(startPlot);
        
        for (Direction move : orientation.getMoves()) {
            int moveCount = 1;
            while(true) {
                Coordinate nextPlot = new Coordinate(startPlot.getX() + moveCount * move.getMoveX(), startPlot.getY() + moveCount * move.getMoveY());
                if (plots.contains(nextPlot)
                        && !plots.contains(new Coordinate(nextPlot.getX() + boundary.getMoveX(), nextPlot.getY() + boundary.getMoveY()))) {
                    sidePlots.add(nextPlot);
                    moveCount++;
                } else {
                    break;
                }
            }
        }
        
        return sidePlots;
    }
    
//    public long calculateSides() {
//        long perimeter = 0;
//        for (Orientation orientation : Orientation.values()) {
//            for (Direction boundary : orientation.getBoundaries()) {
//                List<Coordinate> plotsInspected = new ArrayList<>();
//                for (Coordinate plot : plots) {
//                    for (Direction move : orientation.getMoves()) {
//                        if (!plots.contains(new Coordinate(plot.getX() + boundary.getMoveX(), plot.getY() + boundary.getMoveY()))) {
//                            perimeter++;
//                        }
//                    }
//                }
//            }
//        }
//        return perimeter;
//    }

}
