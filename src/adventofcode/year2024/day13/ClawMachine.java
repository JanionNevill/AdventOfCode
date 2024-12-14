package adventofcode.year2024.day13;

import adventofcode.utilities.LongCoordinate;

class ClawMachine {
    
    private LongCoordinate prize;
    private LongCoordinate incrementA;
    private LongCoordinate incrementB;
    
    public ClawMachine(LongCoordinate prize, LongCoordinate incrementA, LongCoordinate incrementB) {
        this.prize = prize;
        this.incrementA = incrementA;
        this.incrementB = incrementB;
    }
    
    public void movePrize(long xMove, long yMove) {
        prize = new LongCoordinate(prize.getX() + xMove, prize.getY() + yMove);
    }
    
    public long calculateCostToWin(int aCost, int bCost, boolean verbose) {
        double ratioA = (double) incrementA.getY() / incrementA.getX();
        double ratioB = (double) incrementB.getY() / incrementB.getX();
        
        if (Math.abs(ratioA - ratioB) < 0.001) {
            throw new IllegalStateException("A and B vectors are parallel");
        }
        
        double aCount = (double) ((prize.getX() * incrementB.getY()) - (prize.getY() * incrementB.getX()))
                / ((incrementB.getY() * incrementA.getX()) - (incrementB.getX() * incrementA.getY()));
        
        if (aCount % 1 > 0.001) {
            if (verbose) {
                System.out.println("Unwinnable");
            }
            return 0;
//            throw new IllegalStateException("Cannot calculate integer A button presses");
        }
        
        long aPresses = Math.round(aCount);
        double bCount = (double) (prize.getX() - aCount * incrementA.getX()) / incrementB.getX();
        
        if (bCount % 1 > 0.001) {
            if (verbose) {
                System.out.println("Unwinnable");
            }
            return 0;
//            throw new IllegalStateException("Cannot calculate integer B button presses");
        }
        
        long bPresses = Math.round(bCount);
        long cost = aCost * aPresses + bCost * bPresses;

        if (verbose) {
            System.out.println(String.format("Button A: %3d Button B: %3d -> Cost: %d", aPresses, bPresses, cost));
        }
        
        return cost;
    }

}
