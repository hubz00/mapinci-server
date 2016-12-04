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
    private Double lastCheckLength;
    private Logger log;

    LengthCondition(Double epsilon, Double overallLength){
        this.overallLength = overallLength;
        this.epsilon = epsilon;
        this.log = Logger.getLogger("LengthCondition");
        this.lastCheckLength = 0.0;
    }

    @Override
    public boolean meet(SegmentSoul graphSegment, SegmentSoul mapSegment, ConditionsResult result, boolean newSide) {
        if(newSide){
            lengthToFind = graphSegment.getPercentLength()*overallLength;
            epsilonLength = lengthToFind*epsilon;
            log.info(String.format("NEW SIDE [Shape Segment: %s]", graphSegment));
        }
        log.info(String.format("\t[Length to find: %s]\n\t\t[Checked segment length: %s]\n\t\t[Epsilon: %s]", lengthToFind, mapSegment.getLength(), epsilonLength));
        if(lengthToFind - mapSegment.getLength() >= -epsilonLength) {
            lengthToFind -= mapSegment.getLength();
            lastCheckLength = mapSegment.getLength();
            result.setEnoughSpaceForAnotherSegment(lengthToFind > epsilonLength);
            log.info("\t[Length condition: " + true + "]");
            return true;
        }
        //todo write tests
        log.info("\t[Length condition: " + false + "] ");
        result.setBoolResult(false);
        return false;
    }

    @Override
    public void revertLastCheck() {
        lengthToFind += lastCheckLength;
        lastCheckLength = 0.0;
    }
}
