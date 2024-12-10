package adventofcode.year2024.day02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import adventofcode.utilities.FileLineReader;

public class DayTwo {

    public static void main(String[] args) {
        List<List<Integer>> reports = readLevelsFile("input.txt");
//        List<List<Integer>> allLevels = readLevelsFile("10FailsOf14.txt");

        long part1Start = System.currentTimeMillis();

        calculateTotalSafeLevels(reports);

        long part2Start = System.currentTimeMillis();
        
        calculateTotalSafeLevelsWithDampener(reports);

        long end = System.currentTimeMillis();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %dms", part2Start - part1Start));
        System.out.println(String.format("Part 2 duration: %dms", end - part2Start));
    }

    private static void calculateTotalSafeLevels(List<List<Integer>> reports) {
        long safeReports = reports.stream()
                .filter(DayTwo::isReportSafe).count();

        System.out.println(String.format("Total safe levels: %d", safeReports));
    }

    private static void calculateTotalSafeLevelsWithDampener(List<List<Integer>> reports) {
        int totalSafe = 0;
        for (List<Integer> report : reports) {
            if (isReportSafe(report)) {
                totalSafe++;
                continue;
            }
            
            boolean dampenedSafe = false;
            for (int i = 0; i < report.size(); i++) {
                List<Integer> modifiedReport = new ArrayList<>(report);
                modifiedReport.remove(i);

                if (isReportSafe(modifiedReport)) {
                    dampenedSafe = true;
                    break;
                }
            }
            if (dampenedSafe) {
                totalSafe++;
            }
        }

        System.out.println(String.format("Total safe levels: %d", totalSafe));
    }

    private static boolean isReportSafe(List<Integer> report) {
        List<Integer> increments = new ArrayList<>();
        for (int i = 0; i < report.size() - 1; i++) {
            increments.add(report.get(i + 1) - report.get(i));
        }

        boolean reasonableLeaps = increments.stream().map(Math::abs)
                .allMatch(eachIncrement -> eachIncrement > 0 && eachIncrement < 4);
        if (!reasonableLeaps) {
            return(false);
        }

        boolean startsIncreasing = increments.get(0) > 0;
        boolean sameDirection = increments.stream()
                .allMatch(eachIncrement -> (eachIncrement > 0) == startsIncreasing);

        if (!sameDirection) {
            return(false);
        }
        
        return(true);
    }

    private static List<List<Integer>> readLevelsFile(String fileName) {
        List<List<Integer>> allLevels = new ArrayList<>();

        FileLineReader reader = new FileLineReader();
        List<String> lines = reader.readLines(DayTwo.class.getResource(fileName));
        try {
            for (String line : lines) {
                List<Integer> levels = Arrays.asList(line.split(" ")).stream().map(Integer::parseInt)
                        .collect(Collectors.toList());
                allLevels.add(levels);
            }
        } catch (NumberFormatException exptn) {
            System.err.println("Cannot parse levels");
            exptn.printStackTrace();
            lines.clear();
        }

        return (allLevels);
    }
}
