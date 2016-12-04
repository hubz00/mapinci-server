package map.graph.algorithm.conditions;


import map.graph.algorithm.ReferenceRotator;
import map.graph.graphElements.segments.SegmentSoul;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
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

    @Override
    public boolean meet(SegmentSoul graphSegment, SegmentSoul mapSegment, ConditionsResult conditionsResult, boolean newSide) {
        List<SegmentSoul> segments = new LinkedList<>();
        boolean notMet;
        segments.add(0,graphSegment);
        segments.add(1,mapSegment);
        while (Math.abs(segments.get(0).getSlope()) == Double.POSITIVE_INFINITY
                || Math.abs(segments.get(1).getSlope()) == Double.POSITIVE_INFINITY){
            segments = rotator.rotate(segments,(Math.PI/12) + epsilon);
        }
        notMet = (Math.abs(segments.get(0).getSlope() - segments.get(1).getSlope()) <= epsilon)
                || (Math.abs(segments.get(0).getSlope() - segments.get(1).getSlope()) <= epsilon);

        if(!notMet) conditionsResult.setBoolResult(false);
        log.info(String.format("\t[Direction condition: %s]", notMet));
        return notMet;
    }

    @Override
    public void revertLastCheck() {

    }
}
