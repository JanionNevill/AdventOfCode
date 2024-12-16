package adventofcode.utilities.graph;

import java.util.LinkedHashSet;
import java.util.Set;

public class Node<ItemT> {
    
    private final ItemT item;
    private final Set<Edge<ItemT>> edges = new LinkedHashSet<>();
    
    public Node(ItemT item) {
        this.item = item;
    }
    
    protected void addEdge(Edge<ItemT> edge) {
        edges.add(edge);
    }
    
    public ItemT getItem() {
        return item;
    }
    
    public Set<Edge<ItemT>> getEdges() {
        return new LinkedHashSet<>(edges);
    }

}
