package adventofcode.year2024.day05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import adventofcode.utilities.FileLineReader;
import adventofcode.utilities.Pair;

public class DayFive {

    public static void main(String[] args) {
//        Input input = readInputFile("test_input.txt");
//        Input input = readInputFile("cutdown_test_input.txt");
        Input input = readInputFile("input.txt");

        long part1Start = System.currentTimeMillis();

        List<List<Integer>> incorrectSequences = sumValidMiddlePages(input);

        long part2Start = System.currentTimeMillis();
        
        fixAndSumMiddlePages(input, incorrectSequences);

        long end = System.currentTimeMillis();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %dms", part2Start - part1Start));
        System.out.println(String.format("Part 2 duration: %dms", end - part2Start));
    }

    private static List<List<Integer>> sumValidMiddlePages(Input input) {
        List<List<Integer>> incorrectSequences = new ArrayList<>();
        
        int total = 0;
        for (List<Integer> sequence : input.getSequences()) {
            boolean correctOrder = input.getRules().stream()
                    .map(rule -> new Pair<>(sequence.indexOf(rule.getFirst()), sequence.indexOf(rule.getSecond())))
                    .filter(indices -> indices.getFirst() >= 0 && indices.getSecond() >= 0)
                    .allMatch(indices -> indices.getFirst() < indices.getSecond());
            if (correctOrder) {
                total += findMiddlePage(sequence);
            } else {
                incorrectSequences.add(sequence);
            }
        }
        
        System.out.println(String.format("Middle page total: %d", total));
        
        return incorrectSequences;
    }
    
    private static void fixAndSumMiddlePages(Input input, List<List<Integer>> incorrectSequences) {
        int total = 0;
        for (List<Integer> sequence : incorrectSequences) {

            List<Pair<Integer, Integer>> relevantRules = input.getRules().stream()
                    .filter(rule -> sequence.indexOf(rule.getFirst()) != -1 && sequence.indexOf(rule.getSecond()) != -1)
                    .collect(Collectors.toList());
            
            RuleSet root = createRuleSetTree(sequence, relevantRules);
//            List<Integer> correctedSequence = resursivelyTraverseRuleSetTree(root, new ArrayList<>(), sequence.size());
            List<Integer> correctedSequence = traverseRuleSetTree(root, sequence);
            
            if (correctedSequence == null) {
                String sequenceString = String.join(", ", sequence.stream().map(String::valueOf).collect(Collectors.toList()));
                System.err.println(String.format("Cannot find corrected sequence for: %s", sequenceString));
                continue;
            }
            
            total += findMiddlePage(correctedSequence);
        }
        
        System.out.println(String.format("Middle page total: %d", total));
    }
    
    private static List<Integer> traverseRuleSetTree(RuleSet root, List<Integer> sequence) {
        List<Integer> correctedSequence = new ArrayList<>();
        correctedSequence.add(root.getNumber());
        
        while (correctedSequence.size() < sequence.size()) {
            List<RuleSet> noMoreBefores = root.getAfters().stream()
                    .filter(ruleSet -> !correctedSequence.contains(ruleSet.getNumber()))
                    .filter(ruleSet -> beforesSatisfied(ruleSet.getBefores(), correctedSequence))
                    .collect(Collectors.toList());
            List<Integer> beforePeers = noMoreBefores.stream()
                    .filter(ruleSet -> noPeersBefore(ruleSet, noMoreBefores))
                    .map(RuleSet::getNumber)
                    .collect(Collectors.toList());
            
            if (beforePeers.isEmpty()) {
                return null;
            }
            
            correctedSequence.addAll(beforePeers);
        }
        
        return correctedSequence;
    }
    
    private static boolean beforesSatisfied(List<RuleSet> befores, List<Integer> currentSequence) {
        return befores.stream().map(RuleSet::getNumber).allMatch(currentSequence::contains);
    }
    
    private static boolean noPeersBefore(RuleSet candidate, List<RuleSet> peers) {
        List<Integer> peerNumbers = peers.stream().map(RuleSet::getNumber).collect(Collectors.toList());
        return !candidate.getBefores().stream().map(RuleSet::getNumber).anyMatch(peerNumbers::contains);
    }
    
//    private static List<Integer> resursivelyTraverseRuleSetTree(RuleSet nextLevel, List<Integer> correctedSequence, int expectedLength) {
//        if (correctedSequence.contains(nextLevel.getNumber())) {
//            // This should be impossible due to closed loops
//            return(null);
//        }
//        
//        List<Integer> newCorrectedSequence = new ArrayList<>(correctedSequence);
//        newCorrectedSequence.add(nextLevel.getNumber());
//        
//        if (nextLevel.afters.isEmpty()) {
//            if (newCorrectedSequence.size() == expectedLength) {
//                return newCorrectedSequence;
//            } else {
//                return null;
//            }
//        }
//        
//        for (RuleSet after : nextLevel.getAfters()) {
//            List<Integer> deeperCorrectedSequence = resursivelyTraverseRuleSetTree(after, newCorrectedSequence, expectedLength);
//            
//            if (deeperCorrectedSequence != null) {
//                return(deeperCorrectedSequence);
//            }
//        }
//        
//        return null;
//    }
    
    private static RuleSet createRuleSetTree(List<Integer> sequence, List<Pair<Integer, Integer>> rules) {
        Map<Integer, RuleSet> ruleSets = new LinkedHashMap<>();
        
        for (Pair<Integer, Integer> rule : rules) {
            int first = rule.getFirst();
            int second = rule.getSecond();
            RuleSet firstRuleSet = ruleSets.computeIfAbsent(first, RuleSet::new);
            RuleSet secondRuleSet = ruleSets.computeIfAbsent(second, RuleSet::new);
            
            firstRuleSet.addAfter(secondRuleSet);
            secondRuleSet.addBefore(firstRuleSet);
        }
        
        List<RuleSet> roots = ruleSets.values().stream()
                .filter(ruleSet -> ruleSet.getBefores().isEmpty())
                .collect(Collectors.toList());

        if (roots.isEmpty()) {
            System.err.println("No roots found for incorrect sequence");
            return(null);
        } else if (roots.size() > 1) {
            System.err.println("Multiple roots found for incorrect sequence");
        }
        
        return roots.get(0);
    }
    
    private static int findMiddlePage(List<Integer> sequence) {
        return sequence.get(sequence.size() / 2);
    }

    private static Input readInputFile(String filePath) {
        Input input = new Input();

        FileLineReader reader = new FileLineReader();
        List<String> lines = reader.readLines(DayFive.class.getResource(filePath));
        try {
            for (String line : lines) {
                if (line.contains("|")) {
                    String[] pages = line.split("\\|");
                    input.addRule(Integer.parseInt(pages[0]), Integer.parseInt(pages[1]));
                } else if (line.contains(",")) {
                    String[] pages = line.split(",");
                    List<Integer> sequence = Arrays.asList(pages).stream().map(Integer::parseInt)
                            .collect(Collectors.toList());
                    input.addSequence(sequence);
                }
            }
        } catch (NumberFormatException exptn) {
            System.err.println("Cannot parse input");
            exptn.printStackTrace();
            input.clear();
        }

        return input;
    }
}
