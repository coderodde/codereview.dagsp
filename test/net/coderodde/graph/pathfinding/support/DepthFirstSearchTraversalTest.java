package net.coderodde.graph.pathfinding.support;

import net.coderodde.graph.Graph;
import net.coderodde.graph.support.DirectedGraphNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class DepthFirstSearchTraversalTest {

    @Test
    public void testSearch() {
        // This graph is from "Introduction to Algorithms", 3rd edition,
        // chapter 22.3, page 605.
        DirectedGraphNode u = new DirectedGraphNode("u");
        DirectedGraphNode v = new DirectedGraphNode("v");
        DirectedGraphNode w = new DirectedGraphNode("w");
        DirectedGraphNode x = new DirectedGraphNode("x");
        DirectedGraphNode y = new DirectedGraphNode("y");
        DirectedGraphNode z = new DirectedGraphNode("z");
        
        Graph<DirectedGraphNode> graph = new Graph<>();
        
        graph.addNode(u);
        graph.addNode(v);
        graph.addNode(w);
        graph.addNode(x);
        graph.addNode(y);
        graph.addNode(z);
        
        u.addChild(v);
        u.addChild(x);
        
        v.addChild(y);
        
        w.addChild(y);
        w.addChild(z);
        
        x.addChild(v);
        y.addChild(x);
        z.addChild(z);
        
        DepthFirstSearchTraversal.DepthFirstSearchResult<DirectedGraphNode> result =
                new DepthFirstSearchTraversal<DirectedGraphNode>().search(graph);
        
        assertEquals(1, (int) result.startingTimeMap.get(u));
        assertEquals(2, (int) result.startingTimeMap.get(v));
        assertEquals(3, (int) result.startingTimeMap.get(y));
        assertEquals(4, (int) result.startingTimeMap.get(x));
        assertEquals(9, (int) result.startingTimeMap.get(w));
        assertEquals(10, (int) result.startingTimeMap.get(z));
        
        assertEquals(8, (int) result.endingTimeMap.get(u));
        assertEquals(7, (int) result.endingTimeMap.get(v));
        assertEquals(6, (int) result.endingTimeMap.get(y));
        assertEquals(5, (int) result.endingTimeMap.get(x));
        assertEquals(12, (int) result.endingTimeMap.get(w));
        assertEquals(11, (int) result.endingTimeMap.get(z));
        
        assertNull(result.parentMap.get(u));
        assertNull(result.parentMap.get(w));
        
        assertEquals(u, result.parentMap.get(v));
        assertEquals(v, result.parentMap.get(y));
        assertEquals(y, result.parentMap.get(x));
        assertEquals(w, result.parentMap.get(z));
        
        assertFalse(result.isAcyclic);
    }
    
    @Test
    public void testAcyclic() {
        DirectedGraphNode a = new DirectedGraphNode("A");
        DirectedGraphNode b = new DirectedGraphNode("B");
        DirectedGraphNode c = new DirectedGraphNode("C");
        
        Graph<DirectedGraphNode> graph = new Graph<>();
        
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        
        a.addChild(b);
        b.addChild(c);
        
        DepthFirstSearchTraversal.DepthFirstSearchResult<DirectedGraphNode> result =
                new DepthFirstSearchTraversal<DirectedGraphNode>().search(graph);
        
        assertTrue(result.isAcyclic);
    }
}
