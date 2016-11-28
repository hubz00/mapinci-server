package map.graph.algorithm.conditions;


import map.graph.algorithm.ReferenceRotator;
import map.graph.graphElements.segments.SegmentSoul;

import java.util.LinkedList;
import java.util.List;

public class DirectionCondition implements Condition {

    private double epsilon;
    private ReferenceRotator rotator;

    public DirectionCondition(double epsilon){
        this.rotator = new ReferenceRotator();
        this.epsilon = epsilon;
    }

    @Override
    public boolean meet(SegmentSoul graphSegment, SegmentSoul mapSegment) {
        List<SegmentSoul> segments = new LinkedList<>();
        segments.add(0,graphSegment);
        segments.add(1,mapSegment);
        while (Math.abs(segments.get(0).getSlope()) == Double.POSITIVE_INFINITY
                || Math.abs(segments.get(1).getSlope()) == Double.POSITIVE_INFINITY){
            segments = rotator.rotate(segments,(Math.PI/12) + epsilon);
        }
        return (Math.abs(segments.get(0).getSlope() - segments.get(1).getSlope()) <= epsilon)
                        || (Math.abs(segments.get(0).getSlope() - segments.get(1).getSlope()) <= epsilon);
    }
}
