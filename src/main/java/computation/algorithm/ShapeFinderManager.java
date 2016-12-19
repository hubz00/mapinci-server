package computation.algorithm;

import computation.ComputationDispatcher;
import computation.algorithm.conditions.ConditionManager;
import computation.graphElements.Graph;
import computation.graphElements.Node;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentFactory;
import computation.graphElements.segments.SegmentSoul;
import computation.utils.ShapeStateChecker;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class ShapeFinderManager {

    private Graph graph;
    private int simplifyingIterations;
    private List<SegmentSoul> shape;
    private Logger log = Logger.getLogger("ShapeFinderManager");
    private Double overallLength;


    //todo remove graph after search
    public ShapeFinderManager(Graph graph, int simplifyingIterations, Double overallLength) {
        this.graph = graph;
        ComputationDispatcher.addGraph(graph);
        this.simplifyingIterations = simplifyingIterations;
        this.overallLength = overallLength;
    }


    /**
     * Initiates searching for given shape from coordinates given in startNode
     * maxSearchEpsilon given in degrees
     * recommended value between: 0.005 - 0.05
     * shape found on map needs to meet conditions form conditionManager
     *
     * @param shape
     * @param startNode
     * @param conditionManager
     * @param startPointRange
     * @return list of segments from the map in asked shape
     */

    public List<Segment> findShapeOneThread(List<Segment> shape, Node startNode, ConditionManager conditionManager, Double startPointRange){
        return findShapeOneThread(shape, startNode, conditionManager, startPointRange, 0);
    }

    private List<Segment> findShapeOneThread(List<Segment> shape, Node startNode, ConditionManager conditionManager, Double startPointRange, int iteration) {
        ShapeFinder finder = new ShapeFinder(graph);
        Double minSearchEpsilon = 0.0;
        if(iteration > simplifyingIterations)
            return new LinkedList<>();

        while (minSearchEpsilon < startPointRange) {
            Double tempMaxSearch = minSearchEpsilon + 0.005;
            List <Node> nodes = graph.getNodesWithinRadius(startNode.getLongitude(), startNode.getLatitude(), tempMaxSearch, minSearchEpsilon);
            for(Node n: nodes){
                try {
                    List<Segment> result = finder.findShapeForNode(n, shape, conditionManager, overallLength);
                    if (!result.isEmpty())
                        return result;
                } catch (StackOverflowError e){
                    //taking another node
                }
                conditionManager.reset();
            }
            minSearchEpsilon = tempMaxSearch;
        }

        conditionManager.simplifyConditions();

        return findShapeOneThread(shape,startNode,conditionManager,startPointRange, ++iteration);
    }

    public List<List<Segment>> findShapeConcurrent(List<Segment> shapeToFind, Node startNode, ConditionManager conditionManager, Double startPointRange) {
        this.shape = new LinkedList<>();
        Double minSearchEpsilon = 0.0;
        Set<Future<List<List<Segment>>>> futuresSet = Collections.synchronizedSet(new HashSet<>());

        migrateShapeToInterfaceShape(shapeToFind);
        while (minSearchEpsilon < startPointRange) {
            Double tempMaxSearch = minSearchEpsilon + 0.005;
            if (ShapeStateChecker.isClosedShape(shapeToFind)) {
                List<Node> nodesWithinRadius = graph.getNodesWithinRadius(startNode.getLongitude(), startNode.getLatitude(), tempMaxSearch, minSearchEpsilon);
                log.info(String.format("nodes in radius on map: %s", nodesWithinRadius.size()));
                nodesWithinRadius.forEach(startN -> {
                    for (int i = 0; i < shape.size(); i++) {
                        futuresSet.add(ComputationDispatcher.executorService.submit(new AlgorithmExecutor(new LinkedList<>(shape), startN, new ConditionManager(conditionManager), graph.hashCode(), 0)));
                        SegmentSoul tmp = shape.remove(0);
                        shape.add(tmp);
                    }
                });
                minSearchEpsilon = tempMaxSearch;
            } else {
                graph.getNodesWithinRadius(startNode.getLongitude(), startNode.getLatitude(), tempMaxSearch, minSearchEpsilon)
                        .forEach(startN -> {
                            futuresSet.add(ComputationDispatcher.executorService.submit(new AlgorithmExecutor(new LinkedList<>(shape), startN, new ConditionManager(conditionManager), graph.hashCode(), 0)));
                            Collections.reverse(shape);
                            futuresSet.add(ComputationDispatcher.executorService.submit(new AlgorithmExecutor(new LinkedList<>(shape), startN, new ConditionManager(conditionManager), graph.hashCode(), 0)));
                        });
                minSearchEpsilon = tempMaxSearch;
            }
        }

        List<List<Segment>> result;
        while(!futuresSet.isEmpty()){
            Iterator<Future<List<List<Segment>>>> i = futuresSet.iterator();
            while(i.hasNext()){
                Future<List<List<Segment>>> future = i.next();
                if(future.isDone()){
                    try {
                        result = future.get();
                        if(!result.isEmpty()) {
                            futuresSet.forEach(f -> f.cancel(true));
                            return result;
                        }
                        else
                            i.remove();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        log.info("Empty set :c");
        return new LinkedList<>();
    }

    private void migrateShapeToInterfaceShape(List<Segment> preShape) {
        SegmentFactory sf = new SegmentFactory();
        preShape.forEach(segment -> shape.add(sf.newSegment(segment.getVector1(), segment.getVector2(), segment.getPercentLength(), overallLength)));
    }
}


