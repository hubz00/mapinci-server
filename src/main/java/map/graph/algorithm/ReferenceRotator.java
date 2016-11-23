package map.graph.algorithm;

import map.graph.graphElements.Node;
import map.graph.graphElements.Vector;
import map.graph.graphElements.segments.Segment;
import map.graph.graphElements.segments.SegmentFactory;
import map.graph.graphElements.segments.SegmentReflection;
import map.graph.graphElements.segments.SegmentSoul;

import java.util.LinkedList;
import java.util.List;

public class ReferenceRotator {

    private SegmentFactory sf;

    public ReferenceRotator(){
        this.sf = new SegmentFactory();
    }

    public List<SegmentSoul> rotate(List<SegmentSoul> segments, Double angle){

        List<SegmentSoul> result = new LinkedList<>();
        int i = 0;

        segments.forEach(s -> {
            Vector v1 = s.getVector1();
            Vector v2 = s.getVector2();

            Vector rotatedV1 = new Vector(v1.getX()*Math.cos(angle) - v1.getY()*Math.sin(angle), v1.getX()*Math.sin(angle) + v1.getY()*Math.cos(angle));
            Vector rotatedV2 = new Vector(v2.getX()*Math.cos(angle) - v2.getY()*Math.sin(angle), v2.getX()*Math.sin(angle) + v2.getY()*Math.cos(angle));

            result.add(i,sf.newFullSegment(s.getId(),rotatedV1,rotatedV2));
        });

        return result;
    }
}
