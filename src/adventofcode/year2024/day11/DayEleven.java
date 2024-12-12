package adventofcode.year2024.day11;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import adventofcode.utilities.FileLineReader;
import adventofcode.utilities.Pair;
import adventofcode.utilities.benchmark.Benchmarker;

public class DayEleven {

    public static void main(String[] args) {
//        List<Long> stones = readInput("test_input.txt");
//        List<Long> stones = readInput("test_input2.txt");
        List<Long> stones = readInput("input.txt");

        Instant partOneStart = Instant.now();

//        blinkAtStones(stones, 6, true);
//        blinkAtStones(stones, 1, true);
        blinkAtStones(stones, 25, false);

        Instant partTwoStart = Instant.now();

//        blinkAtStones(stones, 6);
//        blinkAtStones(stones, 1);
        blinkAtStones(stones, 75);

        Instant end = Instant.now();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %d ms", partOneStart.until(partTwoStart, ChronoUnit.MILLIS)));
        System.out.println(String.format("Part 2 duration: %d ms", partTwoStart.until(end, ChronoUnit.MILLIS)));

        System.out.println();

        Benchmarker<List<Long>, List<Long>> benchmarker = new Benchmarker<>(
                input -> DayEleven.blinkAtStones(input, 25, false),
                input -> DayEleven.blinkAtStones(input, 75));

        benchmarker.runBenchmark(stones, stones, 10, 100);
    }

    ///////////////////////
    // PART ONE SOLUTION //
    ///////////////////////
    
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
                newStones.add(stone / tens);
                newStones.add(mod);
            } else {
                newStones.add(stone * 2024);
            }
        }
        
        return newStones;
    }

    ///////////////////////
    // PART TWO SOLUTION //
    ///////////////////////
    
    private static void blinkAtStones(List<Long> stones, int blinkCount) {
        long total = 0;
        Map<Pair<Long, Integer>, Long> cache = new LinkedHashMap<>();
        for (long stone : stones) {
            total += blink(stone, blinkCount, cache);
        }
        
        System.out.println(String.format("Stone count: %d", total));
    }
    
    private static long blink(Long stone, int remainingBlinkCount, Map<Pair<Long, Integer>, Long> cache) {
        if (remainingBlinkCount == 0) {
            return 1;
        }

        long total = 0;
        Pair<Long, Integer> stoneBlinks = new Pair<Long, Integer>(stone, remainingBlinkCount);
        if (cache.containsKey(stoneBlinks)) {
            total = cache.get(stoneBlinks);
        } else {
            List<Long> newStones = new ArrayList<>();
            int digits;
            if (stone == 0) {
                newStones.add(1L);
            } else if ((digits = countDigits(stone)) % 2 == 0) {
                long tens = (long) Math.pow(10, (digits / 2));
                long mod = stone % tens;
                newStones.add(stone / tens);
                newStones.add(mod);
            } else {
                newStones.add(stone * 2024);
            }
    
            for (long newStone : newStones) {
                long stoneTotal = blink(newStone, remainingBlinkCount - 1, cache);
                total += stoneTotal;
            }

            populateCache(stone, remainingBlinkCount, total, cache);
        }
        
        return total;
    }
    
    private static void populateCache(Long stone, int remainingBlinkCount, long total, Map<Pair<Long, Integer>, Long> cache) {
        cache.put(new Pair<>(stone, remainingBlinkCount), total);
    }
    
    ///////////////////////
    // UTILITY FUNCTIONS //
    ///////////////////////
    
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
