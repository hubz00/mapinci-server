import map.graph.algorithm.conditions.DirectionCondition;
import map.graph.graphElements.Vector;
import map.graph.graphElements.segments.Segment;
import map.graph.graphElements.segments.SegmentFactory;
import map.graph.graphElements.segments.SegmentReflection;
import map.graph.graphElements.segments.SegmentSoul;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import static junit.framework.TestCase.assertTrue;

public class DirectionConditionTest {


    @Test
    public void epsilonAndInfiniteSlopeHandling(){
        assertTrue(Math.abs(Double.NEGATIVE_INFINITY) == Double.POSITIVE_INFINITY);

        DirectionCondition condition = new DirectionCondition(0.1);

        SegmentFactory sf = new SegmentFactory();
        SegmentSoul mapSegment = sf.newFullSegment(1L, new Vector(0.0, 5.0), new Vector(0.0, -5.0));
        SegmentSoul graphSegment = sf.newFullSegment(1L, new Vector(0.05, 5.0), new Vector(-0.05, -5.0));
        assertTrue(condition.meet(mapSegment,graphSegment));
    }
}
