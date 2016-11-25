package map.graph.algorithm.conditions;

import map.graph.graphElements.segments.SegmentSoul;

interface PrimaryCondition extends Condition {
    boolean apply(SegmentSoul graphSegment, SegmentSoul mapSegment);
}
