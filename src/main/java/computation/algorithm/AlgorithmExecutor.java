package computation.algorithm;

import computation.ComputationDispatcher;
import computation.algorithm.conditions.ConditionManager;
import computation.algorithm.conditions.LengthCondition;
import computation.graphElements.Graph;
import computation.graphElements.LatLon;
import computation.graphElements.Node;
import computation.graphElements.NodeFactory;
import computation.graphElements.segments.Segment;
import computation.utils.PositionApproximator;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class AlgorithmExecutor implements Callable<List<List<Segment>>>{

    private List<Segment> shape;
    private Node startNode;
    private ConditionManager conditionManager;
    private int graphKey;

    public AlgorithmExecutor(List<Segment> shape, Node startNode, ConditionManager conditionManager, int graphKey){
        this.shape = shape;
        this.startNode = startNode;
        this.conditionManager = conditionManager;
        this.graphKey = graphKey;
    }

    @Override
    public List<List<Segment>> call() throws Exception {
        //todo initialize new call for next segment
        List<List<Segment>> potentialPaths = new LinkedList<>();
        ExecutorService executorService = ComputationDispatcher.executorService;
        Map<Node,Future<List<List<Segment>>>> futures = new HashMap<>();
        Map<Node,List<Segment>> foundSegments = new HashMap<>();
        Graph graph = ComputationDispatcher.getGraph(graphKey);
        Node firstNode = findFirstNode();
        LengthCondition lengthCondition = (LengthCondition) conditionManager.getBaseConditions()
                .stream()
                .filter(c -> c instanceof LengthCondition)
                .findAny()
                .get();

        Segment segmentToMap = shape.remove(0);
        Double ratio = segmentToMap.getPercentLength() * lengthCondition.getOverallLength() / segmentToMap.getVector1().getLength();
        Double offsetX = segmentToMap.getVector1().getX() * ratio;
        Double offsetY = segmentToMap.getVector1().getY() * ratio;
        LatLon desiredCoordinates = new PositionApproximator().offset(firstNode, offsetX, offsetY);

        Node desiredNode = new NodeFactory().newNode(desiredCoordinates.getLon(), desiredCoordinates.getLat());

        Double startPointRange = segmentToMap.getPercentLength() * lengthCondition.getOverallLength() * lengthCondition.getEpsilon();
        if(startPointRange < 0.05)
            startPointRange = 0.05;

        List<Node> potentialNodes = graph.getNodesWithinRadius(desiredNode.getLongitude(), desiredNode.getLatitude(), startPointRange, 0.0);
        potentialNodes.forEach(n -> futures.put(n,executorService.submit(new AlgorithmExecutor(new LinkedList<>(shape), n,conditionManager, graphKey))));

        //todo find way for this segment

        SegmentFinder segmentFinder = new SegmentFinder(graph,conditionManager);
        for(Node endNode: potentialNodes){
            List<Segment> result = segmentFinder.findSegment(startNode, endNode, segmentToMap);
            if(result.isEmpty()){
                futures.get(endNode).cancel(true);
                potentialNodes.remove(endNode);
            }
            else {
                foundSegments.put(endNode,result);
            }
        }

        //todo add returning a class not list -> what if multiple shapes found
        while (!futures.isEmpty()){
            for(Map.Entry<Node,Future<List<List<Segment>>>> futureEntry: futures.entrySet()){
                if(futureEntry.getValue().isDone()){
                    if(futureEntry.getValue().get().isEmpty()){
                        futures.remove(futureEntry.getKey());
                        potentialNodes.remove(futureEntry.getKey());
                    }
                    else{
                        List<Segment> result = foundSegments.get(futureEntry.getKey());
                        for(List<Segment> list: futureEntry.getValue().get()){
                            List<Segment> tmp = new LinkedList<>(result);
                            tmp.addAll(list);
                            potentialPaths.add(tmp);
                        }
                        futures.remove(futureEntry.getKey());
                    }
                }
                else if(futureEntry.getValue().isCancelled()) {
                    futures.remove(futureEntry.getKey());
                    potentialNodes.remove(futureEntry.getKey());
                }
            }
            Thread.sleep(1000);
        }
        //todo cancel or wait for the result
        return potentialPaths;
    }

    private Node findFirstNode(){
        Segment s1 = shape.get(0);
        Segment s2 = shape.get(1);

        if(s1.getNode1().compareTo(s2.getNode1()) == 0){
            return s1.getNode2();
        }
        return s1.getNode1();
    }
}
