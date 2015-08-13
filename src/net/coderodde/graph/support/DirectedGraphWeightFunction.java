package net.coderodde.graph.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.coderodde.graph.AbstractWeightFunction;

/**
 * This class implements weight functions for directed edges. This function is
 * asymmetric: the edges {@code (u, v}} and {@code (v, u)} may have different
 * weights, or some of them may be absent.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6
 */
public class DirectedGraphWeightFunction 
extends AbstractWeightFunction<DirectedGraphNode> {

    private final Map<DirectedGraphNode, Map<DirectedGraphNode, Double>> map;
    
    public DirectedGraphWeightFunction() {
        this.map = new HashMap<>();
    }
    
   /**
    * {@inheritDoc }
    */ 
    @Override
    public void put(DirectedGraphNode tail, 
                    DirectedGraphNode head, 
                    double weight) {
        Objects.requireNonNull(tail, "The tail node of an arc is null.");
        Objects.requireNonNull(head, "The head node of an arc is null.");
        checkWeight(weight);
        
        if (!map.containsKey(tail)) {
            map.put(tail, new HashMap<>());
        }
        
        map.get(tail).put(head, weight);
    }

   /**
    * {@inheritDoc }
    */ 
    @Override
    public double get(DirectedGraphNode tail, DirectedGraphNode head) {
        Objects.requireNonNull(tail, "The tail node of an arc is null.");
        Objects.requireNonNull(head, "The head node of an arc is null.");
        Map<DirectedGraphNode, Double> tmp;
        
        if (!map.containsKey(tail)) {
            throw new IllegalStateException(
                    "The requested arc (" + tail + ", " + head + ") has no " +
                    "weight in this weight function.");
        }
        
        tmp = map.get(tail);
        
        if (tmp == null) {
            throw new IllegalStateException(
                    "The requested arc (" + tail + ", " + head + ") has no " +
                    "weight in this weight function.");
        }
        
        return tmp.get(head);
    }
    
    /**
     * {@inheritDoc }
     */
    public void clear() {
        map.clear();
    }
}
