package adventofcode.utilities.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.ToDoubleBiFunction;

public class AStarPathfinder {
    
    public <ItemT> Optional<List<Node<ItemT>>> findShortestPath(Graph<ItemT> graph, Node<ItemT> from, Node<ItemT> to, ToDoubleBiFunction<Node<ItemT>, Node<ItemT>> heuristic) {
     // The set of discovered nodes that may need to be (re-)expanded.
        // Initially, only the start node is known.
        // This is usually implemented as a min-heap or priority queue rather than a hash-set.
        List<Node<ItemT>> openSet = new ArrayList<>();
        openSet.add(from);

        // For node n, cameFrom[n] is the node immediately preceding it on the cheapest path from the start
        // to n currently known.
        Map<Node<ItemT>, Node<ItemT>> cameFrom = new LinkedHashMap<>();

        // For node n, gScore[n] is the currently known cost of the cheapest path from start to n.
        // with default value of Infinity
        Map<Node<ItemT>, Double> gScore = new LinkedHashMap<>();
        gScore.put(from, 0.0);

        // For node n, fScore[n] := gScore[n] + h(n). fScore[n] represents our current best guess as to
        // how cheap a path could be from start to finish if it goes through n.
        // with default value of Infinity
        Map<Node<ItemT>, Double> fScore = new LinkedHashMap<>();
        fScore.put(from, heuristic.applyAsDouble(from, to));

        while (!openSet.isEmpty()) {
            openSet.sort(Comparator.comparingDouble(node -> fScore.getOrDefault(node, Double.POSITIVE_INFINITY)));
            Node<ItemT> current = openSet.get(0);
            
            if (current.equals(to)) {
                List<Node<ItemT>> path = new ArrayList<>();
                path.add(to);
                Node<ItemT> lastNode = to;
                while (!lastNode.equals(from)) {
                    lastNode = cameFrom.get(lastNode);
                    path.add(0, lastNode);
                }
                return Optional.of(path);
            }

            openSet.remove(current);
            for (Edge<ItemT> edge : current.getEdges()) {
                Node<ItemT> neighbour = edge.getEnd();
                
                // d(current,neighbor) is the weight of the edge from current to neighbor
                // tentative_gScore is the distance from start to the neighbor through current
                double tentative_gScore = gScore.getOrDefault(current, Double.POSITIVE_INFINITY) + edge.getValue();
                if (tentative_gScore < gScore.getOrDefault(neighbour, Double.POSITIVE_INFINITY)) {
                    // This path to neighbour is better than any previous one. Record it!
                    cameFrom.put(neighbour, current);
                    gScore.put(neighbour, tentative_gScore);
                    fScore.put(neighbour, tentative_gScore + heuristic.applyAsDouble(neighbour, to));
                    if (!openSet.contains(neighbour)) {
                        openSet.add(neighbour);
                    }
                }
            }
        }

        // Open set is empty but goal was never reached
        return Optional.empty();
    }

}
