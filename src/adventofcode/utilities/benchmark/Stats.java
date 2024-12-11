package adventofcode.utilities.benchmark;

class Stats {

    private double mean;
    private double median;
    private double ninetyNinthPercentile;
    
    public Stats(double mean, double median, double ninetyNinthPercentile) {
        this.mean = mean;
        this.median = median;
        this.ninetyNinthPercentile = ninetyNinthPercentile;
    }

    public double getMean() {
        return mean;
    }

    public double getMedian() {
        return median;
    }

    public double getNinetyNinthPercentile() {
        return ninetyNinthPercentile;
    }

}
