package computation.algorithm;

import computation.ComputationDispatcher;
import computation.algorithm.conditions.ConditionManager;
import computation.graphElements.Graph;
import computation.graphElements.Node;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentSoul;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class AlgorithmExecutor implements Runnable{

    private List<SegmentSoul> shape;
    private Node startNode;
    private ConditionManager conditionManager;
    private int graphKey;
    private Logger log;
    private AlgorithmExecutionResult parentAlgorithmResult;


    public AlgorithmExecutor(List<SegmentSoul> shape, Node startNode, ConditionManager conditionManager, int graphKey, AlgorithmExecutionResult result){
        this.shape = shape;
        this.startNode = startNode;
        this.conditionManager = conditionManager;
        this.graphKey = graphKey;
        this.parentAlgorithmResult = result;
        this.log = Logger.getLogger(this.toString() + this.hashCode());
    }

    @Override
    public void run() {
        //todo initialize new call for next segment
        ExecutorService executorService = ComputationDispatcher.executorService;
        Map<Node,List<Segment>> foundSegments = new HashMap<>();
        Graph graph = ComputationDispatcher.getGraph(graphKey);

        if(shape.size() == 0){
            log.info("Null will be thrown");
        }


        AlgorithmExecutionResult currentAlgorithmResult = new AlgorithmExecutionResult(startNode);

        SegmentSoul segmentToMap = shape.remove(0);
        List<Segment> potentialSegments = graph.getSegmentsForNode(startNode);
        potentialSegments.forEach(segment -> {
            EndNodesPredictor endNodesPredictor = new EndNodesPredictor(graph, conditionManager);
            Map<Node, List<Segment>> potentialNodes = endNodesPredictor.getNodes(startNode,segment,segmentToMap, segmentToMap.getVector1());

            if(!potentialNodes.isEmpty()) {
                potentialNodes.entrySet().forEach(entry -> {
                    if (!foundSegments.containsKey(entry.getKey()) || foundSegments.containsKey(entry.getKey()) && foundSegments.get(entry.getKey()).size() > entry.getValue().size())
                        foundSegments.put(entry.getKey(), entry.getValue());
                });

                if (!shape.isEmpty())
                    potentialNodes.keySet().forEach(n -> executorService.submit(new AlgorithmExecutor(new LinkedList<>(shape), n, conditionManager, graphKey, currentAlgorithmResult)));
            }
        });

        if(!foundSegments.isEmpty()){
            currentAlgorithmResult.setPathsToEndNodes(foundSegments);
            parentAlgorithmResult.addResultForNode(startNode, currentAlgorithmResult);
        }
    }
}
