package computation.algorithm;

import computation.ComputationDispatcher;
import computation.algorithm.conditions.ConditionManager;
import computation.algorithm.conditions.LengthCondition;
import computation.graphElements.*;
import computation.graphElements.Vector;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentFactory;
import computation.graphElements.segments.SegmentSoul;
import computation.utils.PositionApproximator;
import computation.utils.ReferenceRotator;
import sun.awt.image.ImageWatched;

import java.util.*;
import java.util.concurrent.Callable;
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
                log.info(String.format("\tChecking Segments from start node segment neighbour [%s]\n\t\tOriginal slope: [%s]", segment.getNeighbour(startNode), segment.getSlope()));
                shape.get(0).getVectors().forEach( shapeVector -> {
                    log.info(String.format("\t\tRotating shape vector: [%s]\n\t\t\tto map Vector: [%s]",shapeVector, segment.getVectorFromNode(startNode)));
                    this.shape = referenceRotator.rotateShapeToFit(shape, segment.getVectorFromNode(startNode), shapeVector );
                    List<Node> potentialNodes = obtainPotentialNodes(shape.get(0), graph);
                    potentialNodes.forEach( pn -> System.out.println(String.format("From node: %s\tto Node: %s\tslope: %s", startNode, pn, segment.getSlope())));
                    if(potentialNodes.stream().anyMatch(potNode -> potNode.getLatitude() == 50.0683923 && potNode.getLongitude() == 19.9208255 )) {
                        SegmentFinder segmentFinder = new SegmentFinder(graph, conditionManager);
                        //todo remove after tests
                        Node tmp = potentialNodes.get(potentialNodes.indexOf(graph.getNodeById(273461466L)));
                        potentialNodes = new LinkedList<Node>();
                        potentialNodes.add(tmp);
                        //
                        Iterator<Node> i = potentialNodes.iterator();
                        while (i.hasNext()) {
                            Node endNode = i.next();
                            this.shape = referenceRotator.rotateShapeToFit(shape, new Vector(startNode,endNode), shapeVector);
                            SegmentFactory sf = new SegmentFactory();
                            log.info(String.format("\t\tSearching for path between:\n\t\t\t[%s %s]",startNode, endNode));
                            List<Segment> result = segmentFinder.findSegment(startNode, endNode, sf.newSegment(shape.get(0)));
                            if (result.isEmpty()) {
                                i.remove();
                            } else {
                                log.info(String.format("Found path between:\n\t\t\t[%s %s]", startNode, endNode));
                                foundSegments.put(endNode, result);
                            }
                        }
                    }
                    //todo remove - just for testing
                    else{
                        potentialNodes = new LinkedList<Node>();
                    }
                    // ------------------------------
                    if(!shape.subList(1, shape.size()).isEmpty())
                        potentialNodes.forEach(n -> futures.put(n,executorService.submit(new AlgorithmExecutor(new LinkedList<>(shape.subList(1, shape.size())), n,conditionManager, graphKey, ++depthLevel))));
                });
            });
            shape.remove(0);
        }
        else {
            SegmentSoul segmentToMap = shape.remove(0);
            List<Node> potentialNodes = obtainPotentialNodes(segmentToMap, graph);

            //todo find way for this segment

            SegmentFinder segmentFinder = new SegmentFinder(graph, conditionManager);
            Iterator<Node> i = potentialNodes.iterator();
            while (i.hasNext()) {
                Node endNode = i.next();
                List<Segment> result = segmentFinder.findSegment(startNode, endNode, segmentToMap);
                if (result.isEmpty())
                    i.remove();
                else {
                    log.info(String.format("Found path between:\n\t\t\t[%s %s]", startNode, endNode));
                    foundSegments.put(endNode, result);
                }
            }
            if(!shape.isEmpty())
                potentialNodes.forEach(n -> futures.put(n, executorService.submit(new AlgorithmExecutor(new LinkedList<>(shape), n, conditionManager, graphKey, ++depthLevel))));
        }

        if(shape.isEmpty() && !foundSegments.isEmpty()){
            List<List<Segment>> result = new LinkedList<>();
            foundSegments.values().forEach(list -> result.add(new LinkedList<>(list)));
            return result;
        }else if(foundSegments.isEmpty()){
            futures.values().forEach(f -> f.cancel(true));
            return new LinkedList<>();
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
        log.info(String.format("\tReturning path: %s", potentialPaths));
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

        //divided by approx length of one coordinate point -> to make in range in points
        Double startPointRange = segmentToMap.getLength() * lengthCondition.getEpsilon()/111111;
        if(startPointRange > 0.005)
            startPointRange = 0.000005;

        return graph.getNodesWithinRadius(desiredNode.getLongitude(), desiredNode.getLatitude(), startPointRange, 0.0);
    }

}
