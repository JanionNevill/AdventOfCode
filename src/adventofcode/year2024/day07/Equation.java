package adventofcode.year2024.day07;

import java.util.List;

class Equation {
    
    private long result;
    private List<Long> arguments;
    
    public Equation(long result, List<Long> arguments) {
        this.result = result;
        this.arguments = arguments;
    }
    
    public long getResult() {
        return result;
    }
    
    public List<Long> getArguments() {
        return arguments;
    }
    
    public void setResult(long result) {
        this.result = result;
    }
    
    public void setArguments(List<Long> arguments) {
        this.arguments = arguments;
    }
}