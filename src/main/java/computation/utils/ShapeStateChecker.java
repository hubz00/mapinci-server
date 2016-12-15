package computation.utils;

import computation.graphElements.Node;
import computation.graphElements.segments.Segment;

import java.util.List;

public class ShapeStateChecker {

    public static boolean isClosedShape(List<Segment> shape) {
        if(shape.size() < 3) return false;
        Segment firstSegment = shape.get(0);
        Segment lastSegment = shape.get(shape.size()-1);
        Segment secondSegment = shape.get(1);
        Segment preLastSegment = shape.get(shape.size()-2);

        Node firstSegmentNode1 = firstSegment.getNode1();
        Node firstSegmentNode2 = firstSegment.getNode2();

        //after checking nodes in shape, create shape without nodes for easier manipulation

        if(lastSegment.contains(firstSegmentNode1)
                && !preLastSegment.contains(firstSegmentNode1)
                && !secondSegment.contains(firstSegmentNode1)) return true;

        else if(lastSegment.contains(firstSegmentNode2)
                && !preLastSegment.contains(firstSegmentNode2)
                && !secondSegment.contains(firstSegmentNode2)) return true;

        return false;
    }
}
