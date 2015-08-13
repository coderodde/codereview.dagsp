package net.coderodde.graph.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.coderodde.graph.AbstractGraphNode;

/**
 * This abstract class defines the API and utilities for path finder algorithms.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6
 * @param <N> the actual graph node implementation type.
 */
public abstract class AbstractWeightedPathFinder<N extends AbstractGraphNode<N>> {
    
    /**
     * Searches a shortest path from {@code source} to {@code target}
     * 
     * @param source
     * @param target
     * @return 
     */
    public abstract List<N> find(N source, N target);
    
    /**
     * Traces back the path found by a path finder.
     * 
     * @param target    the target node.
     * @param parentMap the map mapping each node to its parent node.
     * @return a path as a list of nodes.
     */
    protected List<N> tracebackPath(N target, Map<N, N> parentMap) {
        List<N> ret = new ArrayList<>();
        N current = target;
        
        while (current != null) {
            // Append 'current' to the list.
            ret.add(current);
            current = parentMap.get(current);
        }
        
        // Here, the path in wrong order, reverse.
        Collections.<N>reverse(ret);
        return ret;
    }
}
