package adventofcode.year2024.day12;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import adventofcode.utilities.Coordinate;
import adventofcode.utilities.Direction;
import adventofcode.utilities.FileLineReader;
import adventofcode.utilities.Grid;
import adventofcode.utilities.benchmark.Benchmarker;

public class DayTwelve {

    public static void main(String[] args) {
//        Grid<String> garden = readInput("test_input.txt");
//        Grid<String> garden = readInput("test_input2.txt");
//        Grid<String> garden = readInput("test_input3.txt");
//        Grid<String> garden = readInput("test_input4.txt");
        Grid<String> garden = readInput("input.txt");

        Instant partOneStart = Instant.now();

        List<GardenRegion> regions = calculateFenceCostByPerimeter(garden);

        Instant partTwoStart = Instant.now();

        calculateFenceCostBySides(regions);

        Instant end = Instant.now();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %d ms", partOneStart.until(partTwoStart, ChronoUnit.MILLIS)));
        System.out.println(String.format("Part 2 duration: %d ms", partTwoStart.until(end, ChronoUnit.MILLIS)));

        System.out.println();

        Benchmarker<Grid<String>, List<GardenRegion>> benchmarker = new Benchmarker<>(
                input -> DayTwelve.calculateFenceCostByPerimeter(input),
                input -> DayTwelve.calculateFenceCostBySides(input));

        benchmarker.runBenchmark(garden, regions, 10, 100);
    }
    
    private static List<GardenRegion> calculateFenceCostByPerimeter(Grid<String> garden) {
        List<Coordinate> assignedPlots = new ArrayList<>();
        List<GardenRegion> regions = new ArrayList<>();

        for (int x = 0; x < garden.getWidth(); x++) {
            for (int y = 0; y < garden.getLength(); y++) {
                Coordinate plot = new Coordinate(x, y);
                if (assignedPlots.contains(plot)) {
                    continue;
                }
                
                GardenRegion region = findTotalRegion(plot, garden);
                regions.add(region);
                
                assignedPlots.addAll(region.getPlots());
            }
        }
        
        long totalCost = 0;
        for (GardenRegion region : regions) {
            totalCost += region.getArea() * region.calculatePerimeter();
        }
        
        System.out.println(String.format("Total fencing cost: £%d", totalCost));
        
        return regions;
    }
    
    private static List<GardenRegion> calculateFenceCostBySides(List<GardenRegion> regions) {
        long totalCost = 0;
        for (GardenRegion region : regions) {
            totalCost += region.getArea() * region.calculateSides();
        }
        
        System.out.println(String.format("Total fencing cost: £%d", totalCost));
        
        return regions;
    }
    
    private static GardenRegion findTotalRegion(Coordinate startingPlot, Grid<String> garden) {
        GardenRegion region = new GardenRegion(garden.getCell(startingPlot.getX(), startingPlot.getY()));
        traverseGarden(startingPlot, region, garden);
        
        return region;
    }
    
    private static void traverseGarden(Coordinate plot, GardenRegion region, Grid<String> garden) {
        region.addPlot(plot);
        for (Direction direction : Direction.ORTHOGONAL_DIRECTIONS) {
            Coordinate neighbourPlot = new Coordinate(plot.getX() + direction.getMoveX(), plot.getY() + direction.getMoveY());
            
            if (region.containsPlot(neighbourPlot)
                    || neighbourPlot.getX() < 0 || neighbourPlot.getX() >= garden.getWidth()
                    || neighbourPlot.getY() < 0 || neighbourPlot.getY() >= garden.getLength()) {
                continue;
            }
            
            String neighbourPlant = garden.getCell(neighbourPlot.getX(), neighbourPlot.getY());
            if (neighbourPlant.equals(region.getPlant())) {
                traverseGarden(neighbourPlot, region, garden);
            }
        }
    }
    
    private static Grid<String> readInput(String filePath) {
        FileLineReader reader = new FileLineReader();
        List<String> lines = reader.readLines(DayTwelve.class.getResource(filePath));
        
        Grid<String> garden = new Grid<>();
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                garden.addCell(x, y, line.substring(x, x + 1));
            }
        }
        
        return garden;
    }

}
