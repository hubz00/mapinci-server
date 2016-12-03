package map.graph.graphElements.segments;

import map.graph.graphElements.Node;
import map.graph.graphElements.Vector;

public final class SegmentFactory {

    private static long id;

    public SegmentFactory(){
        SegmentFactory.id = 1;
    }

    public Segment newSegment(Node n1, Node n2){
        Segment segment = new Segment(id,n1,n2);
        id++;
        return segment;
    }

    public Segment newSegment(Node n1, Node n2, Double percentLength){
        Segment segment = new Segment(id,n1,n2, percentLength);
        id++;
        return segment;
    }

    public SegmentReflection newSegment(Long correspondingId, Vector v1, Vector v2){
        SegmentReflection sr = new SegmentReflection(id, correspondingId, v1, v2);
        id++;
        return sr;
    }

    public SegmentReflection newSegment(Long correspondingId, Vector v1, Vector v2, Double percentLength){
        SegmentReflection sr = new SegmentReflection(id, correspondingId, v1, v2, percentLength);
        id++;
        return sr;
    }

    public SegmentReflection newSegment(Vector v1, Vector v2){
        SegmentReflection sr = new SegmentReflection(id, v1, v2);
        id++;
        return sr;
    }

    public SegmentReflection newSegment(Vector v1, Vector v2, Double percentLength){
        SegmentReflection sr = new SegmentReflection(id, id, v1, v2, percentLength);
        id++;
        return sr;
    }



}
