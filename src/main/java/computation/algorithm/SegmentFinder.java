package computation.algorithm;


import computation.algorithm.conditions.ConditionManager;
import computation.algorithm.conditions.ConditionsResult;
import computation.graphElements.Graph;
import computation.graphElements.Node;
import computation.graphElements.Vector;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentSoul;

import java.util.*;
import java.util.logging.Logger;

public class SegmentFinder {

    private final Graph graph;
    private List<Segment> onMapSegments;
    private Logger log = Logger.getLogger("Segment Finder");
    private final ConditionManager conditionManager;
    private SegmentSoul shapeSegment;
    private Vector shapeVector;

    public SegmentFinder(Graph g, ConditionManager conditionManager){
        this.graph = g;
        this.conditionManager = new ConditionManager(conditionManager);
        this.onMapSegments = new LinkedList<>();
    }

    public List<Segment> findSegment(Node startNode, Node endNode, SegmentSoul shapeSegment) {
        this.shapeSegment = shapeSegment;
        this.shapeVector = new Vector(startNode, endNode);
        if(executeSearch(startNode,endNode, null))
            return onMapSegments;

        return new LinkedList<>();
    }

    public boolean executeSearch(Node startNode, Node endNode, Segment previouslyAdded){
        if(startNode.compareTo(endNode) == 0){
            log.info(String.format("\t\t\tNode match: %s\n\t\t\t\tPath: %s", startNode, onMapSegments));
            return true;
        }
        if(shapeSegment.getLengthToFind() < -shapeSegment.getLength()*0.3){
            return false;
        }
        List<Segment> possibleSegments = graph.getSegmentsForNode(startNode);
        possibleSegments = sortByClosestDirection(possibleSegments, startNode);
        for(Segment segment: possibleSegments){
            if(previouslyAdded == null || segment.compareTo(previouslyAdded) != 0) {
                log.info(String.format("\t\t\t[%s --- %s]\n\t\t\t\tChecked Segment: [%s]", startNode, endNode, segment));
                ConditionsResult conditionsResult = conditionManager.checkConditions(shapeSegment, segment,shapeVector ,segment.getVectorFromNode(startNode) );
                if (conditionsResult.areMet()) {
                    onMapSegments.add(segment);
                    shapeSegment.changeLengthToFind(-segment.getLength());
                    if (executeSearch(segment.getNeighbour(startNode), endNode, segment)) {
                        return true;
                    }
                    shapeSegment.changeLengthToFind(segment.getLength());
                    onMapSegments.remove(segment);
                }
            }
        }
        return false;
    }

    private List<Segment> sortByClosestDirection(List<Segment> possibleSegments, Node startNode) {
        List<Double> angles = new LinkedList<>();
        Map<Double, Segment>  segmentMap = new HashMap<>();
        possibleSegments.forEach(segment ->{
            Double angle = shapeVector.getAngleBetween(segment.getVectorFromNode(startNode));
            angles.add(angle);
            segmentMap.put(angle, segment);
        });
        Collections.sort(angles);
        List<Segment> result = new LinkedList<>();
        angles.forEach(angle -> result.add(segmentMap.get(angle)));

        return result;
    }
}
