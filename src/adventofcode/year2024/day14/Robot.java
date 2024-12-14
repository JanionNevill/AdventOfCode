package adventofcode.year2024.day14;

import adventofcode.utilities.Coordinate;

class Robot {

    private Coordinate position;
    private Coordinate velocity;
    
    public Robot(Coordinate position, Coordinate velocity) {
        this.position = position;
        this.velocity = velocity;
    }
    
    public Coordinate findPositionAfter(int steps, int gridWidth, int gridLength) {
        int x = Math.floorMod(position.getX() + velocity.getX() * steps, gridWidth);
        int y = Math.floorMod(position.getY() + velocity.getY() * steps, gridLength);
        return new Coordinate(x, y);
    }

}
