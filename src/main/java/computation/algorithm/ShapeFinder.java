package computation.algorithm;

import computation.algorithm.conditions.ConditionManager;
import computation.algorithm.conditions.ConditionsResult;
import computation.graphElements.*;
import computation.graphElements.Vector;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentFactory;
import computation.graphElements.segments.SegmentSoul;
import computation.utils.ReferenceRotator;

import java.util.*;
import java.util.logging.Logger;

import static computation.utils.ShapeStateChecker.isClosedShape;

public class ShapeFinder {

    private final Graph graph;
    private List<Segment> preShape;
    private List<SegmentSoul> shape;
    private List<Segment> onMapSegments;
    private Logger log = Logger.getLogger("Shape Finder");
    private ConditionManager conditionManager;
    private ReferenceRotator referenceRotator;
    private SegmentSoul firstOriginalAngleSegment;
    private Double overallLength;

    public ShapeFinder(Graph graph){
        this.graph = graph;
        this.referenceRotator = new ReferenceRotator();
    }

    List<Segment> findShapeForNode(Node node, List<Segment> shape, ConditionManager conditionManager, Double overallLength){
        this.overallLength = overallLength;
        this.shape = new LinkedList<>();
        this.preShape = shape;
        this.conditionManager = conditionManager;
        this.onMapSegments = new LinkedList<>();

        migratePreShapeToShape();
        if(isClosedShape(shape)){
//            log.info("Is a closed shape");
            for(int i = 0; i < this.shape.size(); i++){
//                log.info(String.format("Taking next node to check, loop: %d, left: %d", i, this.shape.size() - i));
                if(initAlgorithm(node)){
                    return onMapSegments;
                }
                SegmentSoul tmp = this.shape.remove(0);
                this.shape.add(tmp);
                this.onMapSegments = new LinkedList<>();
                conditionManager.reset();
            }
        }
        else {
            if(initAlgorithm(node)) {
                return onMapSegments;
            }
            Collections.reverse(this.shape);
            this.onMapSegments = new LinkedList<>();
            conditionManager.reset();
            if(initAlgorithm(node)){
                return onMapSegments;
            }
        }
//        log.info("Empty result graph");
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
            for(Vector shapeVector: shape.get(0).getVectors()) {
                this.shape = referenceRotator.rotateShapeToFit(shape,s.getVectorFromNode(startNode),  shapeVector);
//            log.info("New Shape angle --------------------------------------------");
//            shape.forEach(seg -> log.info(seg.toString()));
//            log.info("------------------------------------------------------------");
                ConditionsResult result = conditionManager.checkConditions(shape.get(0), s);
                if (result.areMet()) {
                    //todo add handling condition manager
//                log.info(String.format("[Adding new segment to result: %s]", s));
                    onMapSegments.add(s);
                    shape.get(0).changeLengthToFind(-s.getLength());
                    if (result.isEnoughSpaceForAnotherSegment()) {
                        if (findNextSegment(s.getNeighbour(startNode), 0, false))
                            return true;
                    } else {
                        if (findNextSegment(s.getNeighbour(startNode), 1, true))
                            return true;
                    }
                    shape.get(0).changeLengthToFind(s.getLength());
                }

                onMapSegments = new LinkedList<>();
                conditionManager.reset();
            }
        }
        return false;
    }

    private boolean findNextSegment(Node startNode, int position, boolean newSegment){
        if(position == shape.size()) return true;

        List<Segment> possibleSegments = graph.getSegmentsForNode(startNode);
        SegmentSoul segmentToMap = shape.get(position);
//        log.info(String.format("New call\n\t[Position: %s]\n\t\t[Start node: %s]\n\t\t[Segment to map: %s]",position, startNode, segmentToMap));

        for (Segment s: possibleSegments){
//            log.info(String.format("\t[Checking Segment: %s]\n\t\t[Comparing to recently added: %s]\n\t\t[Added list size: %s]",s, s.compareTo(onMapSegments.get(onMapSegments.size()-1)), onMapSegments.size()));
            if(((onMapSegments.size() == 0) || (s.compareTo(onMapSegments.get(onMapSegments.size()-1)) != 0))) {
                ConditionsResult conditionsResult = conditionManager.checkConditions(segmentToMap, s);
                if (conditionsResult.areMet()) {
                    onMapSegments.add(s);
                    segmentToMap.changeLengthToFind(-s.getLength());
//                    log.info(String.format("[Adding new segment to result: %s]", s));
                    if (conditionsResult.isEnoughSpaceForAnotherSegment())
                        if (findNextSegment(s.getNeighbour(startNode), position, false))
                            return true;
                        else{
                            onMapSegments.remove(s);
                            segmentToMap.changeLengthToFind(s.getLength());
                        }
                    else {
                        if(position == 0){
                            checkRotation();
                        }
                        if (findNextSegment(s.getNeighbour(startNode), position + 1, true))
                            return true;
                        else {
                            onMapSegments.remove(s);
                            segmentToMap.changeLengthToFind(s.getLength());
                        }
                    }
                }
            }
        }
        return false;
    }

    private void checkRotation() {

        SegmentSoul shapeSegment = shape.get(0);

//        log.info(String.format("\t\tCurrent slope: %f real Slope: %f", shapeSegment.getSlope(), firstOriginalAngleSegment.getSlope()));
        if(Math.abs(shapeSegment.getSlope() - firstOriginalAngleSegment.getSlope()) >= 0.1){
            this.shape = referenceRotator.rotateShapeToFit(shape,firstOriginalAngleSegment.getVector1(), shapeSegment.getVector1());
//            log.info("New Shape angle --------------------------------------------");
//            shape.forEach(seg -> log.info(seg.toString()));
//            log.info("------------------------------------------------------------");
        }

    }


    private void migratePreShapeToShape() {
        SegmentFactory sf = new SegmentFactory();
        preShape.forEach(segment -> shape.add(sf.newSegment(segment.getVector1(), segment.getVector2(), segment.getPercentLength(), overallLength)));
        firstOriginalAngleSegment = shape.get(0);
    }


    public Graph getGraph() {
        return graph;
    }


}
