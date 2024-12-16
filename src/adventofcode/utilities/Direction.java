package adventofcode.utilities;

import java.util.Arrays;
import java.util.List;

public class Direction {

    public static final Direction UP = new Direction("UP", 0, -1);
    public static final Direction RIGHT = new Direction("RIGHT", 1, 0);
    public static final Direction DOWN = new Direction("DOWN", 0, 1);
    public static final Direction LEFT = new Direction("LEFT", -1, 0);
    
    public static final List<Direction> ORTHOGONAL_DIRECTIONS = Arrays.asList(UP, RIGHT, DOWN, LEFT);
    
    static {
        UP.setClockwiseDirection(RIGHT);
        UP.setAnticlockwiseDirection(LEFT);
        UP.setOppositeDirection(DOWN);
        
        RIGHT.setClockwiseDirection(DOWN);
        RIGHT.setAnticlockwiseDirection(UP);
        RIGHT.setOppositeDirection(LEFT);
        
        DOWN.setClockwiseDirection(LEFT);
        DOWN.setAnticlockwiseDirection(RIGHT);
        DOWN.setOppositeDirection(UP);
        
        LEFT.setClockwiseDirection(UP);
        LEFT.setAnticlockwiseDirection(DOWN);
        LEFT.setOppositeDirection(RIGHT);
    }

    private String name;
    private int moveX;
    private int moveY;

    private Direction clockwiseDirection;
    private Direction anticlockwiseDirection;
    private Direction oppositeDirection;

    private Direction(String name, int moveX, int moveY) {
        this.name = name;
        this.moveX = moveX;
        this.moveY = moveY;
    }

    public Direction turnClockwise() {
        return clockwiseDirection;
    }

    public Direction turnAnticlockwise() {
        return anticlockwiseDirection;
    }

    public Direction turnBackwards() {
        return oppositeDirection;
    }

    private void setClockwiseDirection(Direction clockwiseDirection) {
        this.clockwiseDirection = clockwiseDirection;
    }

    private void setAnticlockwiseDirection(Direction anticlockwiseDirection) {
        this.anticlockwiseDirection = anticlockwiseDirection;
    }

    private void setOppositeDirection(Direction oppositeDirection) {
        this.oppositeDirection = oppositeDirection;
    }
    
    public Orientation findOrientation() {
        if (moveX != 0 && moveY == 0) {
            return Orientation.HORIZONTAL;
        } else if (moveY != 0 && moveX == 0) {
            return Orientation.VERTICAL;
        }
        
        return Orientation.OTHER;
    }
    
    public String getName() {
        return name;
    }

    public int getMoveX() {
        return moveX;
    }

    public int getMoveY() {
        return moveY;
    }
    
    @Override
    public String toString() {
        return name;
    }

}