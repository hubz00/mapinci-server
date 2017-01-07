package computation.algorithm;

import computation.ComputationDispatcher;
import computation.algorithm.conditions.ConditionManager;
import computation.graphElements.Graph;
import computation.graphElements.Node;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentFactory;
import computation.graphElements.segments.SegmentSoul;
import computation.utils.ShapeStateChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ShapeFinderManager {

    private Graph graph;
    private int simplifyingIterations;
    private List<SegmentSoul> shape;
    private Logger logger = LoggerFactory.getLogger(ShapeFinderManager.class);
    private Double overallLength;


    public ShapeFinderManager(Graph graph, int simplifyingIterations, Double overallLength) {
        this.graph = graph;
        ComputationDispatcher.addGraph(graph);
        this.simplifyingIterations = simplifyingIterations;
        this.overallLength = overallLength;
    }

    public List<List<Segment>> findShapeConcurrent(List<Segment> shapeToFind, Node startNode, ConditionManager conditionManager, Double startPointRange) {
        int simplifyIndex = 0;
        List<List<Segment>> result = new LinkedList<>();

        while(simplifyIndex <= simplifyingIterations) {
            this.shape = new LinkedList<>();
            result = new LinkedList<>();
            Double minSearchEpsilon = 0.0;
            migrateShapeToInterfaceShape(shapeToFind);
            ComputationDispatcher.removeResults();

            while (minSearchEpsilon <= startPointRange) {
                Double maxSearchEpsilon = minSearchEpsilon + 0.0005;
                if (ShapeStateChecker.isClosedShape(shapeToFind)) {
                    List<Node> nodesWithinRadius = graph.getNodesWithinRadius(startNode.getLongitude(), startNode.getLatitude(), maxSearchEpsilon, minSearchEpsilon);
                    nodesWithinRadius.forEach(startN -> {
                        for (int i = 0; i < shape.size(); i++) {
                            ComputationDispatcher.addFuture(graph.hashCode(), ComputationDispatcher.executorService.submit(new AlgorithmInitExecutor(new LinkedList<>(shape), startN, new ConditionManager(conditionManager), graph.hashCode())));
                            SegmentSoul tmp = shape.remove(0);
                            shape.add(tmp);
                        }

                    });

                } else {
                    graph.getNodesWithinRadius(startNode.getLongitude(), startNode.getLatitude(), maxSearchEpsilon, minSearchEpsilon)
                            .forEach(startN -> {
                                ComputationDispatcher.addFuture(graph.hashCode(), ComputationDispatcher.executorService.submit(new AlgorithmInitExecutor(new LinkedList<>(shape), startN, new ConditionManager(conditionManager), graph.hashCode())));
                                Collections.reverse(shape);
                                ComputationDispatcher.addFuture(graph.hashCode(), ComputationDispatcher.executorService.submit(new AlgorithmInitExecutor(new LinkedList<>(shape), startN, new ConditionManager(conditionManager), graph.hashCode())));
                            });
                }
                minSearchEpsilon = maxSearchEpsilon;
            }

                while (!ComputationDispatcher.allRunnableFinished(graph.hashCode())) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    result.addAll(checkIfFinished());
                }
                if (result.isEmpty()) {
                    conditionManager.simplifyConditions();
                    simplifyIndex++;
                } else {
                    simplifyIndex = simplifyingIterations + 1;
                }
        }

        ComputationDispatcher.removeFutures(graph.hashCode());
        ComputationDispatcher.removeGraph(graph.hashCode());
        ComputationDispatcher.removeResults();
        logger.info(String.format("Found paths: %s", result.size()));
        if(!result.isEmpty()){
            List<List<Segment>> tmp = ifShapeClosedFilterForOne(shapeToFind, result);
            if(tmp.isEmpty())
                return result;
            else
                return tmp;
        }
        return result;
    }

    private List<List<Segment>> ifShapeClosedFilterForOne(List<Segment> shapeToFind, List<List<Segment>> preResult) {
        if(ShapeStateChecker.isClosedShape(shapeToFind)){
            return preResult.parallelStream()
                    .filter(list -> {
                        if(list.size() > 2){
                            Node first;
                            Node last;
                            if(list.get(1).contains(list.get(0).getNode1()))
                                first = list.get(0).getNode2();
                            else
                                first = list.get(0).getNode1();

                            if(list.get(list.size()-1).contains(list.get(list.size()-2).getNode1()))
                                last = list.get(list.size()-1).getNode2();
                            else
                                last = list.get(list.size()-1).getNode1();

                            return first.compareTo(last) == 0;
                        }
                        else
                            return false;
                    }).collect(Collectors.toList());
        }
        return preResult;
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

    private List<List<Segment>> checkIfFinished(){
        Map<Node, List<AlgorithmExecutionResult>> map = ComputationDispatcher.getResultsStartingFromNode();
        List<List<Segment>> result = new LinkedList<>();
        try {
                for (List<AlgorithmExecutionResult> startNodeList : map.values()) {
                        for (AlgorithmExecutionResult firstNodeResult : startNodeList) {
                            if(firstNodeResult.isFinished()) {
                                List<List<Segment>> tmp = checkNthSegment(1, firstNodeResult);
                                if (!tmp.isEmpty())
                                    result.addAll(tmp);
                            }
                        }

                }
                return result;
        } catch(NullPointerException e){
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    private List<List<Segment>> checkNthSegment(int depth, AlgorithmExecutionResult algoResult){
        if(algoResult.getPathsToEndNodes()!= null && !algoResult.getPathsToEndNodes().isEmpty()){
            if(depth == shape.size()){
                List<List<Segment>> result = new LinkedList<>();
                for (List<Segment> tmpList : algoResult.getPathsToEndNodes().values()){
                    if(!tmpList.isEmpty()) {
                        result.add(tmpList);
                    }
                }
                return result;
            }
            int newDepth = depth + 1;

            List<List<Segment>> result = new LinkedList<>();

            for(Map.Entry<Node, List<AlgorithmExecutionResult>> currentAlgoResult: algoResult.getResultsForNextSegments().entrySet()){
                for(AlgorithmExecutionResult algorithmExecutionResult: currentAlgoResult.getValue()) {
                    if (algorithmExecutionResult.isFinished()) {
                        List<List<Segment>> tmpResults = checkNthSegment(newDepth, algorithmExecutionResult);
                        if (tmpResults != null && !tmpResults.isEmpty()) {
                            List<Segment> matchedCurrentRoute = algoResult.getPathforEndNode(currentAlgoResult.getKey());
                            tmpResults.forEach(list -> {
                                List<Segment> tmp = new LinkedList<>(matchedCurrentRoute);
                                tmp.addAll(list);
                                result.add(tmp);
                            });
                        }
                    }
                }
            }
            return result;
        }
        return new LinkedList<>();
    }}

