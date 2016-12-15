package computation.algorithm;

import computation.ComputationDispatcher;
import computation.algorithm.conditions.Condition;
import computation.algorithm.conditions.ConditionManager;
import computation.algorithm.conditions.LengthCondition;
import computation.graphElements.Graph;
import computation.graphElements.LatLon;
import computation.graphElements.Node;
import computation.graphElements.segments.Segment;
import computation.utils.PositionApproximator;
import computation.utils.ShapeStateChecker;

import java.util.*;
import java.util.concurrent.*;

public class ShapeFinderManager {

    private Graph graph;
    private int simplifyingIterations;
    private LengthCondition lengthCondition;


    //todo remove graph after search
    public ShapeFinderManager(Graph graph, int simplifyingIterations) {
        this.graph = graph;
        ComputationDispatcher.addGraph(graph);
        this.simplifyingIterations = simplifyingIterations;
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
                    List<Segment> result = finder.findShapeForNode(n, shape, conditionManager);
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

    public List<Segment> findShapeConcurrent(List<Segment> shape, Node startNode, ConditionManager conditionManager, Double startPointRange) {
        Optional<Condition> foundCondition =  conditionManager.getBaseConditions().stream()
                .filter(c -> c instanceof LengthCondition)
                .findAny();
        Double minSearchEpsilon = 0.0;
        List<Future<List<Segment>>> callsList = new LinkedList<>();

        if(foundCondition.isPresent()){
            this.lengthCondition = (LengthCondition) foundCondition.get();
        }else {
            return new LinkedList<>();
        }



            if (ShapeStateChecker.isClosedShape(shape)) {
                while (minSearchEpsilon < startPointRange) {
                    Double tempMaxSearch = minSearchEpsilon + 0.005;
                    graph.getNodesWithinRadius(startNode.getLongitude(), startNode.getLatitude(), tempMaxSearch, minSearchEpsilon)
                            .forEach(startN -> {
                                for (int i = 0; i < shape.size(); i++) {
                                    callsList.add(ComputationDispatcher.executorService.submit(new AlgorithmExecutor(shape, startN, conditionManager, graph.hashCode())));
                                    Segment tmp = shape.remove(0);
                                    shape.add(tmp);
                                }
                            });
                    minSearchEpsilon = tempMaxSearch;
                }
            } else {
                while (minSearchEpsilon < startPointRange) {
                    Double tempMaxSearch = minSearchEpsilon + 0.005;
                    graph.getNodesWithinRadius(startNode.getLongitude(), startNode.getLatitude(), tempMaxSearch, minSearchEpsilon )
                            .forEach(startN -> {
                                callsList.add(ComputationDispatcher.executorService.submit(new AlgorithmExecutor(shape, startN, conditionManager, graph.hashCode())));
                                Collections.reverse(shape);
                                callsList.add(ComputationDispatcher.executorService.submit(new AlgorithmExecutor(shape, startN, conditionManager, graph.hashCode())));
                            });
                    minSearchEpsilon = tempMaxSearch;
                }
            }

            List<Segment> result;
            while(true){
                for(Future<List<Segment>> future: callsList){
                    if(future.isDone()){
                        try {
                            result = future.get();
                            if(!result.isEmpty()) {
                                callsList.forEach(f -> f.cancel(true));
                                return result;
                            }
                            else
                                callsList.remove(future);
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
    }
 }
