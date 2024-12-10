package adventofcode.year2024.day04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import adventofcode.utilities.FileLineReader;

public class DayFour {
    
    private enum Direction {
        RIGHT, LEFT, DOWN, UP, UP_RIGHT_DIAGONAL, UP_LEFT_DIAGONAL, DOWN_LEFT_DIAGONAL, DOWN_RIGHT_DIAGONAL;
    }
    
    private static Pattern xmasPattern = Pattern.compile("(XMAS)");

    public static void main(String[] args) {
        FileLineReader reader = new FileLineReader();
//        List<String> lines = reader.readLines(DayFour.class.getResource("tiny_cross_test_input.txt"));
//        List<String> lines = reader.readLines(DayFour.class.getResource("test_input.txt"));
        List<String> lines = reader.readLines(DayFour.class.getResource("input.txt"));

        long part1Start = System.currentTimeMillis();

        findAllXmases(lines);

        long part2Start = System.currentTimeMillis();
        
        findAllCrossMases(lines);

        long end = System.currentTimeMillis();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %dms", part2Start - part1Start));
        System.out.println(String.format("Part 2 duration: %dms", end - part2Start));
    }

    private static void findAllCrossMases(List<String> lines) {
        int total = 0;
        for (int x = 1; x < lines.get(0).length() - 1; x++) {
            for (int y = 1; y < lines.size() - 1; y++) {
                String letter = lines.get(y).substring(x, x + 1);
                if (!letter.equals("A")) {
                    continue;
                }
                
                List<Set<String>> diagonals = new ArrayList<>();
                // Top-left & bottom-right
                diagonals.add(new HashSet<>(Arrays.asList(lines.get(y - 1).substring(x - 1, x), lines.get(y + 1).substring(x + 1, x + 2))));
                // Top-right & bottom-left
                diagonals.add(new HashSet<>(Arrays.asList(lines.get(y - 1).substring(x + 1, x + 2), lines.get(y + 1).substring(x - 1, x))));
                
                if (diagonals.stream().allMatch(new HashSet<>(Arrays.asList("M", "S"))::equals)) {
                    total++;
                }
                
//                List<Set<String>> orthogonals = new ArrayList<>();
//                // Top & bottom
//                orthogonals.add(new HashSet<>(Arrays.asList(lines.get(y - 1).substring(x, x + 1), lines.get(y + 1).substring(x, x + 1))));
//                // Right & left
//                orthogonals.add(new HashSet<>(Arrays.asList(lines.get(y).substring(x - 1, x), lines.get(y).substring(x + 1, x + 2))));
//                
//                if (orthogonals.stream().allMatch(new HashSet<>(Arrays.asList("M", "S"))::equals)) {
//                    total++;
//                }
            }
        }
        
        System.out.println(String.format("Total X-MAS matches: %d", total));
    }

    private static void findAllXmases(List<String> lines) {
        Map<Direction, List<String>> transforms = transformLines(lines);
        
        Map<Direction, Integer> totals = new LinkedHashMap<>();
        for (Entry<Direction, List<String>> entry : transforms.entrySet()) {
            totals.put(entry.getKey(), findXmases(entry.getValue()));
        }
        
        int total = totals.values().stream().collect(Collectors.summingInt(val -> val));
        System.out.println(String.format("Total 'XMAS' matches: %d", total));
    }
    
    private static Map<Direction, List<String>> transformLines(List<String> lines) {
        Map<Direction, List<String>> transforms = new LinkedHashMap<>();

        // Left/right
        transforms.put(Direction.RIGHT, lines);
        transforms.put(Direction.LEFT, reverseStringsInList(lines));
        // Up/down
        transforms.put(Direction.DOWN, rotateStringsDown(lines));
        transforms.put(Direction.UP, reverseStringsInList(transforms.get(Direction.DOWN)));
        // Diagonals
        transforms.put(Direction.DOWN_RIGHT_DIAGONAL, rotateStringsDownRight(lines));
        transforms.put(Direction.UP_LEFT_DIAGONAL, reverseStringsInList(transforms.get(Direction.DOWN_RIGHT_DIAGONAL)));
        transforms.put(Direction.UP_RIGHT_DIAGONAL, rotateStringsUpRight(lines));
        transforms.put(Direction.DOWN_LEFT_DIAGONAL, reverseStringsInList(transforms.get(Direction.UP_RIGHT_DIAGONAL)));
        
        return(transforms);
    }
    
    private static List<String> rotateStringsDown(List<String> lines) {
        List<String> downLines = new ArrayList<>();
        for (int i = 0; i < lines.get(0).length(); i++) {
            final int ii = i;
            downLines.add(lines.stream().map(line -> line.substring(ii, ii + 1)).collect(Collectors.joining()));
        }
        
        return(downLines);
    }
    
    private static List<String> rotateStringsDownRight(List<String> lines) {
        List<String> downRightLines = new ArrayList<>();
        for (int i = lines.get(0).length() - 1; i > -lines.size(); i--) {
            String line = "";
            for (int x = Math.max(i, 0), y = Math.max(0, -i); x < lines.get(0).length() && y < lines.size(); x++, y++) {
                line += lines.get(y).substring(x, x + 1);
            }
            downRightLines.add(line);
        }
        
        return(downRightLines);
    }
    
    private static List<String> rotateStringsUpRight(List<String> lines) {
        List<String> upRightLines = new ArrayList<>();
        for (int i = lines.size() + lines.get(0).length() - 2; i >= 0; i--) {
            String line = "";
            for (int x = Math.max(i - (lines.size() - 1), 0), y = Math.min(i, lines.size() - 1); x < lines.get(0).length() && y >= 0; x++, y--) {
                line += lines.get(y).substring(x, x + 1);
            }
            upRightLines.add(line);
        }
        
        return(upRightLines);
    }
    
    private static List<String> reverseStringsInList(List<String> lines) {
        return(lines.stream().map(StringBuilder::new).map(builder -> builder.reverse().toString()).collect(Collectors.toList()));
    }

    private static int findXmases(List<String> lines) {
        int total = 0;
        for (String line : lines) {
            Matcher matcher = xmasPattern.matcher(line);
            int startIndex = 0;
            while (matcher.find(startIndex)) {
                total++;
                startIndex = matcher.end();
            }
        }
        
        return(total);
    }
}
