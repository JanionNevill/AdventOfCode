package adventofcode.year2024.day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import adventofcode.utilities.FileLineReader;

public class DayEleven {

    public static void main(String[] args) {
        List<Long> stones = Arrays.asList(2L);
        blinkAtStones(stones, 10, true);
        
//        List<Long> stones = readInput("test_input.txt");
////        List<Long> stones = readInput("test_input2.txt");
////        List<Long> stones = readInput("input.txt");
//
//        long part1Start = System.currentTimeMillis();
//
//        blinkAtStones(stones, 6, true);
////        blinkAtStones(stones, 1, true);
////        blinkAtStones(stones, 25, false);
//
//        long part2Start = System.currentTimeMillis();
//
//        blinkAtStones(stones, 75, false);
//
//        long end = System.currentTimeMillis();
//        System.out.println();
//        System.out.println(String.format("Part 1 duration: %dms", part2Start - part1Start));
//        System.out.println(String.format("Part 2 duration: %dms", end - part2Start));
    }
    
    private static void blinkAtStones(List<Long> stones, int blinkCount, boolean printSequence) {
        if (printSequence) {
            System.out.println(stones.stream().map(String::valueOf).collect(Collectors.joining(" ")));
        }
        
        for (int i = 0; i < blinkCount; i++) {
            stones = blink(stones);
            if (printSequence) {
                System.out.println(stones.stream().map(String::valueOf).collect(Collectors.joining(" ")));
            }
        }
        
        System.out.println(String.format("Stone count: %d", stones.size()));
    }
    
    private static List<Long> blink(List<Long> stones) {
        List<Long> newStones = new ArrayList<>();
        for (long stone : stones) {
            int digits;
            if (stone == 0) {
                newStones.add(1L);
            } else if ((digits = countDigits(stone)) % 2 == 0) {
                long tens = (long) Math.pow(10, (digits / 2));
                long mod = stone % tens;
                newStones.add((long) (stone / tens));
                newStones.add(mod);
            } else {
                newStones.add(stone * 2024);
            }
        }
        
        return newStones;
    }
    
    private static int countDigits(long number) {
        return (int) Math.log10(number) + 1;
    }
    
    private static List<Long> readInput(String filePath) {
        FileLineReader reader = new FileLineReader();
        String line = reader.readLines(DayEleven.class.getResource(filePath)).get(0);
        
        List<Long> stones = new ArrayList<>();
        for (String str : line.split(" ")) {
            stones.add(Long.parseLong(str));
        }
        
        return stones;
    }

}
