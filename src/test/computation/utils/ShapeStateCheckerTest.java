package computation.utils;

import computation.graphElements.Node;
import computation.graphElements.segments.Segment;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ShapeStateCheckerTest {

    Node node1 = new Node(1L, 10.0, 10.0);
    Node node2 = new Node(2L, 11.0, 10.0);
    Node node3 = new Node(3L, 11.0, 11.0);

    Segment segment1 = new Segment(1L, node1, node2, 33.3);
    Segment segment2 = new Segment(2L, node2, node3, 33.3);
    Segment segment3 = new Segment(3L, node3, node1, 33.3);

    List<Segment> segments = new LinkedList<>();


    @Before
    public void setUp() throws Exception {
        segments.add(segment1);
        segments.add(segment2);
        segments.add(segment3);
    }

    @Test
    public void isClosedShape() throws Exception {
        boolean isClosed = ShapeStateChecker.isClosedShape(segments);
        assertThat(isClosed, is(equalTo(true)));
    }

}