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
import java.util.logging.Logger;

public class ShapeFinderManager {

    private Graph graph;
    private int simplifyingIterations;
    private List<SegmentSoul> shape;
    private Logger log = Logger.getLogger("ShapeFinderManager");
    private Double overallLength;
    private boolean once;


    //todo remove graph after search
    public ShapeFinderManager(Graph graph, int simplifyingIterations, Double overallLength) {
        this.graph = graph;
        ComputationDispatcher.addGraph(graph);
        this.simplifyingIterations = simplifyingIterations;
        this.overallLength = overallLength;
        this.once = true;
    }

    public List<List<Segment>> findShapeConcurrent(List<Segment> shapeToFind, Node startNode, ConditionManager conditionManager, Double startPointRange) {
        this.shape = new LinkedList<>();

        Double minSearchEpsilon = 0.0;

        migrateShapeToInterfaceShape(shapeToFind);
        while (minSearchEpsilon <= startPointRange) {
            //todo change to add something
            Double maxSearchEpsilon = minSearchEpsilon + 0.0005;
            if (ShapeStateChecker.isClosedShape(shapeToFind)) {
                List<Node> nodesWithinRadius = graph.getNodesWithinRadius(startNode.getLongitude(), startNode.getLatitude(), maxSearchEpsilon, minSearchEpsilon);
                nodesWithinRadius.forEach(startN -> {
                    //todo this if only for testing to search from one specific node #debugging
                    if(startN.getLatitude() == 50.0669934 && startN.getLongitude() == 19.9203659 && once) {
                        // todo return with loop after testing
                        // for (int i = 0; i < shape.size(); i++) {
                        log.info(shape.get(1).toString());
                        updateOnce();
//                            futuresSet.add(ComputationDispatcher.executorService.submit(new AlgorithmExecutor(new LinkedList<>(shape), startN, new ConditionManager(conditionManager), graph.hashCode(), 0)));
                        SegmentSoul tmp = shape.remove(0);
                        shape.add(tmp);
                        //todo remove after test uncomment upper
                        ComputationDispatcher.executorService.submit(new AlgorithmInitExecutor(new LinkedList<>(shape), startN, new ConditionManager(conditionManager), graph.hashCode()));
//                        }
                    }
                });

            } else{
                graph.getNodesWithinRadius(startNode.getLongitude(), startNode.getLatitude(), maxSearchEpsilon, minSearchEpsilon)
                        .forEach(startN -> {
//                            futuresSet.add(ComputationDispatcher.executorService.submit(new AlgorithmExecutor(new LinkedList<>(shape), startN, new ConditionManager(conditionManager), graph.hashCode(), 0)));
                            Collections.reverse(shape);
//                            futuresSet.add(ComputationDispatcher.executorService.submit(new AlgorithmExecutor(new LinkedList<>(shape), startN, new ConditionManager(conditionManager), graph.hashCode(), 0)));
                        });

            }
            minSearchEpsilon = maxSearchEpsilon;
        }

        List<List<Segment>> result = new LinkedList<>();

        while(result.isEmpty()){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = checkIfFinished();
        }
        ComputationDispatcher.removeGraph(graph.hashCode());
        ComputationDispatcher.removeResults();
        System.out.println("----------------------------------------------\n\nFound Paths: " + result.size() + "\n\n-------------------------------------------------");
        return result;
    }

    private void migrateShapeToInterfaceShape(List<Segment> preShape) {
        SegmentFactory sf = new SegmentFactory();
        Segment s1 = preShape.get(0);
        Segment s2 = preShape.get(1);
        Node first;
        if(s2.contains(s1.getNode1()))
            first = s1.getNode2();
        else
            first = s1.getNode1();

        for(Segment segment: preShape){
            Node neighbour = segment.getNeighbour(first);
            shape.add(sf.newSegment(segment.getVectorFromNode(first), segment.getVectorFromNode(neighbour), segment.getPercentLength(), overallLength));
            first = neighbour;
        }
    }

    private void updateOnce(){
        once = false;
    }

    private List<List<Segment>> checkIfFinished(){
        Map<Node, List<AlgorithmExecutionResult>> map = ComputationDispatcher.getResultsStartingFromNode();

        for(List<AlgorithmExecutionResult> startNodeList: map.values()){
            for(AlgorithmExecutionResult firstNodeResult: startNodeList){
                List<List<Segment>> tmp = checkNthSegment(1,firstNodeResult);
                if(!tmp.isEmpty())
                    return tmp;
            }
        }
        return new LinkedList<>();
    }

    private List<List<Segment>> checkNthSegment(int depth, AlgorithmExecutionResult algoResult){
        if(algoResult.getPathsToEndNodes()!= null && !algoResult.getPathsToEndNodes().isEmpty()){
            if(depth == shape.size()){
                List<List<Segment>> result = new LinkedList<>();
                for (List<Segment> tmpList : algoResult.getPathsToEndNodes().values()){
                    result.add(tmpList);
                }
                return result;
            }

            List<List<Segment>> result = new LinkedList<>();

            for(Map.Entry<Node, AlgorithmExecutionResult> currentAlgoResult: algoResult.getResultsForNextSegments().entrySet()){
                List<List<Segment>> tmpResults = checkNthSegment(depth+1, currentAlgoResult.getValue());
                if(tmpResults != null && !tmpResults.isEmpty()){
                    List<Segment> matchedCurrentRoute = algoResult.getPathforEndNode(currentAlgoResult.getKey());
                    tmpResults.forEach(list -> {
                        List<Segment> tmp = new LinkedList<>(matchedCurrentRoute);
                        tmp.addAll(list);
                        result.add(tmp);
                    });
                }
            }
            return result;
        }
        return new LinkedList<>();
    }}

