package adventofcode.year2024.day06;

import java.util.ArrayList;
import java.util.List;

class Location {
    
    private boolean isBlocked;
    private final List<Direction> traversals;
    
    public Location(Location toCopy) {
        this(toCopy.isBlocked);
        traversals.addAll(toCopy.getTraversals());
    }
    
    public Location(boolean isBlocked) {
        this.isBlocked = isBlocked;
        traversals = new ArrayList<>();
    }
    
    public void setBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public void addTraversal(Direction traversal) {
        if (isBlocked) {
            throw new IllegalStateException("Cannot add traversal to blocked location");
        }
        traversals.add(traversal);
    }
    
    public boolean isBlocked() {
        return isBlocked;
    }
    
    public List<Direction> getTraversals() {
        return traversals;
    }
    
}