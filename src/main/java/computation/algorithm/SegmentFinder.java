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
        if(executeSearch(startNode,endNode, null))
            return onMapSegments;

        return new LinkedList<>();
    }

    public boolean executeSearch(Node startNode, Node endNode, Segment previouslyAdded){
        if(startNode.compareTo(endNode) == 0){
            return true;
        }
        if(shapeSegment.getLengthToFind() < -shapeSegment.getLength()*0.4){
            return false;
        }
        List<Segment> possibleSegments = graph.getSegmentsForNode(startNode);
        for(Segment segment: possibleSegments){
            if(previouslyAdded == null || segment.compareTo(previouslyAdded) != 0) {
                log.info(String.format("\t\t\t[%s --- %s]\n\t\t\t\tChecked Segment: [%s]", startNode, endNode, segment));
                ConditionsResult conditionsResult = conditionManager.checkConditions(shapeSegment, segment);
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
}
