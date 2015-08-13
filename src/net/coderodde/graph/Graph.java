package net.coderodde.graph;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * This class implements the graph data structure.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6
 * @param <N> the actual graph node implementation type.
 */
public class Graph<N extends AbstractGraphNode<N>> implements Iterable<N> {
    
    /**
     * The amount of modifications made to this graph.
     */
    private int modificationCount;
    
    /**
     * Caches the amount of edges in this graph.
     */
    int edgeAmount;
    
    /**
     * Maps the names of nodes to nodes.
     */
    private final Map<String, N> map;
    
    /**
     * Constructs an empty graph.
     */
    public Graph() {
        this.map = new TreeMap<>();
    }
    
    /**
     * Adds the node {@code node} to this graph. If the input node belongs to
     * another graph, it is removed from it prior to adding the node to this 
     * graph.
     * 
     * @param node the node to add.
     * @return {@code true} if the graph structure was modified.
     */
    public boolean addNode(N node) {
        Objects.requireNonNull(node, "The input node is null.");
        
        if (node.getOwnerGraph() == this) {
            return false;
        }
        
        if (node.getOwnerGraph() != null) {
            node.clear();
            node.getOwnerGraph().removeNode(node);
        }
            
        map.put(node.getName(), node);
        node.ownerGraph = this;
        modificationCount++;
        return true;
    }
    
    /**
     * Attempts to remove the input node from this graph. The actual removal 
     * takes place only if the input node belongs to this graph.
     * 
     * @param node the node to remove.
     * @return {@code true} if the graph structure was modified.
     */
    public boolean removeNode(N node) {
        Objects.requireNonNull(node, "The input node is null.");
        
        if (node.getOwnerGraph() != this) {
            return false;
        }
        
        if (!map.containsKey(node.getName())) {
            return false;
        }
        
        node.clear();
        node.ownerGraph = null;
        map.remove(node.getName());
        modificationCount++;
        return true;
    }
    
    /**
     * Removes all the nodes from this graph and forgets all the edges/arcs.
     */
    public void clear() {
        map.values().stream().forEach((node) -> node.clear());
        modificationCount += map.size();
        map.clear();
    }
    
    /**
     * Gets a node by its name.
     * 
     * @param nodeName the name of the node.
     * @return the node with name {@code nodeName} or {@code null}  if there is
     *         no such.
     */
    public N getNode(String nodeName) {
        return map.get(nodeName);
    }
    
    public int getNodeAmount() {
        return map.size();
    }
    
    public int getModificationCount() {
        return modificationCount;
    }
    
    public void incModificationCount() {
        ++modificationCount;
    }

    @Override
    public Iterator<N> iterator() {
        return new NodeIterator();
    }
    
    private class NodeIterator implements Iterator<N> {

        private final Iterator<N> iterator;
        
        NodeIterator() {
            this.iterator = map.values().iterator();
        }
        
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public N next() {
            return iterator.next();
        }
    }
}
