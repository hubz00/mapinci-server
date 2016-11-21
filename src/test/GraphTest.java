import map.graph.graphElements.*;
import map.graph.graphElements.segments.Segment;
import map.graph.graphElements.segments.SegmentFactory;
import map.graph.graphElements.segments.SegmentImpl;
import org.junit.Test;

import java.util.Objects;

import static junit.framework.TestCase.assertTrue;

public class GraphTest {

    @Test
    public void vectorsCreation(){
        NodeFactory nf = new NodeFactory();

        Node n1 = nf.newNode(0.0,10.0);
        Node n2 = nf.newNode(5.0,10.0);

        SegmentFactory sf = new SegmentFactory();

        Segment s1 = sf.newHalfSegment(n1);
        s1.setNode2(n2);

        Vector vs1n1 = s1.getVectorFromNode(n1);
        Vector vs1n2 = s1.getVectorFromNode(n2);

        Double vs1n1X = vs1n1.getX();
        Double vs1n1Y = vs1n1.getY();

        Double vs1n2X = vs1n2.getX();
        Double vs1n2Y = vs1n2.getY();

        Double n1X = n2.getLongitude() - n1.getLongitude();
        Double n1Y = n2.getLatitude() - n1.getLatitude();

        Double n2X = n1.getLongitude() - n2.getLongitude();
        Double n2Y = n1.getLatitude() - n2.getLatitude();

        assertTrue(Objects.equals(vs1n1X, n1X));
        assertTrue(Objects.equals(vs1n1Y, n1Y));
        assertTrue(Objects.equals(vs1n2X, n2X));
        assertTrue(Objects.equals(vs1n2Y, n2Y));
    }
}
