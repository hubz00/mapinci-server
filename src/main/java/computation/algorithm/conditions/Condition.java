package computation.algorithm.conditions;

import computation.graphElements.segments.SegmentSoul;

public interface Condition {
    boolean meet(SegmentSoul graphSegment, SegmentSoul mapSegment, ConditionsResult result, boolean newSide);
    void revertLastCheck();
}
