package computation.algorithm.conditions;

import computation.graphElements.Vector;
import computation.graphElements.segments.SegmentSoul;

import java.util.logging.Logger;


/**
 * Checks whether proposed Segment's length
 * shorter than side's length or longer up to the given epsilon [fraction of current side]
 */
public class LengthCondition implements Condition {

    private Double epsilon;
    private Logger log;

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
    public boolean meet(SegmentSoul shapeSegment, SegmentSoul mapSegment, ConditionsResult result, Vector shapeVector, Vector mapVector) {
        Double lengthToFind = shapeSegment.getLengthToFind();
        Double epsilonLength = shapeSegment.getLength() * epsilon;
        if(lengthToFind - mapSegment.getLength() >= -epsilonLength) {
            lengthToFind -= mapSegment.getLength();
            result.setEnoughSpaceForAnotherSegment(lengthToFind > epsilonLength);
            log.info(String.format("\t\t\t\tLength condition: [True]\n\t\t\t\t\tShape segment length to find: [%s]\n\t\t\t\t\tMap segment slope: [%s]",shapeSegment.getLengthToFind(),mapSegment.getLength()));
            return true;
        }
        //todo write tests
        result.setBoolResult(false);
        log.info(String.format("\t\t\t\tLength condition: [False]\n\t\t\t\t\tShape segment length to find: [%s]\n\t\t\t\t\tMap segment slope: [%s]",shapeSegment.getLengthToFind(),mapSegment.getLength()));
        return false;
    }

    @Override
    public void simplify() {
        this.epsilon *= 1.3;
    }


}
