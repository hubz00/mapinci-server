package map.graph.algorithm.conditions;

import map.graph.graphElements.segments.SegmentSoul;

public class PaddingCondition implements PrimaryCondition{

    private Double lengthEpsilon;
    private Double angleEpsilon;
    private DirectionCondition direction;

    PaddingCondition(Double lengthEpsilon, Double angleEpsilon){
        this.lengthEpsilon = lengthEpsilon;
        this.angleEpsilon = angleEpsilon;
        ConditionFactory factory = new ConditionFactory();
        this.direction = (DirectionCondition) factory.newCondition(angleEpsilon);
    }

    @Override
    public boolean meet(SegmentSoul graphSegment, SegmentSoul mapSegment, ConditionsResult result, boolean newSide) {
        if(direction.meet(graphSegment,mapSegment,result,true) && mapSegment.getLength() <= lengthEpsilon){
            result.setEnoughSpaceForAnotherSegment(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean applicable(SegmentSoul graphSegment, SegmentSoul mapSegment) {
        return (mapSegment.getLength() <= lengthEpsilon);
    }
}
