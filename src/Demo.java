import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.coderodde.graph.Graph;
import net.coderodde.graph.pathfinding.AbstractWeightedPathFinder;
import net.coderodde.graph.pathfinding.support.DagShortestPathFinder;
import net.coderodde.graph.pathfinding.support.DijkstraPathFinder;
import net.coderodde.graph.support.DirectedGraphNode;
import net.coderodde.graph.support.DirectedGraphWeightFunction;

/**
 * This class demonstrates the performance of shortest path algorithm for dags 
 * (directed acyclic graphs).
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6
 */
public class Demo {
    
    private static final int LEVELS = 2000;
    private static final int MAX_LEVEL_WIDTH = 10;
    private static final int MAX_PROCEED = 15;
    private static final float EDGE_PROBABILITY = 0.01f;
    
    public static void main(String[] args) {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        WeightedDag weightedDag = createRandomDag(LEVELS,
                                                  MAX_LEVEL_WIDTH,
                                                  EDGE_PROBABILITY,
                                                  random);
        System.out.println("Seed: " + seed);
        
        long startTime = System.currentTimeMillis();
        
        DagShortestPathFinder finder1 = 
                new DagShortestPathFinder(weightedDag.graph,
                                          weightedDag.weightFunction);
        
        long endTime = System.currentTimeMillis();
        
        AbstractWeightedPathFinder<DirectedGraphNode> finder2 =
                new DijkstraPathFinder<>(weightedDag.weightFunction);
        
        System.out.println("Dag preprocessed in " + 
                           (endTime - startTime) + " milliseconds.");
        bar();
        
        List<DirectedGraphNode> nodeList = 
                new ArrayList<>(weightedDag.graph.getNodeAmount());
        
        for (DirectedGraphNode node : weightedDag.graph) {
            nodeList.add(node);
        }
        
        ////////////////////////////////////////////////////////////////////////
        DirectedGraphNode source = choose(nodeList, random);
        DirectedGraphNode target = choose(nodeList, random);
        
        System.out.println("Source node: " + source);
        System.out.println("Target node: " + target);
        bar();
        
        //// DAG SHORTEST PATH FINDER //////////////////////////////////////////
        startTime = System.currentTimeMillis();
        
        List<DirectedGraphNode> path1 = finder1.find(source, target);
        
        endTime = System.currentTimeMillis();
        
        System.out.println("Dag shortest path algorithm in " + 
                           (endTime - startTime) + " milliseconds.");
        
        System.out.println("Path size: " + path1.size() + ", is valid: " +
                           isValidPath(path1));
        bar();
        
        //// DIJKSTRA SHORTEST PATH FINDER /////////////////////////////////////
        startTime = System.currentTimeMillis();
        
        List<DirectedGraphNode> path2 = finder2.find(source, target);
        
        endTime = System.currentTimeMillis();
        
        System.out.println("Dijkstra's shortest path algorithm in " + 
                           (endTime - startTime) + " milliseconds.");
        
        System.out.println("Path size: " + path2.size() + ", is valid: " +
                           isValidPath(path2));
        bar();
        
        boolean identical = pathsIdentical(path1, path2);
        
        System.out.println("Paths are identical: " + 
                           pathsIdentical(path1, path2));
        
        if (identical) {
            for (DirectedGraphNode node : path1) {
                System.out.println(node);
            }
        }
    }
    
    private static class WeightedDag {
        Graph<DirectedGraphNode> graph;
        DirectedGraphWeightFunction weightFunction;
    }
    
    private static WeightedDag createRandomDag(int levels,
                                               int maxLevelWidth,
                                               float edgeProbability,
                                               Random random) {
        List<List<DirectedGraphNode>> levelList = new ArrayList<>();
        int nodeIndex = 0;
        
        Graph<DirectedGraphNode> graph = new Graph<>();
        DirectedGraphWeightFunction weightFunction = 
                new DirectedGraphWeightFunction();
        
        // Create nodes.
        for (int i = 0; i < levels; ++i) {
            List<DirectedGraphNode> level = new ArrayList<>(maxLevelWidth);
            int levelWidth = 1 + random.nextInt(maxLevelWidth);
            
            for (int j = 0; j < levelWidth; ++j) {
                DirectedGraphNode node = 
                        new DirectedGraphNode("Level " + i + ", "
                                                       + nodeIndex++);
                graph.addNode(node);
                level.add(node);
            }
            
            levelList.add(level);
        }
        
        // Create directed arcs.
        int edges = (int)(Math.min(0.95, edgeProbability) * nodeIndex 
                                                          * nodeIndex);
        
        while (edges > 0) {
            int a = random.nextInt(levelList.size());
            int b = Math.min(levelList.size() - 1, 
                             a + random.nextInt(MAX_PROCEED + 1));
            if (a < b) {
                DirectedGraphNode tail = choose(levelList.get(a), random);
                DirectedGraphNode head = choose(levelList.get(b), random);
                
                tail.addChild(head);
                weightFunction.put(tail, head, random.nextFloat());
                --edges;
            } else {
                // Tail and head nodes in the same level. Make sure the new arc
                // does not introduce cycles.
                int index1 = random.nextInt(levelList.get(a).size());
                int index2 = random.nextInt(levelList.get(a).size());
                
                if (index1 != index2) {
                    if (index1 > index2) {
                        int tmp = index1;
                        index1 = index2;
                        index2 = tmp;
                    }
                    
                    DirectedGraphNode tail = levelList.get(a).get(index1);
                    DirectedGraphNode head = levelList.get(a).get(index2);
                    
                    tail.addChild(head);
                    weightFunction.put(tail, head, random.nextFloat());
                    --edges;
                }
            }
        }
        
        WeightedDag ret = new WeightedDag();
        ret.graph = graph;
        ret.weightFunction = weightFunction;
        return ret;
    }
        
    private static <T> T choose(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }
    
    private static boolean pathsIdentical(List<DirectedGraphNode>... paths) {
        if (paths.length == 0) {
            return true;
        }
        
        for (int i = 0; i < paths.length - 1; ++i) {
            if (paths[i].size() != paths[i + 1].size()) {
                return false;
            }
        }
        
        for (int nodeIndex = 0; nodeIndex < paths[0].size(); ++nodeIndex) {
            for (int pathIndex = 0; pathIndex < paths.length - 1; ++pathIndex) {
                if (!paths[pathIndex].get(nodeIndex)
                                     .equals(paths[pathIndex +1]
                                             .get(nodeIndex))) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private static boolean isValidPath(List<DirectedGraphNode> path) {
        for (int i = 0; i < path.size() - 1; ++i) {
            if (!path.get(i).hasChild(path.get(i + 1))) {
                return false;
            }
        }
        
        return true;
    }
    
    private static void bar() {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < 80; ++i) {
            sb.append('-');
        }
        
        System.out.println(sb.toString());
    }
}
