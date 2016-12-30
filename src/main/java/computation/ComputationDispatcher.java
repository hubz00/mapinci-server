package computation;


import computation.algorithm.AlgorithmExecutionResult;
import computation.graphElements.Graph;
import computation.graphElements.Node;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComputationDispatcher {

    public final static ExecutorService executorService = Executors.newWorkStealingPool();
    private static final Map<Integer, Graph> graphs  = new ConcurrentHashMap<>();
    public static final Map<Node, List<AlgorithmExecutionResult>> resultsStartingFromNode = new ConcurrentHashMap<>();

    public static Graph getGraph(int key){
        return graphs.get(key);
    }

    public static void addGraph(Graph graph){
        graphs.put(graph.hashCode(), graph);
    }

    public static void removeGraph(int key){
        graphs.remove(key);
    }
}
