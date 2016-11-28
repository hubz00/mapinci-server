package map.graph.algorithm.conditions;

import map.graph.graphElements.segments.SegmentSoul;

public class PaddingCondition implements PrimaryCondition{

    private Double lengthEpsilon;
    private Double angleEpsilon;

    PaddingCondition(Double lengthEpsilon, Double angleEpsilon){
        this.lengthEpsilon = lengthEpsilon;
        this.angleEpsilon = angleEpsilon;
    }

    @Override
    public boolean meet(SegmentSoul graphSegment, SegmentSoul mapSegment) {
        //todo implement
        return false;
    }

    @Override
    public boolean apply(SegmentSoul graphSegment, SegmentSoul mapSegment) {
        return (mapSegment.getLength() <= lengthEpsilon);
    }
}
