package adventofcode.year2024.day16;

import java.util.Objects;

import adventofcode.utilities.Coordinate;
import adventofcode.utilities.Direction;

class Location {

    private final Coordinate position;
    private final Direction approachDirection;

    public Location(Coordinate position, Direction approachDirection) {
        this.position = position;
        this.approachDirection = approachDirection;
    }

    public Coordinate getPosition() {
        return position;
    }

    public Direction getApproachDirection() {
        return approachDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(approachDirection, position);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }
        Location other = (Location) obj;
        return Objects.equals(approachDirection, other.approachDirection) && Objects.equals(position, other.position);
    }
    
    @Override
    public String toString() {
        return String.format("%s -> %s", approachDirection, position);
    }

}
