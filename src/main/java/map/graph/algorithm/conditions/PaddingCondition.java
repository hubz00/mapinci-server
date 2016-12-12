package map.graph.algorithm.conditions;

import map.graph.graphElements.segments.SegmentSoul;

import java.util.logging.Logger;

public class PaddingCondition implements PrimaryCondition{

    private Double lengthEpsilon;
    private Double slopeEpsilon;
    private DirectionCondition direction;
    private Logger log;

    PaddingCondition(Double lengthEpsilon, Double slopeEpsilon){
        this.lengthEpsilon = lengthEpsilon;
        this.slopeEpsilon = slopeEpsilon;
        ConditionFactory factory = new ConditionFactory();
        this.direction = (DirectionCondition) factory.newCondition(slopeEpsilon);
        this.log = Logger.getLogger("PaddingCondition");
    }


    Double getLengthEpsilon() {
        return lengthEpsilon;
    }

    Double getSlopeEpsilon() {
        return slopeEpsilon;
    }

    @Override
    public boolean meet(SegmentSoul graphSegment, SegmentSoul mapSegment, ConditionsResult result, boolean newSide) {
        if(direction.meet(graphSegment,mapSegment,result,true) && mapSegment.getLength() <= lengthEpsilon){
            result.setEnoughSpaceForAnotherSegment(true);
            log.info(String.format("\t[Padding condition: true]"));
            return true;
        }
        log.info(String.format("\t[Padding condition: false]"));
        return false;
    }

    @Override
    public void revertLastCheck() {

    }

    @Override
    public boolean applicable(SegmentSoul graphSegment, SegmentSoul mapSegment) {
        log.info(String.format("\t[Map length: %s] [length Epsilon: %s]", mapSegment.getLength(), lengthEpsilon));
        return (mapSegment.getLength() <= lengthEpsilon);
    }
}
