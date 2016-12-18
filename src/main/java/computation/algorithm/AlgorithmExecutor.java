package computation.algorithm;

import computation.ComputationDispatcher;
import computation.algorithm.conditions.ConditionManager;
import computation.algorithm.conditions.LengthCondition;
import computation.graphElements.Graph;
import computation.graphElements.LatLon;
import computation.graphElements.Node;
import computation.graphElements.NodeFactory;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentSoul;
import computation.utils.PositionApproximator;
import computation.utils.ReferenceRotator;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class AlgorithmExecutor implements Callable<List<List<Segment>>>{

    private List<SegmentSoul> shape;
    private Node startNode;
    private ConditionManager conditionManager;
    private int graphKey;
    private Logger log;
    private int depthLevel;


    public AlgorithmExecutor(List<SegmentSoul> shape, Node startNode, ConditionManager conditionManager, int graphKey, int depthLevel){
        this.shape = shape;
        this.startNode = startNode;
        this.conditionManager = conditionManager;
        this.graphKey = graphKey;
        this.depthLevel = depthLevel;
        this.log = Logger.getLogger(this.toString() + this.hashCode());
    }

    @Override
    public List<List<Segment>> call() throws Exception {
        log.info(String.format("\t[%s] AlgorithmExecutor from node: %s",System.identityHashCode(this), startNode));
        //todo initialize new call for next segment
        ReferenceRotator referenceRotator = new ReferenceRotator();
        List<List<Segment>> potentialPaths = new LinkedList<>();
        ExecutorService executorService = ComputationDispatcher.executorService;
        Map<Node,Future<List<List<Segment>>>> futures = new HashMap<>();
        Map<Node,List<Segment>> foundSegments = new HashMap<>();
        Graph graph = ComputationDispatcher.getGraph(graphKey);

        if(shape.size() == 0){
            log.info("Null will be thrown");
        }



        //if first call - >  rotate shape to fit segment
        if(depthLevel == 0){
            List<Segment> initialSegmentsFromMap = graph.getSegmentsForNode(startNode);
            initialSegmentsFromMap.forEach(segment -> {
                log.info(String.format("\t\t[%s] First segment on map: %s",System.identityHashCode(this), shape.get(0)));
                this.shape = referenceRotator.rotateShapeToFit(segment, shape.get(0), shape);
                log.info(String.format("\t\t[%s] First segment rotated: %s",System.identityHashCode(this), shape.get(0)));
                List<Node> potentialNodes = obtainPotentialNodes(shape.get(0), graph);
                log.info(String.format("\t\t[%s] Potential nodes: %s",System.identityHashCode(this), potentialNodes.size()));

                SegmentFinder segmentFinder = new SegmentFinder(graph,conditionManager);
                Iterator<Node> i = potentialNodes.iterator();
                while(i.hasNext()){
                    Node endNode = i.next();
                    log.info(String.format("\t\t[%s] Checking endNode: %s",System.identityHashCode(this),endNode));
                    List<Segment> result = segmentFinder.findSegment(startNode, endNode, shape.get(0));
                    log.info(String.format("Path found: %s", result.toString()));
                    if(result.isEmpty()){
                        i.remove();
                    }
                    else {
                        foundSegments.put(endNode,result);
                    }
                }

                log.info(String.format("\t\t[%s] After checking paths, potential nodes: %s",System.identityHashCode(this),potentialNodes.size() ));
                if(!shape.subList(1, shape.size()).isEmpty())
                    potentialNodes.forEach(n -> futures.put(n,executorService.submit(new AlgorithmExecutor(new LinkedList<>(shape.subList(1, shape.size())), n,conditionManager, graphKey, depthLevel + 1))));
            });

        }
        else {
            SegmentSoul segmentToMap = shape.remove(0);
            List<Node> potentialNodes = obtainPotentialNodes(segmentToMap, graph);

            log.info(String.format("\t\t[%s] Potential nodes: %s",System.identityHashCode(this), potentialNodes.size()));
            //todo find way for this segment

            SegmentFinder segmentFinder = new SegmentFinder(graph, conditionManager);
            Iterator<Node> i = potentialNodes.iterator();
            while (i.hasNext()) {
                Node endNode = i.next();
                List<Segment> result = segmentFinder.findSegment(startNode, endNode, segmentToMap);
                if (result.isEmpty())
                    i.remove();
                else
                    foundSegments.put(endNode, result);
            }
            log.info(String.format("\t\t[%s] After checking paths, potential nodes: %s",System.identityHashCode(this), potentialNodes.size()));
            // running algo for potential nodes
            if(!shape.isEmpty())
                potentialNodes.forEach(n -> futures.put(n, executorService.submit(new AlgorithmExecutor(new LinkedList<>(shape), n, conditionManager, graphKey, depthLevel + 1))));
        }


        //todo add returning a class not list -> what if multiple shapes found
        while (!futures.entrySet().isEmpty()){
            Iterator<Map.Entry<Node,Future<List<List<Segment>>>>> iterator = futures.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<Node,Future<List<List<Segment>>>> futureEntry = iterator.next();
                if(futureEntry.getValue().isDone()){
                    if(futureEntry.getValue().get().isEmpty()){
                        iterator.remove();
                    }
                    else{
                        List<Segment> result = foundSegments.get(futureEntry.getKey());
                        for(List<Segment> list: futureEntry.getValue().get()){
                            List<Segment> tmp = new LinkedList<>(result);
                            tmp.addAll(list);
                            potentialPaths.add(tmp);
                        }
                        iterator.remove();
                    }
                }
                else if(futureEntry.getValue().isCancelled()) {
                    iterator.remove();
                }
            }
            Thread.sleep(1000);
        }
        //todo cancel or wait for the result
        return potentialPaths;
    }

    private List<Node> obtainPotentialNodes(SegmentSoul segmentToMap, Graph graph) {
        LengthCondition lengthCondition = (LengthCondition) conditionManager.getBaseConditions()
                .stream()
                .filter(c -> c instanceof LengthCondition)
                .findAny()
                .get();

        Double ratio = segmentToMap.getLength() / segmentToMap.getVector1().getLength();
        Double offsetX = segmentToMap.getVector1().getX() * ratio;
        Double offsetY = segmentToMap.getVector1().getY() * ratio;
        LatLon desiredCoordinates = new PositionApproximator().offset(startNode, offsetX, offsetY);

        Node desiredNode = new NodeFactory().newNode(desiredCoordinates.getLon(), desiredCoordinates.getLat());

        Double startPointRange = segmentToMap.getLength() * lengthCondition.getEpsilon();
        if(startPointRange < 0.05)
            startPointRange = 0.05;


        return graph.getNodesWithinRadius(desiredNode.getLongitude(), desiredNode.getLatitude(), startPointRange, 0.0);
    }

}
