package map.graph.graphElements.segments;

import map.graph.graphElements.Node;
import map.graph.graphElements.Vector;

public final class SegmentFactory {

    private static long id;

    public SegmentFactory(){
        SegmentFactory.id = 1;
    }

    public Segment newFullSegment(Node n1, Node n2){
        Segment segment = new Segment(id,n1,n2);
        id++;
        return segment;
    }

    public SegmentReflection newFullSegment(Long correspondingId, Vector v1, Vector v2){
        SegmentReflection sr = new SegmentReflection(id, correspondingId, v1, v2);
        id++;
        return sr;
    }

    public Segment newHalfSegment(Node n1){
        Segment segment = new Segment(id,n1);
        id++;
        return segment;
    }


}
