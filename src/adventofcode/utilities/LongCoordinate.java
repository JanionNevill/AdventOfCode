package adventofcode.utilities;

import java.util.Objects;

public class LongCoordinate {

    private long x;
    private long y;

    public LongCoordinate(LongCoordinate toCopy) {
        this(toCopy.x, toCopy.y);
    }

    public LongCoordinate(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
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
        LongCoordinate other = (LongCoordinate) obj;
        return x == other.x && y == other.y;
    }
    
    @Override
    public String toString() {
        return String.format("[%d, %d]", x, y);
    }
}