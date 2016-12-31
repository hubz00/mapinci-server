package computation.algorithm.conditions;

import computation.graphElements.Vector;
import computation.graphElements.segments.SegmentSoul;

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
        this.direction = (DirectionCondition) factory.newDirectionCondition(slopeEpsilon);
        this.log = Logger.getLogger("PaddingCondition");
    }


    Double getLengthEpsilon() {
        return lengthEpsilon;
    }

    Double getSlopeEpsilon() {
        return slopeEpsilon;
    }

    @Override
    public boolean meet(SegmentSoul graphSegment, SegmentSoul mapSegment, ConditionsResult result, Vector shapeVector, Vector mapVector) {
        if(direction.meet(graphSegment,mapSegment,result, shapeVector, mapVector) && mapSegment.getLength() <= lengthEpsilon){
            result.setEnoughSpaceForAnotherSegment(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean applicable(SegmentSoul graphSegment, SegmentSoul mapSegment) {
        return (mapSegment.getLength() <= lengthEpsilon);
    }

    @Override
    public void simplify() {}
}
