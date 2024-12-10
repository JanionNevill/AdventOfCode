package adventofcode.year2024.day06;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Map {
    
    private List<List<Location>> locations;
    
    public Map(Map toCopy) {
        this();
        for (int x = 0; x < toCopy.getWidth(); x++) {
            for (int y = 0; y < toCopy.getWidth(); y++) {
                addLocation(x, y, new Location(toCopy.getLocation(x, y)));
            }
        }
    }
    
    public Map() {
        locations = new ArrayList<>();
    }
    
    public void addLocation(int x, int y, Location location) {
        while (y >= locations.size()) {
            locations.add(new ArrayList<>());
        }
        
        List<Location> row = locations.get(y);
        
        while (x >= row.size()) {
            row.add(null);
        }
        
        if (x < row.size()) {
            row.remove(x);
        }
        
        row.add(x, location);
    }
    
    public Location getLocation(int x, int y) {
        return locations.get(y).get(x);
    }
    
    public int getWidth() {
        if (locations.isEmpty()) {
            return 0;
        }
        return locations.get(0).size();
    }
    
    public int getLength() {
        return locations.size();
    }
    
    @Override
    public String toString() {
        String str = "";
        for (List<Location> row : locations) {
            String rowStr = row.stream().map(this::calculateCharacter).collect(Collectors.joining());
            str += rowStr;
            str += "\n";
        }
        return str;
    }
    
    private String calculateCharacter(Location location) {
        if (location.isBlocked()) {
            return "#";
        } else if (location.getTraversals().isEmpty()) {
            return ".";
        }
        
        List<Direction> traversals = location.getTraversals();
        boolean leftRight = traversals.contains(Direction.LEFT) || traversals.contains(Direction.RIGHT);
        boolean upDown = traversals.contains(Direction.UP) || traversals.contains(Direction.DOWN);
        
        if (leftRight && !upDown) {
            return "-";
        } else if (!leftRight && upDown) {
            return "|";
        }
        
        return "+";
    }

}
