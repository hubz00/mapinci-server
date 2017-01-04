package computation;


import computation.algorithm.AlgorithmExecutionResult;
import computation.graphElements.Graph;
import computation.graphElements.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ComputationDispatcher {

    public final static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final Map<Integer, Graph> graphs  = new ConcurrentHashMap<>();
    private static  Map<Node, List<AlgorithmExecutionResult>> resultsStartingFromNode = new ConcurrentHashMap<>();
    private static final Map<Integer, List<Future<?>>> futuresForGraph = new ConcurrentHashMap<>();
    private Logger logger = LoggerFactory.getLogger(ComputationDispatcher.class);

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
        if(resultsStartingFromNode.containsKey(n)) {
            synchronized (resultsStartingFromNode.get(n)) {
                resultsStartingFromNode.get(n).add(result);
            }
        }
        else {
            List<AlgorithmExecutionResult> tmp = Collections.synchronizedList(new LinkedList<>());
            synchronized (tmp) {
                tmp.add(result);
                resultsStartingFromNode.put(n, tmp);
            }
        }
    }

    public static Map<Node, List<AlgorithmExecutionResult>> getResultsStartingFromNode() {
        return resultsStartingFromNode;
    }

    public static void removeResults(){
        resultsStartingFromNode.keySet().parallelStream().forEach(resultsStartingFromNode::remove);
    }

    public static boolean allRunnableFinished(Integer key){
        if((futuresForGraph.containsKey(key) && futuresForGraph.get(key) != null) ||futuresForGraph.isEmpty()) {
            try {
                synchronized (futuresForGraph.get(key)) {
                    for (Future f : futuresForGraph.get(key)) {
                        if (!f.isCancelled() && !f.isDone()) {
                            return false;
                        }
                    }
                    return true;
                }
            } catch (NullPointerException | ConcurrentModificationException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void addFuture(Integer graphHashCode, Future<?> f){
        if(futuresForGraph.containsKey(graphHashCode)){
            synchronized (futuresForGraph.get(graphHashCode)) {
                futuresForGraph.get(graphHashCode).add(f);
            }
        }
        else {
            List<Future<?>> tmp = Collections.synchronizedList(new LinkedList<>());
            synchronized (tmp) {
                tmp.add(f);
                futuresForGraph.put(graphHashCode, tmp);
            }
        }
    }

    public static void cleanResults(){
        resultsStartingFromNode = new ConcurrentHashMap<>();
    }
}
