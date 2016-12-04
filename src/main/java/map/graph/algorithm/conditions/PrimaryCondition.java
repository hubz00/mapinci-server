package map.graph.algorithm.conditions;

import map.graph.graphElements.segments.SegmentSoul;

interface PrimaryCondition extends Condition {
    boolean applicable(SegmentSoul graphSegment, SegmentSoul mapSegment);
}
