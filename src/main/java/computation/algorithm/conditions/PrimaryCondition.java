package computation.algorithm.conditions;

import computation.graphElements.segments.SegmentSoul;

interface PrimaryCondition extends Condition {
    boolean applicable(SegmentSoul graphSegment, SegmentSoul mapSegment);
}
