package net.coderodde.graph.support;

import net.coderodde.graph.AbstractWeightFunction;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class DirectedGraphWeightFunctionTest {

    DirectedGraphNode a = new DirectedGraphNode("A");
    DirectedGraphNode b = new DirectedGraphNode("B");
    DirectedGraphNode c = new DirectedGraphNode("C");
    
    AbstractWeightFunction<DirectedGraphNode> f =
            new DirectedGraphWeightFunction();
    
    @Before 
    public void before() {
        f.clear();
    }
    
    @Test
    public void test() {
        f.put(a, b, 2.0);
        f.put(b, a, 3.0);
        
        assertEquals(2.0, f.get(a, b), 0.001);
        assertEquals(3.0, f.get(b, a), 0.001);
    }
    
    @Test(expected = NullPointerException.class)
    public void testThrowsOnMissingHeadNode1() {
        f.put(a, null, 1.0);
    }
    
    @Test(expected = NullPointerException.class)
    public void testThrowsOnMissingHeadNode2() {
        f.put(null, a, 1.0);
    }
    
    @Test(expected = NullPointerException.class)
    public void testThrowsOnMissingHeadNode3() {
        f.put(null, null, 1.0);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testThrowsOnNonExistentEdge() {
        f.get(a, c);
    }
}
