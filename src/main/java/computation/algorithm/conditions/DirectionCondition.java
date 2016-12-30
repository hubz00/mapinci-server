package computation.algorithm.conditions;


import computation.graphElements.Vector;
import computation.graphElements.segments.SegmentSoul;

import java.util.logging.Logger;

public class DirectionCondition implements Condition {

    private double epsilon;
    private Logger log;

    /**
     *
     * @param epsilon -  range in radians
     */
    DirectionCondition(double epsilon){
        this.epsilon = epsilon;
        this.log = Logger.getLogger("DirectionCondition");
    }

    double getEpsilon() {
        return epsilon;
    }

    @Override
    public boolean meet(SegmentSoul shapeSegment, SegmentSoul mapSegment, ConditionsResult conditionsResult, Vector shapeVector, Vector mapVector) {
        boolean met;
        met = (shapeVector.getAngleBetween(mapVector) < epsilon);
        if(!met) conditionsResult.setBoolResult(false);
        return met;
    }

    @Override
    public void simplify() {
        this.epsilon *= 1.3;
    }
}
