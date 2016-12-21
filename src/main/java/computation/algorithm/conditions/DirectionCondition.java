package computation.algorithm.conditions;


import computation.graphElements.segments.SegmentSoul;
import computation.utils.ReferenceRotator;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class DirectionCondition implements Condition {

    private double epsilon;
    private ReferenceRotator rotator;
    private Logger log;

    DirectionCondition(double epsilon){
        this.rotator = new ReferenceRotator();
        this.epsilon = epsilon;
        this.log = Logger.getLogger("DirectionCondition");
    }

    double getEpsilon() {
        return epsilon;
    }

    @Override
    public boolean meet(SegmentSoul shapeSegment, SegmentSoul mapSegment, ConditionsResult conditionsResult) {
        List<SegmentSoul> segments = new LinkedList<>();
        boolean met;

        segments.add(0,shapeSegment);
        segments.add(1,mapSegment);
        while (Math.abs(segments.get(0).getSlope()) == Double.POSITIVE_INFINITY
                || Math.abs(segments.get(1).getSlope()) == Double.POSITIVE_INFINITY){
            segments = rotator.rotate(segments,(Math.PI/12) + epsilon);
        }

        met = (Math.abs(segments.get(0).getSlope()/segments.get(1).getSlope())  >= 1.0 -  epsilon)
                && (Math.abs(segments.get(0).getSlope()/segments.get(1).getSlope()) <= 1.0 + epsilon);

        if(!met) conditionsResult.setBoolResult(false);
        log.info(String.format("\t\t\t\tDirection condition: [%s]\n\t\t\t\t\tShape segment slope: [%s]\n\t\t\t\t\tMap segment slope: [%s]", met,shapeSegment,mapSegment));
        return met;
    }

    @Override
    public void simplify() {
        this.epsilon *= 1.3;
    }
}
