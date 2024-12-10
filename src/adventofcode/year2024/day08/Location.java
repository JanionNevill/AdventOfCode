package adventofcode.year2024.day08;

import java.util.LinkedHashSet;
import java.util.Set;

class Location {
    
    private final String antenna;
    private final Set<String> antinodes;
    
    public Location() {
        this(null);
    }
    
    public Location(String antenna) {
        this.antenna = antenna;
        antinodes = new LinkedHashSet<>();
    }
    
    public void addAntinode(String antenna) {
        antinodes.add(antenna);
    }
    
    public boolean hasAntenna() {
        return antenna != null;
    }
    
    public String getAntenna() {
        return antenna;
    }
    
    public Set<String> getAntinodes() {
        return antinodes;
    }

}
