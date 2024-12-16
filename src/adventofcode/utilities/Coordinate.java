package adventofcode.utilities;

import java.util.Objects;

public class Coordinate {

    private int x;
    private int y;

    public Coordinate(Coordinate toCopy) {
        this(toCopy.x, toCopy.y);
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void move(int moveX, int moveY) {
        x += moveX;
        y += moveY;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
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
        Coordinate other = (Coordinate) obj;
        return x == other.x && y == other.y;
    }
    
    @Override
    public String toString() {
        return String.format("[%d, %d]", x, y);
    }
}