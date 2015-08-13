package net.coderodde.graph.support;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import net.coderodde.graph.AbstractGraphNode;

/**
 * This class implements a directed graph node.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6
 */
public class DirectedGraphNode extends AbstractGraphNode<DirectedGraphNode>  {

    /**
     * The set of incoming nodes, namely, the set of parent nodes.
     */
    private final Set<DirectedGraphNode> in;
    
    /**
     * The set of outgoing nodes, namely, the set of child nodes.
     */
    private final Set<DirectedGraphNode> out;
    
    /**
     * Constructs a new directed graph node with given name.
     * 
     * @param name the name of the node.
     */
    public DirectedGraphNode(String name) {
        super(name);
        this.in = new LinkedHashSet<>();
        this.out = new LinkedHashSet<>();
    }
    
    /**
     * {@inheritDoc } 
     */
    @Override
    public boolean addChild(DirectedGraphNode child) {
        Objects.requireNonNull(child, "The child node is null.");
        checkNodesBelongToSameGraph(this, child);
        
        boolean modified = out.add(child);
        
        if (!modified) {
            return false;
        }
        
        child.in.add(this);
        ownerGraph.incModificationCount();
        incEdgeAmount();
        return modified;
    }

    /**
     * {@inheritDoc } 
     */
    @Override
    public boolean removeChild(DirectedGraphNode child) {
        Objects.requireNonNull(child, "The child node is null.");
        checkNodesBelongToSameGraph(this, child);
        
        boolean modified = out.remove(child);
        
        if (!modified) {
            return false;
        }
        
        child.in.remove(this);
        ownerGraph.incModificationCount();
        decEdgeAmount();
        return modified;
    }

    /**
     * {@inheritDoc } 
     */
    @Override
    public void clear() {
        checkOwnerGraphNotNull();
        
        out.stream().forEach((child) -> {
            if (child.in.remove(this)) {
                ownerGraph.incModificationCount();
            }
        });
        
        in.stream().forEach((parent) -> {
            if (parent.out.remove(this)) {
                ownerGraph.incModificationCount();
            }
        });
        
        in.clear();
        out.clear();
    }

    /**
     * {@inheritDoc } 
     */
    @Override
    public boolean hasChild(DirectedGraphNode childCandidate) {
        checkOwnerGraphNotNull();
        return out.contains(childCandidate);
    }

    /**
     * {@inheritDoc } 
     */
    @Override
    public Set<DirectedGraphNode> children() {
        checkOwnerGraphNotNull();
        return Collections.<DirectedGraphNode>unmodifiableSet(out);
    }

    /**
     * {@inheritDoc } 
     */
    @Override
    public Set<DirectedGraphNode> parents() {
        checkOwnerGraphNotNull();
        return Collections.<DirectedGraphNode>unmodifiableSet(in);
    }
   
    /**
     * {@inheritDoc } 
     */
    @Override
    public String toString() {
        return "[DirectedGraphNode " + name + "]";
    }

    /**
     * {@inheritDoc } 
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * {@inheritDoc } 
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DirectedGraphNode)) {
            return false;
        }
        
        return this.name.equals(((DirectedGraphNode) o).name);
    }
    
    /**
     * Checks that the owner graph of this node is set.
     */
    private void checkOwnerGraphNotNull() {
        if (getOwnerGraph() == null) {
            throw new IllegalStateException(
                    "The node does not have an owner graph.");
        }
    }
}
