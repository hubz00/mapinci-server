package map.graph.algorithm;

import map.graph.algorithm.conditions.ConditionManager;
import map.graph.graphElements.Graph;
import map.graph.graphElements.Node;
import map.graph.graphElements.segments.Segment;
import map.graph.graphElements.segments.SegmentFactory;
import map.graph.graphElements.segments.SegmentSoul;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShapeFinder {

    private final Graph graph;
    private List<Segment> preShape;
    private List<SegmentSoul> shape;
    private List<Segment> onMapSegments;
    private Double epsilon;
    private Logger log = Logger.getLogger("Shape Finder");
    private ConditionManager conditionManager;
    private ReferenceRotator referenceRotator;

    public ShapeFinder(Graph graph, List<Segment> shape, ConditionManager cm){
        this.preShape = shape;
        this.shape = new LinkedList<>();
        this.graph = graph;
        this.onMapSegments = new LinkedList<>();
        this.conditionManager = cm;
        this.referenceRotator = new ReferenceRotator();
    }

    public Graph findShape(Node startNode, Double epsilon, Double nodeSearchEpsilon){
        this.epsilon = epsilon;
        Graph result = new Graph();
        Node node = graph.getNodeByCoordinates(startNode.getLongitude(), startNode.getLatitude(), nodeSearchEpsilon);
        log.log(Level.ALL,"Starting node: " + node);

        if(isClosedShape()){
            log.info("Is a closed shape");
            for(int i = 0; i < shape.size(); i++){
                log.info(String.format("Taking next node to check: [Loop: %d]",i));
                if(initAlgorithm(node)){
                    result.setSegments(onMapSegments);
                    return result;
                }
                SegmentSoul tmp = shape.remove(0);
                shape.add(tmp);
            }
        }
        else {
            if(initAlgorithm(node)) {
                result.setSegments(onMapSegments);
                return result;
            }
            Collections.reverse(shape);
            if(initAlgorithm(node)){
                result.setSegments(onMapSegments);
                return result;
            }
        }
        log.info("Empty result graph");
        return result;
    }

    /**
     *  first cycle of searching for shape,
     *  adjust the shape coordinates to fit first segment
     *  and run recursion
     */
    private boolean initAlgorithm(Node startNode){
        List<Segment> possibleSegments = graph.getSegmentsForNode(startNode);

        for(Segment s: possibleSegments) {
            this.shape = referenceRotator.rotateShapeToFit(s, shape.get(0), shape);
            log.info(String.format("Received slopes: %f   &    %f",s.getSlope(),shape.get(0).getSlope()));
            if(conditionManager.checkConditions(shape.get(0),s)){
                onMapSegments.add(s);
                if(findNextSegment(s.getNeighbour(startNode),1)) return true;
            }

            onMapSegments = new LinkedList<>();
        }
        return false;
    }

    private boolean findNextSegment(Node startNode, int position){
        if(position == shape.size()) return true;

        List<Segment> possibleSegments = graph.getSegmentsForNode(startNode);
        SegmentSoul segmentToMap = shape.get(position);
        log.info(String.format("New call\n[Position: %s]\n[Start node: %s]\n[Segment to map: %s]",position, startNode, segmentToMap));

        for (Segment s: possibleSegments){
            log.info(String.format("[Checking Segment: %s]",s));
            if(((position > 0 && (s.compareTo(onMapSegments.get(position-1)) != 0)) || position == 0) && conditionManager.checkConditions(segmentToMap,s)){
                onMapSegments.add(s);
                log.info(String.format("[Adding new segment to result: %s]",s));
                if (findNextSegment(s.getNeighbour(startNode), position + 1))
                    return true;
                else
                    onMapSegments.remove(s);
            }
        }
        return false;
    }

    private boolean isClosedShape() {
        if(preShape.size() < 3) return false;
        Segment firstSegment = preShape.get(0);
        Segment lastSegment = preShape.get(preShape.size()-1);
        Segment secondSegment = preShape.get(1);
        Segment preLastSegment = preShape.get(preShape.size()-2);

        Node firstSegmentNode1 = firstSegment.getNode1();
        Node firstSegmentNode2 = firstSegment.getNode2();

        //after checking nodes in shape, create shape without nodes for easier manipulation
        migratePreShapeToShape();

        if(lastSegment.contains(firstSegmentNode1)
                && !preLastSegment.contains(firstSegmentNode1)
                && !secondSegment.contains(firstSegmentNode1)) return true;

        else if(lastSegment.contains(firstSegmentNode2)
                && !preLastSegment.contains(firstSegmentNode2)
                && !secondSegment.contains(firstSegmentNode2)) return true;

        return false;
    }

    private void migratePreShapeToShape() {
        SegmentFactory sf = new SegmentFactory();
        preShape.forEach(segment -> shape.add(sf.newFullSegment(segment.getVector1(), segment.getVector2())));
    }


    public Graph getGraph() {
        return graph;
    }


}
