package adventofcode.year2024.day05;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class RuleSet {
    
    private int number;
    private List<RuleSet> befores;
    List<RuleSet> afters;
    
    public RuleSet(int number) {
        this.number = number;
        this.befores = new ArrayList<>();
        this.afters = new ArrayList<>();
    }
    
    public void addBefore(RuleSet before) {
        befores.add(before);
    }
    
    public void addAfter(RuleSet after) {
        afters.add(after);
    }

    public int getNumber() {
        return number;
    }
    
    public List<RuleSet> getBefores() {
        return befores;
    }

    public List<RuleSet> getAfters() {
        return afters;
    }
    
    @Override
    public String toString() {
        List<String> afterNumberStrings = afters.stream()
                .map(RuleSet::getNumber)
                .map(String::valueOf)
                .collect(Collectors.toList());
        String aftersString = String.join(", ", afterNumberStrings);
        return String.format("%d: %s", number, aftersString);
    }

}