package computation;


import computation.algorithm.AlgorithmExecutionResult;
import computation.graphElements.Graph;
import computation.graphElements.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComputationDispatcher {

    public final static ExecutorService executorService = Executors.newWorkStealingPool();
    private static final Map<Integer, Graph> graphs  = new ConcurrentHashMap<>();
    private static final Map<Node, List<AlgorithmExecutionResult>> resultsStartingFromNode = new ConcurrentHashMap<>();

    public static Graph getGraph(int key){
        return graphs.get(key);
    }

    public static void addGraph(Graph graph){
        graphs.put(graph.hashCode(), graph);
    }

    public static void removeGraph(int key){
        graphs.remove(key);
    }

    public static void addNewAlgorithmResult(Node n, AlgorithmExecutionResult result){
        if(resultsStartingFromNode.containsKey(n))
            resultsStartingFromNode.get(n).add(result);
        else {
            List<AlgorithmExecutionResult> tmp = new LinkedList<>();
            tmp.add(result);
            resultsStartingFromNode.put(n, tmp);
        }
    }

    public static Map<Node, List<AlgorithmExecutionResult>> getResultsStartingFromNode() {
        return resultsStartingFromNode;
    }

    public static void removeResults(){
        resultsStartingFromNode.keySet().parallelStream().forEach(resultsStartingFromNode::remove);
    }
}
