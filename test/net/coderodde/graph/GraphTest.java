package net.coderodde.graph;

import java.util.Iterator;
import net.coderodde.graph.support.DirectedGraphNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class GraphTest {
    
    private final DirectedGraphNode a = new DirectedGraphNode("A");
    private final DirectedGraphNode b = new DirectedGraphNode("B");
    private final DirectedGraphNode c = new DirectedGraphNode("C");
    private final DirectedGraphNode d = new DirectedGraphNode("D");
    private final DirectedGraphNode e = new DirectedGraphNode("E");
    private final DirectedGraphNode f = new DirectedGraphNode("F");
    
    @Test
    public void testAddNode() {
        Graph<DirectedGraphNode> graph = new Graph<>();
        
        assertEquals(0, graph.getNodeAmount());
        
        graph.addNode(a);
        
        assertEquals(1, graph.getNodeAmount());
        
        graph.addNode(c);
        
        assertEquals(2, graph.getNodeAmount());
        
        graph.addNode(e);
        
        assertEquals(3, graph.getNodeAmount());
        
        assertEquals(a, graph.getNode("A"));
        assertEquals(c, graph.getNode("C"));
        assertEquals(e, graph.getNode("E"));
        
        assertNull(graph.getNode("B"));
        assertNull(graph.getNode("D"));
        assertNull(graph.getNode("F"));
    }

    @Test
    public void testRemoveNode() {
        Graph<DirectedGraphNode> graph = new Graph<>();
        
        graph.addNode(b);
        graph.addNode(d);
        
        assertTrue(graph.removeNode(b));
        assertFalse(graph.removeNode(a));
        assertTrue(graph.removeNode(d));
    }

    @Test
    public void testGetModificationCount() {
        Graph<DirectedGraphNode> graph = new Graph<>();
        
        assertEquals(0, graph.getModificationCount());
        
        graph.addNode(a);
        
        assertEquals(1, graph.getModificationCount());
        
        graph.addNode(c);
        
        assertEquals(2, graph.getModificationCount());
        
        graph.addNode(d);
        
        assertEquals(3, graph.getModificationCount());
        
        a.addChild(c);
        
        assertEquals(4, graph.getModificationCount());
        
        a.addChild(d);
        
        assertEquals(5, graph.getModificationCount());
        
        a.clear();
        
        assertEquals(7, graph.getModificationCount());
        
        c.addChild(d);
        
        assertEquals(8, graph.getModificationCount());
        
        c.removeChild(d);
        
        assertEquals(9, graph.getModificationCount());
        
        graph.removeNode(a);
        
        assertEquals(10, graph.getModificationCount());
        
        graph.clear();
        
        assertEquals(12, graph.getModificationCount());
    }

    @Test(expected = NullPointerException.class)
    public void testGraphThrowsOnAddingNull() {
        new Graph<DirectedGraphNode>().addNode(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGraphThrowsOnRemovingNull() {
        new Graph<DirectedGraphNode>().removeNode(null);
    }
    
    @Test
    public void testGraphAddNodeReturnsFalseIfNodeAlreadyInGraph() {
        Graph<DirectedGraphNode> graph = new Graph<>();
        graph.addNode(c);
        assertFalse(graph.addNode(c));
    }
    
    @Test
    public void testGraphAddNodeClearesNodeBelongingToOtherGraph() {
        Graph<DirectedGraphNode> graph1 = new Graph<>();
        Graph<DirectedGraphNode> graph2 = new Graph<>();
        
        graph1.addNode(a);
        graph1.addNode(b);
        graph1.addNode(c);
        graph1.addNode(d);
        
        a.addChild(b);
        a.addChild(c);
        a.addChild(d);
        
        assertEquals(7, graph1.getModificationCount());
        
        assertTrue(graph2.addNode(a));
        
        assertEquals(11, graph1.getModificationCount());
        assertEquals(1, graph2.getModificationCount());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testThrowsOnAddingTwoNullGraphNodes() {
        a.addChild(c);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testThrowsOnAddingOneNullGraphNode1() {
        Graph<DirectedGraphNode> graph = new Graph<>();
        graph.addNode(a);
        a.addChild(c);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testThrowsOnAddingOneNullGraphNode2() {
        Graph<DirectedGraphNode> graph = new Graph<>();
        graph.addNode(c);
        a.addChild(c);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testThrowsOnAddingNodesFromDifferentGraphs() {
        Graph<DirectedGraphNode> graph1 = new Graph<>();
        Graph<DirectedGraphNode> graph2 = new Graph<>();
        
        graph1.addNode(c);
        graph2.addNode(a);
        a.addChild(c);
    }
    
    @Test
    public void testAddingNodeRemovesItFromOldGraph() {
        Graph<DirectedGraphNode> graph1 = new Graph<>();
        Graph<DirectedGraphNode> graph2 = new Graph<>();
        
        graph1.addNode(b);
        graph2.addNode(c);
        
        assertEquals(b, graph1.getNode(b.getName()));
        assertEquals(c, graph2.getNode(c.getName()));
        
        graph2.addNode(b);
        
        assertEquals(b, graph2.getNode(b.getName()));
        assertEquals(c, graph2.getNode(c.getName()));
        assertNull(graph1.getNode(b.getName()));
    }
    
    @Test
    public void testIterator() {
        Graph<DirectedGraphNode> graph = new Graph<>();
       
        graph.addNode(d);
        graph.addNode(b);
        graph.addNode(f);
        graph.addNode(c);
        
        Iterator<DirectedGraphNode> iter = graph.iterator();
        
        assertEquals(b, iter.next());
        assertEquals(c, iter.next());
        assertEquals(d, iter.next());
        assertEquals(f, iter.next());
        
        assertFalse(iter.hasNext());
        
        graph.clear();
        
        iter = graph.iterator();
        
        assertFalse(iter.hasNext());
    }
}
