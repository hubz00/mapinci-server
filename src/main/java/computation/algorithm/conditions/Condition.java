package computation.algorithm.conditions;

import computation.graphElements.Vector;
import computation.graphElements.segments.SegmentSoul;

public interface Condition {
    boolean meet(SegmentSoul graphSegment, SegmentSoul mapSegment, ConditionsResult result, Vector shapeVector, Vector mapVector);
    void simplify();
}
