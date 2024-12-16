package adventofcode.utilities.graph;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class Graph<ItemT> {
    
    private final Set<Node<ItemT>> nodes = new LinkedHashSet<>();
    private final Set<Edge<ItemT>> edges = new LinkedHashSet<>();
    
    public Node<ItemT> addNode(ItemT item) {
        Node<ItemT> node = new Node<>(item);
        nodes.add(node);
        return node;
    }
    
    public boolean addEdge(ItemT from, ItemT to, double value) {
        Optional<Node<ItemT>> fromNode = getNode(from);
        if (fromNode.isEmpty()) {
            return false;
        }

        Optional<Node<ItemT>> toNode = getNode(to);
        if (toNode.isEmpty()) {
            return false;
        }
        
        return addEdge(fromNode.get(), toNode.get(), value);
    }
    
    public boolean addEdge(Node<ItemT> from, Node<ItemT> to, double value) {
        Edge<ItemT> edge = new Edge<>(from, to, value);
        if (edges.add(edge)) {
            from.addEdge(edge);
            return true;
        }
        
        return false;
    }
    
    public Set<Node<ItemT>> getNodes() {
        return new LinkedHashSet<>(nodes);
    }
    
    public Set<Edge<ItemT>> getEdges() {
        return new LinkedHashSet<>(edges);
    }
    
    public Optional<Node<ItemT>> getNode(ItemT item) {
        return nodes.stream().filter(node -> node.getItem().equals(item)).findFirst();
    }

}
