package map.graph.algorithm.conditions;

import map.graph.graphElements.segments.SegmentSoul;

public interface Condition {
    boolean meet(SegmentSoul graphSegment, SegmentSoul mapSegment);
}
