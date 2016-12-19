import computation.algorithm.conditions.ConditionFactory;
import computation.algorithm.conditions.ConditionsResult;
import computation.algorithm.conditions.DirectionCondition;
import computation.graphElements.Vector;
import computation.graphElements.segments.SegmentFactory;
import computation.graphElements.segments.SegmentSoul;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class DirectionConditionTest {


    @Test
    public void epsilonAndInfiniteSlopeHandling(){
        assertTrue(Math.abs(Double.NEGATIVE_INFINITY) == Double.POSITIVE_INFINITY);

        ConditionFactory conditionFactory = new ConditionFactory();
        DirectionCondition condition = (DirectionCondition) conditionFactory.newDirectionCondition(0.1);

        SegmentFactory sf = new SegmentFactory();
        SegmentSoul mapSegment = sf.newSegment(1L, new Vector(0.0, 5.0), new Vector(0.0, -5.0));
        SegmentSoul graphSegment = sf.newSegment(1L, new Vector(0.05, 5.0), new Vector(-0.05, -5.0));
        assertTrue(condition.meet(mapSegment,graphSegment, new ConditionsResult()));
    }
}
