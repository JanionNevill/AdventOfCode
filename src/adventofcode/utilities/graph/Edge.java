package adventofcode.utilities.graph;

public class Edge<ItemT> {

    private final Node<ItemT> start;
    private final Node<ItemT> end;
    private final double value;
    
    public Edge(Node<ItemT> start, Node<ItemT> end, double value) {
        this.start = start;
        this.end = end;
        this.value = value;
    }

    public Node<ItemT> getStart() {
        return start;
    }

    public Node<ItemT> getEnd() {
        return end;
    }

    public double getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return String.format("FROM: %s : TO: %s : VALUE: %f", start.getItem(), end.getItem(), value);
    }
    
}
