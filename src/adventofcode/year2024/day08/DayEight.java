package adventofcode.year2024.day08;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import adventofcode.utilities.Coordinate;
import adventofcode.utilities.FileLineReader;
import adventofcode.utilities.Grid;

public class DayEight {

    public static void main(String[] args) {
//        Grid<Location> grid = readGrid("test_input.txt");
        Grid<Location> grid = readGrid("input.txt");

        long part1Start = System.currentTimeMillis();
        
        Map<String, List<Coordinate>> antennaGroupings = findAntinodes(grid);

        long part2Start = System.currentTimeMillis();
        
        findAntinodesIncludingResonances(grid, antennaGroupings);

        long end = System.currentTimeMillis();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %dms", part2Start - part1Start));
        System.out.println(String.format("Part 2 duration: %dms", end - part2Start));
    }

    private static Map<String, List<Coordinate>> findAntinodes(Grid<Location> grid) {
        Map<String, List<Coordinate>> antennaGroupings =  identifyAnternnas(grid);
        
        for (Entry<String, List<Coordinate>> grouping : antennaGroupings.entrySet()) {
            String antenna = grouping.getKey();
            List<Coordinate> coordinates = grouping.getValue();

            for (Coordinate first : coordinates) {
                for (Coordinate second : coordinates) {
                    if (first == second) {
                        continue;
                    }

                    grid.getCell(first.getX(), first.getY()).addAntinode(antenna);
                    grid.getCell(second.getX(), second.getY()).addAntinode(antenna);
                    
                    Coordinate delta = new Coordinate(second.getX() - first.getX(), second.getY() - first.getY());

                    Coordinate firstAntinode = new Coordinate(first.getX() - delta.getX(), first.getY() - delta.getY());
                    Coordinate secondAntinode = new Coordinate(second.getX() + delta.getX(), second.getY() + delta.getY());

                    if (firstAntinode.getX() >= 0 && firstAntinode.getX() < grid.getWidth()
                            && firstAntinode.getY() >= 0 && firstAntinode.getY() < grid.getLength()) {
                        grid.getCell(firstAntinode.getX(), firstAntinode.getY()).addAntinode(antenna);
                    }
                    if (secondAntinode.getX() >= 0 && secondAntinode.getX() < grid.getWidth()
                            && secondAntinode.getY() >= 0 && secondAntinode.getY() < grid.getLength()) {
                        grid.getCell(secondAntinode.getX(), secondAntinode.getY()).addAntinode(antenna);
                    }
                }
            }
        }
        
        int totalAntinodes = 0;
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getLength(); y++) {
                if (!grid.getCell(x, y).getAntinodes().isEmpty()) {
                    totalAntinodes++;
                }
            }
        }
        
        System.out.println(String.format("Total unique antinodes: %d", totalAntinodes));
        
        return antennaGroupings;
    }

    private static void findAntinodesIncludingResonances(Grid<Location> grid, Map<String, List<Coordinate>> antennaGroupings) {
        for (Entry<String, List<Coordinate>> grouping : antennaGroupings.entrySet()) {
            String antenna = grouping.getKey();
            List<Coordinate> coordinates = grouping.getValue();

            for (Coordinate first : coordinates) {
                for (Coordinate second : coordinates) {
                    if (first == second) {
                        continue;
                    }
                    
                    Coordinate delta = new Coordinate(second.getX() - first.getX(), second.getY() - first.getY());

                    Coordinate firstAntinode = new Coordinate(first.getX() - delta.getX(), first.getY() - delta.getY());
                    Coordinate secondAntinode = new Coordinate(second.getX() + delta.getX(), second.getY() + delta.getY());

                    while (firstAntinode.getX() >= 0 && firstAntinode.getX() < grid.getWidth()
                            && firstAntinode.getY() >= 0 && firstAntinode.getY() < grid.getLength()) {
                        grid.getCell(firstAntinode.getX(), firstAntinode.getY()).addAntinode(antenna);
                        
                        firstAntinode = new Coordinate(firstAntinode.getX() - delta.getX(), firstAntinode.getY() - delta.getY());
                    }
                    while (secondAntinode.getX() >= 0 && secondAntinode.getX() < grid.getWidth()
                            && secondAntinode.getY() >= 0 && secondAntinode.getY() < grid.getLength()) {
                        secondAntinode = new Coordinate(secondAntinode.getX() + delta.getX(), secondAntinode.getY() + delta.getY());
                    }
                }
            }
        }
        
        int totalAntinodes = 0;
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getLength(); y++) {
                if (!grid.getCell(x, y).getAntinodes().isEmpty()) {
                    totalAntinodes++;
                }
            }
        }
        
        System.out.println(String.format("Total unique antinodes: %d", totalAntinodes));
    }

    private static Map<String, List<Coordinate>> identifyAnternnas(Grid<Location> grid) {
        Map<String, List<Coordinate>> antennaGroupings = new LinkedHashMap<>();

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getLength(); y++) {
                Location here = grid.getCell(x, y);
                if (!here.hasAntenna()) {
                    continue;
                }

                antennaGroupings.computeIfAbsent(here.getAntenna(), id -> new ArrayList<>()).add(new Coordinate(x, y));
            }
        }

        return antennaGroupings;
    }

    private static Grid<Location> readGrid(String filePath) {
        Grid<Location> grid = new Grid<>();

        FileLineReader reader = new FileLineReader();
        List<String> lines = reader.readLines(DayEight.class.getResource(filePath));
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                String antenna = line.substring(x, x + 1);

                if (antenna.equals(".")) {
                    grid.addCell(x, y, new Location());
                } else {
                    grid.addCell(x, y, new Location(antenna));
                }
            }
        }

        return grid;
    }

}
