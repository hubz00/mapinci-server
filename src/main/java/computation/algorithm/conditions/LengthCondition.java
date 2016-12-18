package computation.algorithm.conditions;

import computation.graphElements.segments.SegmentSoul;

import java.util.logging.Logger;


/**
 * Checks whether proposed Segment's length
 * shorter than side's length or longer up to the given epsilon [fraction of current side]
 */
public class LengthCondition implements Condition {

    private Double epsilon;
    private Logger log;
    private SegmentSoul justChecked;
    private Double changedByValue;

    /**
     * @param epsilon - multiply by side length defines the range of length search
     */
    LengthCondition(Double epsilon){
        this.epsilon = epsilon;
        this.log = Logger.getLogger("LengthCondition");
    }

    public Double getEpsilon() {
        return epsilon;
    }

    @Override
    public boolean meet(SegmentSoul graphSegment, SegmentSoul mapSegment, ConditionsResult result) {
        Double lengthToFind = graphSegment.getLengthToFind();
        Double epsilonLength = lengthToFind * epsilon;
//        log.info(String.format("\t[Length to find: %s]\n\t\t[Checked segment length: %s]\n\t\t[Epsilon: %s]", lengthToFind, mapSegment.getLength(), epsilonLength));
        if(lengthToFind - mapSegment.getLength() >= -epsilonLength) {
            justChecked = graphSegment;
            changedByValue = mapSegment.getLength();
            lengthToFind -= mapSegment.getLength();
            graphSegment.changeLengthToFind(-mapSegment.getLength());
            result.setEnoughSpaceForAnotherSegment(lengthToFind > epsilonLength);
//            log.info("\t[Length condition: " + true + "]");
            return true;
        }
        //todo write tests
//        log.info("\t[Length condition: " + false + "] ");
        result.setBoolResult(false);
        return false;
    }

    @Override
    public void simplify() {
        this.epsilon *= 1.3;
    }


}
