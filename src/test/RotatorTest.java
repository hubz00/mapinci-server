import map.graph.algorithm.ReferenceRotator;
import map.graph.graphElements.NodeFactory;
import map.graph.graphElements.Vector;
import map.graph.graphElements.segments.Segment;
import map.graph.graphElements.segments.SegmentFactory;
import map.graph.graphElements.segments.SegmentSoul;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static junit.framework.TestCase.assertTrue;

public class RotatorTest {

    @Test
    public void checkIfSegmentsWillBeParallel(){

        List<SegmentSoul> list = new LinkedList<>();
        ReferenceRotator rotator = new ReferenceRotator();
        SegmentFactory sf = new SegmentFactory();
        NodeFactory nf = new NodeFactory();

        Segment s1 = sf.newSegment(nf.newNode(0.0,0.0), nf.newNode(0.0, 5.0));
        Segment s2 = sf.newSegment(nf.newNode(0.0,0.0), nf.newNode(5.0, 0.0));

        list.add(s2);
        list.add(sf.newSegment(new Vector(0.0,1.0), new Vector(0.0,-1.0)));
        list.add(sf.newSegment(new Vector(1.0,1.0), new Vector(-1.0,-1.0)));

        List<SegmentSoul> rotated = rotator.rotateShapeToFit(s1,s2,list);
        list.forEach(System.out::println);
        System.out.println("=========================");
        rotated.forEach(System.out::println);
        assertTrue(Objects.equals(s1.getSlope(), rotated.get(0).getSlope()));

    }
}
