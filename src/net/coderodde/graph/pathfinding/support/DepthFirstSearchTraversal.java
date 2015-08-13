package net.coderodde.graph.pathfinding.support;

import java.util.HashMap;
import java.util.Map;
import net.coderodde.graph.AbstractGraphNode;
import net.coderodde.graph.Graph;

/**
 * This class implements depth-first search traversal.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6
 * @param <N> the actual graph node implementation type.
 */
public class DepthFirstSearchTraversal<N extends AbstractGraphNode<N>> {

    public static final Boolean GRAY = Boolean.FALSE;
    public static final Boolean BLACK = Boolean.TRUE;
    
    /**
     * Describes the graph after being processed by depth-first search.
     * 
     * @param <N> the actual graph node type.
     */
    public static final class DepthFirstSearchResult<N> {
        public final Map<N, Integer> startingTimeMap;
        public final Map<N, Integer> endingTimeMap;
        public final Map<N, Boolean> colorMap;
        public final Map<N, N> parentMap;
        public final boolean isAcyclic;

        public DepthFirstSearchResult(Map<N, Integer> startingTimeMap,
                                      Map<N, Integer> endingTimeMap,
                                      Map<N, Boolean> colorMap,
                                      Map<N, N> parentMap,
                                      boolean isAcyclic) {
            this.startingTimeMap = startingTimeMap;
            this.endingTimeMap = endingTimeMap;
            this.colorMap = colorMap;
            this.parentMap = parentMap;
            this.isAcyclic = isAcyclic;
        }
    }
    
    private int time;
    private Map<N, Integer> startingTimeMap;
    private Map<N, Integer> endingTimeMap;
    private Map<N, Boolean> colorMap;
    private Map<N, N> parentMap;
    private boolean isAcyclic;
    
    /**
     * Runs depth-first search on the input graph. This algorithm is taken from
     * "Introduction to Algorithms" 3rd edition, chapter 22, page 604.
     * 
     * @param graph the graph to traverse.
     * @return data structures describing the graph.
     */
    public DepthFirstSearchResult<N> search(Graph<N> graph) {
        // Create new state.
        time = 0;
        startingTimeMap = new HashMap<>();
        endingTimeMap = new HashMap<>();
        colorMap = new HashMap<>();
        parentMap = new HashMap<>();
        isAcyclic = true;
        
        for (N node : graph) {
            parentMap.put(node, null);
        }
        
        // Traverse.
        for (N node : graph) {
            // If 'node' is not in the 'colorMap', its color is assumed to be
            // white
            if (!colorMap.containsKey(node)) {
                visit(node);
            }
        }
        
        return new DepthFirstSearchResult<>(startingTimeMap,
                                            endingTimeMap,
                                            colorMap,
                                            parentMap,
                                            isAcyclic);
    }
    
    // Implementation.
    private void visit(N node) {
        startingTimeMap.put(node, ++time);
        colorMap.put(node, GRAY);
        
        for (N child : node.children()) {
            // If the color of 'child' is white, do...
            if (!colorMap.containsKey(child)) {
                parentMap.put(child, node);
                visit(child);
            } else if (colorMap.get(child).equals(GRAY)) {
                // We have a cycle.
                isAcyclic = false;
            }
        }
        
        colorMap.put(node, BLACK);
        endingTimeMap.put(node, ++time);
    }
}
