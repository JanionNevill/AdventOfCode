package adventofcode.utilities.benchmark;

import java.io.OutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Benchmarker<PartOneInputT, PartTwoInputT> {

    private Consumer<PartOneInputT> partOneRunner;
    private Consumer<PartTwoInputT> partTwoRunner;

    public Benchmarker(Consumer<PartOneInputT> partOneRunner, Consumer<PartTwoInputT> partTwoRunner) {
        this.partOneRunner = partOneRunner;
        this.partTwoRunner = partTwoRunner;
    }

    public void runBenchmark(PartOneInputT partOneInput, PartTwoInputT partTwoInput, int burnIn, int iterations) {
        runBenchmark(partOneInput, partTwoInput, burnIn, burnIn, iterations, iterations);
    }

    public void runBenchmark(PartOneInputT partOneInput, PartTwoInputT partTwoInput, int partOneBurnIn, int partTwoBurnIn, int partOneIterations, int partTwoIterations) {
        List<Long> partOneDurations = new ArrayList<>();
        List<Long> partTwoDurations = new ArrayList<>();
        
        System.out.println("### Benchmark ###");
        System.out.println();
        
        PrintStream out = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            public void write(int value) {
                // Do nothing
            }
        }));

        for (int i = 0; i < partOneBurnIn; i++) {
            partOneRunner.accept(partOneInput);
        }

        for (int i = 0; i < partOneIterations; i++) {
            Instant partOneStart = Instant.now();
            
            partOneRunner.accept(partOneInput);
            
            Instant partOneEnd = Instant.now();
            partOneDurations.add(partOneStart.until(partOneEnd, ChronoUnit.MICROS));
        }

        for (int i = 0; i < partTwoBurnIn; i++) {
            partTwoRunner.accept(partTwoInput);
        }

        for (int i = 0; i < partTwoIterations; i++) {
            Instant partTwoStart = Instant.now();
            
            partTwoRunner.accept(partTwoInput);
            
            Instant partTwoEnd = Instant.now();
            partTwoDurations.add(partTwoStart.until(partTwoEnd, ChronoUnit.MICROS));
        }

        System.setOut(out);

        Stats partOneStats = calculateStats(partOneDurations);
        Stats partTwoStats = calculateStats(partTwoDurations);
        
        System.out.println(String.format("%-10s%-13s%-13s%-13s", "Name", "Mean", "Median", "99th %"));
        printStats("Part 1", partOneStats);
        printStats("Part 2", partTwoStats);
    }
    
    private void printStats(String name, Stats stats) {
        System.out.println(String.format("%-10s%-13s%-13s%-13s",
                name,
                String.format("%.1f us", stats.getMean()),
                String.format("%.1f us", stats.getMedian()),
                String.format("%.1f us", stats.getNinetyNinthPercentile())));
    }
    
    private Stats calculateStats(List<Long> durations) {
        durations.sort(Comparator.naturalOrder());
        double mean = durations.stream().collect(Collectors.summingDouble(val -> val)) / durations.size();
        double median = calculatePercentile(durations, 50);
        double ninetyNinthPercentile = calculatePercentile(durations, 99);
        
        return new Stats(mean, median, ninetyNinthPercentile);
    }
    
    private double calculatePercentile(List<Long> durations, int nthPercentile) {
        if (durations.size() == 1) {
            return durations.get(0);
        }
        
        double percentilePosition = durations.size() * nthPercentile / 100;
        int percentileIndex = (int) percentilePosition;
        double delta = percentilePosition - percentileIndex;
        
        if (delta == 0) {
            return durations.get(percentileIndex);
        } else {
            return durations.get(percentileIndex) * (1 - delta)
                    + durations.get(percentileIndex + 1) * delta;
        }
    }
    
//    public static void main(String[] args) {
//        Benchmarker<Integer, Integer> benchmarker = new Benchmarker<>(
//                (a, result) -> {
//                    result.setPartOneResult(1);
//                    return 1;
//                },
//                (a, result) -> result.setPartTwoResult(1),
//                new Result("Part 1: %d", "Part 2: %d"));
//        
//        benchmarker.runBenchmark(1, 1000);
//    }

}
