package adventofcode.year2024.day07;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToLongBiFunction;
import java.util.stream.Collectors;

import adventofcode.utilities.FileLineReader;

public class DaySeven {
    
    private enum Operation {
        
        PLUS((a, b) -> a + b),
        MULTIPLY((a, b) -> a * b),
//      CONCATENATE((a, b) -> a * (long) Math.pow(10, Math.ceil(Math.log10(b + 1))) + b);
        CONCATENATE((a, b) -> {
            long factor = 10;
            while (factor <= b) {
                factor *= 10;
            }
            return a * factor + b;
        });
        
        private ToLongBiFunction<Long, Long> function;
        
        private Operation(ToLongBiFunction<Long, Long> function) {
            this.function = function;
        }
        
        public long operate(long a, long b) {
            long result =  function.applyAsLong(a, b);

            if (result < a) {
                throw new IllegalArgumentException("Long overflow");
            }
            
            return result;
        }
    }
    
    public static void main(String[] args) {
//        List<Equation> equations = readInput("my_test_input.txt");
//        List<Equation> equations = readInput("test_input.txt");
        List<Equation> equations = readInput("input.txt");

        long part1Start = System.currentTimeMillis();
        
        checkForValidEquations(equations, Arrays.asList(Operation.PLUS, Operation.MULTIPLY));
        
        long part2Start = System.currentTimeMillis();

        checkForValidEquations(equations, Arrays.asList(Operation.PLUS, Operation.MULTIPLY, Operation.CONCATENATE));
        
        long end = System.currentTimeMillis();
        System.out.println();
        System.out.println(String.format("Part 1 duration: %dms", part2Start - part1Start));
        System.out.println(String.format("Part 2 duration: %dms", end - part2Start));
    }
    
    private static void checkForValidEquations(List<Equation> equations, List<Operation> allowedOperations) {
        long validTotal = 0;
        
        for (Equation eqn : equations) {
            List<Long> remainingValues = eqn.getArguments().subList(1, eqn.getArguments().size());
            if (recursivelyCheckValidity(eqn.getArguments().get(0), remainingValues, eqn.getResult(), allowedOperations)) {
                if (validTotal + eqn.getResult() < validTotal) {
                    throw new IllegalArgumentException("Long overflow");
                }
                
                validTotal += eqn.getResult();
            }
        }
        
        System.out.println(String.format("Total valid result sum: %d", validTotal));
    }
    
    private static boolean recursivelyCheckValidity(long currentValue, List<Long> remainingValues, long expectedValue, List<Operation> allowedOperations) {
        if (currentValue > expectedValue) {
            return false;
        } else if (remainingValues.isEmpty()) {
            return currentValue == expectedValue;
        }
        
        List<Long> newRemainingValues = remainingValues.subList(1, remainingValues.size());
        return allowedOperations.stream()
                .anyMatch(op -> recursivelyCheckValidity(op.operate(currentValue, remainingValues.get(0)), newRemainingValues, expectedValue, allowedOperations));
    }
    
    private static List<Equation> readInput(String filePath) {
        List<Equation> equations = new ArrayList<>();
        
        FileLineReader reader = new FileLineReader();
        for (String line : reader.readLines(DaySeven.class.getResource(filePath))) {
            String[] sides = line.split(":");
            
            List<Long> arguments = Arrays.asList(sides[1].strip().split(" ")).stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            
            long result = Long.parseLong(sides[0]);
            if (!String.valueOf(result).equals(sides[0])) {
                throw new IllegalArgumentException(String.format("Cannot parse number: %s", sides[0]));
            }
            
            equations.add(new Equation(result, arguments));
        }
        
        return equations;
    }

}
