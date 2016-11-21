package map.graph.algorithm.conditions;

import map.graph.graphElements.segments.Segment;

public interface Condition {
    boolean meet(Segment graphSegment, Segment mapSegment);
}
