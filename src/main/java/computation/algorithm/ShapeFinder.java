package computation.algorithm;

import computation.algorithm.conditions.ConditionManager;
import computation.algorithm.conditions.ConditionsResult;
import computation.graphElements.Graph;
import computation.graphElements.Node;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentFactory;
import computation.graphElements.segments.SegmentSoul;
import computation.utils.ReferenceRotator;

import java.util.*;
import java.util.logging.Logger;

public class ShapeFinder {

    private final Graph graph;
    private List<Segment> preShape;
    private List<SegmentSoul> shape;
    private List<Segment> onMapSegments;
    private Logger log = Logger.getLogger("Shape Finder");
    private ConditionManager conditionManager;
    private ReferenceRotator referenceRotator;
    private SegmentSoul firstOriginalAngleSegment;

    public ShapeFinder(Graph graph){
        this.graph = graph;
        this.referenceRotator = new ReferenceRotator();
    }

    List<Segment> findShapeForNode(Node node, List<Segment> shape, ConditionManager conditionManager){
        this.shape = new LinkedList<>();
        this.preShape = shape;
        this.conditionManager = conditionManager;
        this.onMapSegments = new LinkedList<>();

        if(isClosedShape()){
            log.info("Is a closed shape");
            for(int i = 0; i < this.shape.size(); i++){
                log.info(String.format("Taking next node to check, loop: %d, left: %d", i, this.shape.size() - i));
                if(initAlgorithm(node)){
                    return onMapSegments;
                }
                SegmentSoul tmp = this.shape.remove(0);
                this.shape.add(tmp);
                this.onMapSegments = new LinkedList<>();

            }
        }
        else {
            if(initAlgorithm(node)) {
                return onMapSegments;
            }
            Collections.reverse(this.shape);
            if(initAlgorithm(node)){
                return onMapSegments;
            }
        }
        log.info("Empty result graph");
        return new LinkedList<>();
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
            log.info("New Shape angle --------------------------------------------");
            shape.forEach(seg -> log.info(seg.toString()));
            log.info("------------------------------------------------------------");
            ConditionsResult result = conditionManager.checkConditions(shape.get(0),s,true);
            if(result.areMet()){
                //todo add handling condition manager
                log.info(String.format("[Adding new segment to result: %s]", s));
                onMapSegments.add(s);
                if(result.isEnoughSpaceForAnotherSegment()) {
                    if (findNextSegment(s.getNeighbour(startNode), 0, false))
                        return true;
                } else {
                    if (findNextSegment(s.getNeighbour(startNode), 1, true))
                        return true;
                }
            }

            onMapSegments = new LinkedList<>();
        }
        return false;
    }

    private boolean findNextSegment(Node startNode, int position, boolean newSegment){
        if(position == shape.size()) return true;

        List<Segment> possibleSegments = graph.getSegmentsForNode(startNode);
        SegmentSoul segmentToMap = shape.get(position);
        log.info(String.format("New call\n\t[Position: %s]\n\t\t[Start node: %s]\n\t\t[Segment to map: %s]",position, startNode, segmentToMap));

        for (Segment s: possibleSegments){
            log.info(String.format("\t[Checking Segment: %s]\n\t\t[Comparing to recently added: %s]\n\t\t[Added list size: %s]",s, s.compareTo(onMapSegments.get(onMapSegments.size()-1)), onMapSegments.size()));
            if(((onMapSegments.size() == 0) || (s.compareTo(onMapSegments.get(onMapSegments.size()-1)) != 0))) {
                ConditionsResult conditionsResult = conditionManager.checkConditions(segmentToMap, s, newSegment);
                if (conditionsResult.areMet()) {
                    onMapSegments.add(s);
                    log.info(String.format("[Adding new segment to result: %s]", s));
                    if (conditionsResult.isEnoughSpaceForAnotherSegment())
                        if (findNextSegment(s.getNeighbour(startNode), position, false))
                            return true;
                        else
                            onMapSegments.remove(s);
                    else {
                        if(position == 0){
                            checkRotation();
                        }
                        if (findNextSegment(s.getNeighbour(startNode), position + 1, true))
                            return true;
                        else
                            onMapSegments.remove(s);
                    }
                }
            }
        }
        return false;
    }

    private void checkRotation() {

        SegmentSoul shapeSegment = shape.get(0);
        SegmentFactory sf = new SegmentFactory();

        log.info(String.format("\t\tCurrent slope: %f real Slope: %f", shapeSegment.getSlope(), firstOriginalAngleSegment.getSlope()));
        if(Math.abs(shapeSegment.getSlope() - firstOriginalAngleSegment.getSlope()) >= 0.1){
            this.shape = referenceRotator.rotateShapeToFit((Segment) firstOriginalAngleSegment,shapeSegment, shape);
            log.info("New Shape angle --------------------------------------------");
            shape.forEach(seg -> log.info(seg.toString()));
            log.info("------------------------------------------------------------");
        }

    }

    private boolean isClosedShape() {
        migratePreShapeToShape();
        firstOriginalAngleSegment = preShape.get(0);
        if(preShape.size() < 3) return false;
        Segment firstSegment = preShape.get(0);
        Segment lastSegment = preShape.get(preShape.size()-1);
        Segment secondSegment = preShape.get(1);
        Segment preLastSegment = preShape.get(preShape.size()-2);

        Node firstSegmentNode1 = firstSegment.getNode1();
        Node firstSegmentNode2 = firstSegment.getNode2();

        //after checking nodes in shape, create shape without nodes for easier manipulation

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
        preShape.forEach(segment -> shape.add(sf.newSegment(segment.getVector1(), segment.getVector2(), segment.getPercentLength())));
    }


    public Graph getGraph() {
        return graph;
    }


}
