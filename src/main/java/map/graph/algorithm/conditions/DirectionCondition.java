package map.graph.algorithm.conditions;


import map.graph.graphElements.segments.Segment;

public class DirectionCondition implements Condition {

    private double epsilon;

    public DirectionCondition(double epsilon){
        this.epsilon = epsilon;
    }

    @Override
    public boolean meet(Segment graphSegment, Segment mapSegment) {
        //todo implement applying rotation here if "bad" slope
        return (Math.abs(mapSegment.getSlope() - graphSegment.getSlope()) <= epsilon)
                        || (Math.abs(mapSegment.getSlope() - graphSegment.getSlope()) <= epsilon);
    }
}
