package net.coderodde.graph.pathfinding.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.coderodde.graph.Graph;
import net.coderodde.graph.pathfinding.AbstractWeightedPathFinder;
import net.coderodde.graph.support.DirectedGraphNode;
import net.coderodde.graph.support.DirectedGraphWeightFunction;

/**
 * This class implements a shortest path finder in dags (directed acyclic 
 * graphs).
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6
 */
public class DagShortestPathFinder 
extends AbstractWeightedPathFinder<DirectedGraphNode> {
    
    /**
     * The graph this finder works with.
     */
    private final Graph<DirectedGraphNode> graph;
    
    /**
     * The weight function for {@code graph}.
     */
    private final DirectedGraphWeightFunction weightFunction;
    
    /**
     * The list holding the nodes in topological order.
     */
    private final List<DirectedGraphNode> nodeList;
    
    /**
     * The map mapping each node to its appearance index in {@code nodeArray}.
     */
    private final Map<DirectedGraphNode, Integer> nodeIndexMap;
    
    private int graphModificationCount;
    
    /**
     * Constructs this path finder.
     * 
     * @param graph          the graph to search.
     * @param weightFunction the weight function.
     */
    public DagShortestPathFinder(Graph<DirectedGraphNode> graph,
                                 DirectedGraphWeightFunction weightFunction) {
        this.graph = Objects.requireNonNull(graph, "The input graph is null.");
        this.weightFunction = 
                Objects.requireNonNull(weightFunction,
                                       "The input weight function is null.");
        
        this.nodeList = new ArrayList<>();
        this.nodeIndexMap = new HashMap<>();
        
        processGraph();
    }

    @Override
    public List<DirectedGraphNode> find(DirectedGraphNode source, 
                                        DirectedGraphNode target) {
        Objects.requireNonNull(source, "The source node is null.");
        Objects.requireNonNull(target, "The target node is null.");
        
        checkNodeBelongsToGraph(source, 
                                graph, 
                                "The source node does not belong to the " + 
                                "current graph.");
        
        checkNodeBelongsToGraph(target,
                                graph, 
                                "The target node does not belong to the " +
                                "current graph.");
        
        checkGraph();
        
        int sourceIndex = nodeIndexMap.get(source);
        int targetIndex = nodeIndexMap.get(target);
        
        if (sourceIndex > targetIndex) {
            return Collections.<DirectedGraphNode>emptyList();
        }
        
        Map<DirectedGraphNode, Double> distanceMap = new HashMap<>();
        Map<DirectedGraphNode, DirectedGraphNode> parentMap = new HashMap<>();
        
        distanceMap.put(source, 0.0);
        parentMap.put(source, null);
        
        List<DirectedGraphNode> range = nodeList.subList(sourceIndex, 
                                                         targetIndex + 1);
        
        for (DirectedGraphNode current : range) {
            if (distanceMap.containsKey(current)) {
                if (current.equals(target)) {
                    // We reached the target node.
                    return tracebackPath(target, parentMap);
                }
                
                // Expand the children of 'current'.
                for (DirectedGraphNode child : current.children()) {
                    double edgeWeight = weightFunction.get(current, child);
                    
                    if (!distanceMap.containsKey(child)
                            || distanceMap.get(child) > 
                               distanceMap.get(current) + edgeWeight) {
                        parentMap.put(child, current);
                        distanceMap.put(child,
                                        distanceMap.get(current) + edgeWeight);
                    } 
                }
            }
        }
        
        // Path not found.
        return Collections.<DirectedGraphNode>emptyList();
    }
    
    /**
     * Checks whether the graph has changed, and if so, reprocesses it.
     */
    private void checkGraph() {
        if (graphModificationCount != graph.getModificationCount()) {
            processGraph();
        }
    }
    
    /**
     * Establish the data structures for faster path search.
     */
    private void processGraph() {
        graphModificationCount = graph.getModificationCount();
        this.nodeIndexMap.clear();
        this.nodeList.clear();
        this.nodeList.addAll(Arrays.asList(TopologicalSort.sort(graph)));
        
        for (int i = 0; i < nodeList.size(); ++i) {
            nodeIndexMap.put(nodeList.get(i), i);
        }
    }
    
    /**
     * Checks that the input node belongs to the input graph.
     * 
     * @param node         the node to check.
     * @param graph        the graph the node should belong to.
     * @param errorMessage the error message upon failure.
     * @throws IllegalStateException if the input node does not belong to input
     *                               graph.
     */
    private static void checkNodeBelongsToGraph(DirectedGraphNode node,
                                                Graph<DirectedGraphNode> graph,
                                                String errorMessage) {
        if (node.getOwnerGraph() != graph) {
            throw new IllegalStateException(errorMessage);
        }
    }
}
