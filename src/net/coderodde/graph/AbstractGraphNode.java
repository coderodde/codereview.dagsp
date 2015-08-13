package net.coderodde.graph;

import net.coderodde.graph.support.DirectedGraphNode;
import java.util.Objects;
import java.util.Set;

/**
 * This abstract class defines the API for graph nodes.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6
 * @param <N> the actual graph node implementation type.
 */
public abstract class AbstractGraphNode<N extends AbstractGraphNode<N>> {
    
    /**
     * The name of this node.
     */
    protected final String name;
    
    /**
     * Caches the graph this node belongs to.
     */
    protected Graph<N> ownerGraph;
    
    /**
     * Constructs this node with given name.
     * 
     * @param name the name of this node.
     */
    public AbstractGraphNode(String name) {
        Objects.requireNonNull(name, "The node name is null.");
        this.name = name;
    }
    
    /**
     * Attempts to add a child to this node.
     * 
     * @param child the child node to add.
     * @return {@code true} if the structure of the graph changed.
     * @throws NullPointerException if {@code child} if {@code null}.
     */
    public abstract boolean addChild(DirectedGraphNode child);

    /**
     * Attempts to remove a child from the child list of this node.
     * 
     * @param child the child node to remove.
     * @return {@code true} only if the structure of the graph changed.
     */
    public abstract boolean removeChild(DirectedGraphNode child);

    /**
     * Removes all incoming and out-going arcs from this node.
     */
    public abstract void clear();

    /**
     * Returns {@code true} if {@code childCanidate} is a child of this node.
     * 
     * @param childCandidate the node to check for "childness".
     * @return {@code true} if {@code childCandidate} is a child node of this 
     *         node.
     */
    public abstract boolean hasChild(DirectedGraphNode childCandidate);

    /**
     * Returns an immutable set view of child nodes of this node.
     * 
     * @return the child set.
     */
    public abstract Set<N> children();

    /**
     * Returns an immutable set view of parent nodes of this node.
     * 
     * @return the parent set.
     */
    public abstract Set<N> parents();
    
    // Force the user to implement 'hashCode'.
    /**
     * {@inheritDoc }
     */
    @Override
    public abstract int hashCode();
    
    // Force the user to implement 'equals'.
    /**
     * @return 
     */
    @Override
    public abstract boolean equals(Object o);
    
    /**
     * Returns the graph this node belongs to.
     * 
     * @return the owner graph.
     */
    public Graph<N> getOwnerGraph() {
        return ownerGraph;
    }
    
    /**
     * Returns the name of this node.
     * 
     * @return the name of this node.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Checks that the two input nodes belong to the same graph.
     * 
     * @param <N>   the actual graph node implementation type.
     * @param node1 the first node to check.
     * @param node2 the second node to check.
     */
    protected static <N> 
        void checkNodesBelongToSameGraph(AbstractGraphNode<?> node1,
                                         AbstractGraphNode<?> node2) {
        if (node1.getOwnerGraph() == null) {
            throw new IllegalStateException(
                    "The first node does not belong to any graph.");
        }
        
        if (node1.getOwnerGraph() == null) {
            throw new IllegalStateException(
                    "The second node does not belong to any graph.");
        }
        
        if (node1.getOwnerGraph() != node2.getOwnerGraph()) {
            throw new IllegalStateException(
                    "The two nodes belong to different graphs.");
        }
    }
        
    protected final void incEdgeAmount() {
        ++ownerGraph.edgeAmount;
    }
    
    protected final void decEdgeAmount() {
        --ownerGraph.edgeAmount;
    }
}
