package net.coderodde.graph.pathfinding.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import net.coderodde.graph.AbstractGraphNode;
import net.coderodde.graph.AbstractWeightFunction;
import net.coderodde.graph.pathfinding.AbstractWeightedPathFinder;

/**
 * This class implements Dijkstra's shortest path algorithm.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6
 * @param <N> the actual graph node implementation type.
 */
public class DijkstraPathFinder<N extends AbstractGraphNode<N>> 
extends AbstractWeightedPathFinder<N> {

    /**
     * The weight function.
     */
    private final AbstractWeightFunction<N> weightFunction;
    
    /**
     * Constructs this shortest path finder with given weight function.
     * 
     * @param weightFunction the weight function.
     */
    public DijkstraPathFinder(AbstractWeightFunction<N> weightFunction) {
        this.weightFunction = 
                Objects.requireNonNull(weightFunction,
                                       "The weight function is null.");
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<N> find(N source, N target) {
        checkNodes(source, target);
        
        Queue<NodeWrapper<N>> open = new PriorityQueue<>();
        Set<N> closed = new HashSet<>();
        
        Map<N, N> parentMap = new HashMap<>();
        Map<N, Double> distanceMap = new HashMap<>();
        
        open.add(new NodeWrapper<>(source, 0.0));
        parentMap.put(source, null);
        distanceMap.put(source, 0.0);
        
        while (!open.isEmpty()) {
            N current = open.poll().node;
            
            if (current.equals(target)) {
                return tracebackPath(target, parentMap);
            }
            
            // 'current' is settled.
            closed.add(current);
            
            for (N child : current.children()) {
                if (!closed.contains(child)) {
                    double w = distanceMap.get(current) + 
                               weightFunction.get(current, child);
                    
                    if (!distanceMap.containsKey(child) 
                            || distanceMap.get(child) > w) {
                        open.add(new NodeWrapper<>(child, w));
                        distanceMap.put(child, w);
                        parentMap.put(child, current);
                    }
                }
            }
        }
        
        return Collections.<N>emptyList();
    }
    
    // An ad hoc structure for describing nodes and costs.
    private static final class NodeWrapper<N> 
    implements Comparable<NodeWrapper<N>> {
        
        N node;
        double g;
        
        NodeWrapper(N node, double g) {
            this.node = node;
            this.g = g;
        }

        @Override
        public int compareTo(NodeWrapper<N> o) {
            return Double.compare(g, o.g);
        }
    }
    
    /**
     * Checks the source and target nodes are in order.
     * 
     * @param <N> the actual graph node implementation type.
     * @param source the source node.
     * @param target the target node.
     */
    private static <N extends AbstractGraphNode<N>> 
        void checkNodes(N source, N target) {
        Objects.requireNonNull(source, "The source node is null.");
        Objects.requireNonNull(target, "The target node is null.");
        Objects.requireNonNull(source.getOwnerGraph(), 
                               "The source node does not belong to any graph.");
        Objects.requireNonNull(target.getOwnerGraph(),
                               "The target node does not belong to any graph.");
        
        if (source.getOwnerGraph() != target.getOwnerGraph()) {
            throw new IllegalStateException(
                    "The source and the target node do not belong to the " +
                    "same graph");
        }
    }
}
