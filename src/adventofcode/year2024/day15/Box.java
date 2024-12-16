package adventofcode.year2024.day15;

import adventofcode.utilities.Coordinate;

class Box {

    private Coordinate position;
    private int width;
    private int height;

    public Box(Coordinate position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public void move(int moveX, int moveY) {
        position.move(moveX, moveY);
    }
    
    public boolean occupies(int x, int y) {
        int deltaX = x - position.getX();
        int deltaY = y - position.getY();
        return deltaX >= 0 && deltaX < width && deltaY >= 0 && deltaY < height;
    }

    public Coordinate getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
