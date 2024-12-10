package adventofcode.year2024.day03;

import static java.lang.Integer.parseInt;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adventofcode.utilities.FileLineReader;

public class DayThree {

    public static void main(String[] args) {
        FileLineReader reader = new FileLineReader();
        List<String> lines = reader.readLines(DayThree.class.getResource("input.txt"));

        long part1Start = System.currentTimeMillis();

        calculateTotalMultiplication(lines);

        long part2Start = System.currentTimeMillis();
        
        calculateTotalFilteredMultiplication(lines);

        long end = System.currentTimeMillis();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %dms", part2Start - part1Start));
        System.out.println(String.format("Part 2 duration: %dms", end - part2Start));
    }

    private static void calculateTotalMultiplication(List<String> lines) {
        Pattern pattern = Pattern.compile("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)");
        
        int total = 0;
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            int startIndex = 0;
            while (matcher.find(startIndex)) {
                total += parseInt(matcher.group(1)) * parseInt(matcher.group(2));
                startIndex = matcher.end();
            }
        }
        
        System.out.println(String.format("Total multiplications: %d", total));
    }

    private static void calculateTotalFilteredMultiplication(List<String> lines) {
        Pattern mulPattern = Pattern.compile("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)");
        Pattern doPattern = Pattern.compile("do\\(\\)");
        Pattern dontPattern = Pattern.compile("don't\\(\\)");
        
        boolean enabled = true;
        int total = 0;
        for (String line : lines) {
            Matcher mulMatcher = mulPattern.matcher(line);
            Matcher doMatcher = doPattern.matcher(line);
            Matcher dontMatcher = dontPattern.matcher(line);
            
            int startIndex = 0;
            while (startIndex < line.length() - 1) {
                if (!enabled) {
                    if (doMatcher.find(startIndex)) {
                        startIndex = doMatcher.end();
                        enabled = true;
                    } else {
                        startIndex = line.length();
                    }
                } else if (enabled) {
                    int nextDont = dontMatcher.find(startIndex) ? dontMatcher.end() : line.length();
                    while (mulMatcher.find(startIndex)) {
                        if (mulMatcher.start() < nextDont) {
                            total += parseInt(mulMatcher.group(1)) * parseInt(mulMatcher.group(2));
                            startIndex = mulMatcher.end();
                        } else {
                            startIndex = nextDont;
                            enabled = false;
                            break;
                        }
                    }
                }
            }
        }
        
        System.out.println(String.format("Total filtered multiplications: %d", total));
    }
}
