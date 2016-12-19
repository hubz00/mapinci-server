package computation.algorithm;


import computation.algorithm.conditions.ConditionManager;
import computation.algorithm.conditions.ConditionsResult;
import computation.graphElements.Graph;
import computation.graphElements.Node;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentSoul;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class SegmentFinder {

    private final Graph graph;
    private List<Segment> onMapSegments;
    private Logger log = Logger.getLogger("Segment Finder");
    private final ConditionManager conditionManager;
    private SegmentSoul shapeSegment;

    public SegmentFinder(Graph g, ConditionManager conditionManager){
        this.graph = g;
        this.conditionManager = new ConditionManager(conditionManager);
        this.onMapSegments = new LinkedList<>();
    }

    public List<Segment> findSegment(Node startNode, Node endNode, SegmentSoul shapeSegment) {
        this.shapeSegment = shapeSegment;
//        log.info(String.format("Looking for segment: [%s]\n\t between strartNode: [%s] endNode: [%s]", shapeSegment,startNode,endNode));
        if(executeSearch(startNode,endNode, null))
            return onMapSegments;

        return new LinkedList<>();
    }

    public boolean executeSearch(Node startNode, Node endNode, Segment previouslyAdded){
        if(startNode.compareTo(endNode) == 0){
            log.info("Found segment: " + shapeSegment);
            return true;
        }
        if(shapeSegment.getLengthToFind() < -shapeSegment.getLength()*0.4){
            return false;
        }
        List<Segment> possibleSegments = graph.getSegmentsForNode(startNode);
//        log.info(String.format("\t\t[New Call]\n\t\t\tStart node: %s\n\t\t\tEnd node: [X: %f\t%f]\n\t\t\tnumber of possible segments: %s", startNode,endNode.getLongitude(), endNode.getLatitude(), possibleSegments.size()));
        for(Segment segment: possibleSegments){
            if(previouslyAdded == null || segment.compareTo(previouslyAdded) != 0) {
//                log.info(String.format("\t\t\tChecking segment: %s with shape segment: %s", segment, shapeSegment));
                ConditionsResult conditionsResult = conditionManager.checkConditions(shapeSegment, segment);
                if (conditionsResult.areMet()) {
//                    log.info(String.format("\t\t\tAdding segment: %s, first node: %s",segment.getNeighbour(startNode), segment));
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
}
