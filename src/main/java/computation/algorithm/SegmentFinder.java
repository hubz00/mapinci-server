package computation.algorithm;


import computation.algorithm.conditions.ConditionManager;
import computation.algorithm.conditions.ConditionsResult;
import computation.graphElements.Graph;
import computation.graphElements.Node;
import computation.graphElements.segments.Segment;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class SegmentFinder {

    private final Graph graph;
    private List<Segment> onMapSegments;
    private Logger log = Logger.getLogger("Segment Finder");
    private final ConditionManager conditionManager;
    private Segment shapeSegment;

    public SegmentFinder(Graph g, ConditionManager conditionManager){
        this.graph = g;
        this.conditionManager = new ConditionManager(conditionManager);
        this.onMapSegments = new LinkedList<>();
    }

    public List<Segment> findSegment(Node startNode, Node endNode, Segment shapeSegment) {
        this.shapeSegment = shapeSegment;

        if(executeSearch(startNode,endNode))
            return onMapSegments;

        return new LinkedList<>();
    }

    public boolean executeSearch(Node startNode, Node endNode){
        if(startNode.compareTo(endNode) == 0){
            return true;
        }
        List<Segment> possibleSegments = graph.getSegmentsForNode(startNode);

        for(Segment segment: possibleSegments){
            ConditionsResult conditionsResult = conditionManager.checkConditions(shapeSegment, segment, false);
            if(conditionsResult.areMet()){
                onMapSegments.add(segment);
                if(executeSearch(segment.getNeighbour(startNode), endNode)){
                    return true;
                }
                conditionManager.revertLastCheck();
                onMapSegments.remove(segment);
            }
        }
        return false;
    }
}
