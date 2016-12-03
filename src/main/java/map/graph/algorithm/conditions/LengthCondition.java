package map.graph.algorithm.conditions;

import map.graph.graphElements.segments.SegmentSoul;

import java.util.logging.Logger;


/**
 * Checks whether proposed Segment's length
 * shorter than side's length or longer up to the given epsilon [fraction of current side]
 */
public class LengthCondition implements Condition {

    private Double overallLength;
    private Double epsilon;
    private Double epsilonLength;
    private Double lengthToFind;
    private Logger log;

    public LengthCondition(Double epsilon, Double overallLength){
        this.overallLength = overallLength;
        this.epsilon = epsilon;
        this.log = Logger.getLogger("LengthCondition");
    }

    @Override
    public boolean meet(SegmentSoul graphSegment, SegmentSoul mapSegment, ConditionsResult result, boolean newSide) {
        if(newSide){
            lengthToFind = graphSegment.getPercentLength()*overallLength;
            epsilonLength = lengthToFind*epsilon;
            log.info("NEW SIDE");
        }
        log.info(String.format("Length to find: %s\nChecked segment length: %s\nEpsilon: %s", lengthToFind, mapSegment.getLength(), epsilonLength));
        if(lengthToFind - mapSegment.getLength() >= -epsilonLength) {
            lengthToFind -= mapSegment.getLength();
            result.setEnoughSpaceForAnotherSegment(lengthToFind > epsilonLength);
            log.info("Length condition: " + true);
            return true;
        }
        //todo write tests
        log.info("Length condition: " + false);
        result.setBoolResult(false);
        return false;
    }
}
