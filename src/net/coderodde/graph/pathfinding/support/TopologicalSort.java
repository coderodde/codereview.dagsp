package net.coderodde.graph.pathfinding.support;

import java.util.Arrays;
import net.coderodde.graph.Graph;
import net.coderodde.graph.support.DirectedGraphNode;

/**
 * This class implements topological sort.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6
 */
public class TopologicalSort {

    /**
     * Returns the directed graph nodes in topological order. Note that the 
     * returned order may not be unique.
     * 
     * @param graph the graph to sort.
     * @return an array of nodes in topological order.
     * @throws IllegalStateException if the input graph is not acyclic.
     */
    public static DirectedGraphNode[] sort(Graph<DirectedGraphNode> graph) {
        DepthFirstSearchTraversal.DepthFirstSearchResult<DirectedGraphNode> state =
                new DepthFirstSearchTraversal<DirectedGraphNode>().search(graph);
        
        if (!state.isAcyclic) {
            throw new IllegalStateException("The input graph is not acyclic.");
        }
        
        DirectedGraphNode[] ret = new DirectedGraphNode[graph.getNodeAmount()];
        int index = 0;
        
        for (DirectedGraphNode node : graph) {
            ret[index++] = node;
        }
        
        Arrays.sort(ret, (DirectedGraphNode a, DirectedGraphNode b) -> {
            return state.endingTimeMap.get(b) - state.endingTimeMap.get(a);
        });
        
        return ret;
    }
}
