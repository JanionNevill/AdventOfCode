package adventofcode.year2024.day01;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import adventofcode.utilities.FileLineReader;
import adventofcode.utilities.Pair;

public class DayOne {

    public static void main(String[] args) {
        Pair<List<Integer>, List<Integer>> lists = readLocationIdFile();
        List<Integer> firstList = lists.getFirst();
        List<Integer> secondList = lists.getSecond();

        long part1Start = System.currentTimeMillis();

        calculateTotalDistance(firstList, secondList);

        long part2Start = System.currentTimeMillis();
        
        calculateSimilarityScore(firstList, secondList);

        long end = System.currentTimeMillis();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %dms", part2Start - part1Start));
        System.out.println(String.format("Part 2 duration: %dms", end - part2Start));
    }

    private static void calculateTotalDistance(List<Integer> firstList, List<Integer> secondList) {
        int totalDistance = 0;
        for (int i = 0; i < firstList.size(); i++) {
            int distance = Math.abs(firstList.get(i) - secondList.get(i));
            totalDistance += distance;
        }

        System.out.println(String.format("Total distance between lists: %d", totalDistance));
    }

    private static void calculateSimilarityScore(List<Integer> firstList, List<Integer> secondList) {
        int totalSimilarityScore = 0;
        for (int item : firstList) {
            long secondListOccurrances = secondList.stream().filter(eachItem -> eachItem == item).count();
            long similarityScore = item * secondListOccurrances;
            totalSimilarityScore += similarityScore;
        }

        System.out.println(String.format("Total similarity score: %d", totalSimilarityScore));
    }

    private static Pair<List<Integer>, List<Integer>> readLocationIdFile() {
        List<Integer> firstList = new ArrayList<>();
        List<Integer> secondList = new ArrayList<>();

        FileLineReader reader = new FileLineReader();
        List<String> lines = reader.readLines(DayOne.class.getResource("input.txt"));
        try {
            for (String line : lines) {
                String[] locationIds = line.split("\\s+");
                firstList.add(Integer.parseInt(locationIds[0]));
                secondList.add(Integer.parseInt(locationIds[1]));
            }
        } catch (NumberFormatException exptn) {
            System.err.println("Cannot parse location ID");
            exptn.printStackTrace();
            firstList.clear();
            secondList.clear();
        }

        if (firstList.size() != secondList.size()) {
            System.err.println("Lists of location IDs are different lengths");
            firstList.clear();
            secondList.clear();
        }

        firstList.sort(Comparator.naturalOrder());
        secondList.sort(Comparator.naturalOrder());

        return (new Pair<>(firstList, secondList));
    }
}
