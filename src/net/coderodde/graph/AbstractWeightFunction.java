package net.coderodde.graph;

/**
 * This class defines the API for weight functions.
 * 
 * @author Rodion "rodde" Efremov
 * @param <N>
 */
public abstract class AbstractWeightFunction<N extends AbstractGraphNode<N>> {

    /**
     * Sets the weight for the edge {@code (tail, head)}.
     * 
     * @param tail   the tail node of the edge.
     * @param head   the head node of the edge.
     * @param weight the weight of the edge.
     */
    public abstract void put(N tail, N head, double weight);
    
    /**
     * Gets the weight of the edge {@code (tail, head)}.
     * 
     * @param tail the tail node of the edge.
     * @param head the head node of the edge.
     * @return the weight of the edge.
     * @throws IllegalStateException if this weight function does not contain 
     *                               the weight of the requested edges.
     */
    public abstract double get(N tail, N head);
    
    /**
     * Removes all edge weights from this map.
     */
    public abstract void clear();
    
    /**
     * Checks that the weight is not {@code NaN}.
     * 
     * @param weight the weight to check.
     */
    protected static void checkWeight(double weight) {
        if (Double.isNaN(weight)) {
            throw new IllegalArgumentException("The weight is NaN.");
        }
    }
}
