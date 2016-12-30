package computation.algorithm;

import computation.algorithm.conditions.ConditionManager;
import computation.algorithm.conditions.ConditionsResult;
import computation.graphElements.Graph;
import computation.graphElements.Node;
import computation.graphElements.Vector;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentSoul;

import java.util.*;

public class SegmentFinder {

    private final Graph graph;
    private Map<Node, List<Segment>> endNodes;
    private List<Segment> onMapSegments;
    private final ConditionManager conditionManager;
    private SegmentSoul shapeSegment;
    private Segment firstSegment;
    private Vector shapeVector;

    public SegmentFinder(Graph g, ConditionManager conditionManager){
        this.graph = g;
        this.conditionManager = new ConditionManager(conditionManager);
        this.endNodes = new HashMap<>();
        this.onMapSegments = new LinkedList<>();
    }

    public Map<Node, List<Segment>> getNodes(Node startNode, Segment firstSegment, SegmentSoul shapeSegment, Vector shapeVector) {
        this.shapeSegment = shapeSegment;
        this.firstSegment = firstSegment;
        this.shapeVector = shapeVector;
        onMapSegments.add(firstSegment);
        executeSearch(firstSegment.getNeighbour(startNode), null);
        return endNodes;
    }

    private boolean executeSearch(Node startNode, Segment previouslyAdded){
        if(shapeSegment.getLengthToFind() < -shapeSegment.getLength()*0.3){
            return false;
        }

        List<Segment> possibleSegments = graph.getSegmentsForNode(startNode);
        possibleSegments = sortByClosestDirection(possibleSegments, startNode);
        for(Segment segment: possibleSegments){
            if(previouslyAdded == null || segment.compareTo(previouslyAdded) != 0) {
                ConditionsResult conditionsResult = conditionManager.checkConditions(shapeSegment, segment,shapeVector ,segment.getVectorFromNode(startNode) );
                if (conditionsResult.areMet()) {
                    onMapSegments.add(segment);
                    shapeSegment.changeLengthToFind(-segment.getLength());
                    if(shapeSegment.getLengthToFind() < shapeSegment.getLength() * 0.3){
                        if(!endNodes.containsKey(segment.getNeighbour(startNode)) || endNodes.containsKey(segment.getNeighbour(startNode)) && endNodes.get(segment.getNeighbour(startNode)).size() > onMapSegments.size())
                            endNodes.put(segment.getNeighbour(startNode), new LinkedList<>(onMapSegments));
                    }
                    if (executeSearch(segment.getNeighbour(startNode), segment)) {
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
        Map<Double, Segment> segmentMap = new HashMap<>();
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
