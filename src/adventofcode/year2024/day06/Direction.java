package adventofcode.year2024.day06;

class Direction {

    public static final Direction UP = new Direction(0, -1);
    public static final Direction RIGHT = new Direction(1, 0);
    public static final Direction DOWN = new Direction(0, 1);
    public static final Direction LEFT = new Direction(-1, 0);
    
    static {
        UP.setNextDirection(RIGHT);
        RIGHT.setNextDirection(DOWN);
        DOWN.setNextDirection(LEFT);
        LEFT.setNextDirection(UP);
//        UP.setNextDirection(LEFT);
//        LEFT.setNextDirection(DOWN);
//        DOWN.setNextDirection(RIGHT);
//        RIGHT.setNextDirection(UP);
    }

    private int moveX;
    private int moveY;

    private Direction nextDirection;

    public Direction(int moveX, int moveY) {
        this.moveX = moveX;
        this.moveY = moveY;
    }

    public Direction getNextDirection() {
        return nextDirection;
    }

    public void setNextDirection(Direction nextDirection) {
        this.nextDirection = nextDirection;
    }

    public int getMoveX() {
        return moveX;
    }

    public int getMoveY() {
        return moveY;
    }

}