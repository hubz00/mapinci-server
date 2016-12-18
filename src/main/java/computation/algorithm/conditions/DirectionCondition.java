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
    public boolean meet(SegmentSoul graphSegment, SegmentSoul mapSegment, ConditionsResult conditionsResult) {
        List<SegmentSoul> segments = new LinkedList<>();
        boolean notMet;
        segments.add(0,graphSegment);
        segments.add(1,mapSegment);
        while (Math.abs(segments.get(0).getSlope()) == Double.POSITIVE_INFINITY
                || Math.abs(segments.get(1).getSlope()) == Double.POSITIVE_INFINITY){
            segments = rotator.rotate(segments,(Math.PI/12) + epsilon);
        }
        Double angleEpsilon = Math.abs(segments.get(0).getSlope())*epsilon;
        if(angleEpsilon < 0.25)
            angleEpsilon = 0.25;
        notMet = (Math.abs(segments.get(0).getSlope() - segments.get(1).getSlope()) <= angleEpsilon)
                || (Math.abs(segments.get(0).getSlope() - segments.get(1).getSlope()) <= angleEpsilon);

        if(!notMet) conditionsResult.setBoolResult(false);
//        log.info(String.format("\t[Direction condition: %s]\t[Shape slope: %s] [Map slope: %s]", notMet, graphSegment.getSlope(),mapSegment.getSlope()));
        return notMet;
    }

    @Override
    public void simplify() {
        this.epsilon *= 1.3;
    }
}
