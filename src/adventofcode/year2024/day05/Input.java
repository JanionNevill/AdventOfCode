package adventofcode.year2024.day05;

import java.util.ArrayList;
import java.util.List;

import adventofcode.utilities.Pair;

class Input {

    private List<Pair<Integer, Integer>> rules = new ArrayList<>();
    private List<List<Integer>> sequences = new ArrayList<>();

    public void addRule(int first, int second) {
        rules.add(new Pair<>(first, second));
    }

    public void addSequence(List<Integer> sequence) {
        sequences.add(sequence);
    }

    public void clear() {
        rules.clear();
        sequences.clear();
    }

    public List<Pair<Integer, Integer>> getRules() {
        return rules;
    }

    public List<List<Integer>> getSequences() {
        return sequences;
    }
}