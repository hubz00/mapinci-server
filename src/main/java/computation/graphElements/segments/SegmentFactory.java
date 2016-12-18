package computation.graphElements.segments;

import computation.graphElements.Node;
import computation.graphElements.Vector;

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

    public SegmentReflection newSegment(Long correspondingId, Vector v1, Vector v2, Double percentLength, double overallLength){
        SegmentReflection sr = new SegmentReflection(id, correspondingId, v1, v2, percentLength, overallLength);
        id++;
        return sr;
    }

    public SegmentReflection newSegment(Vector v1, Vector v2){
        SegmentReflection sr = new SegmentReflection(id, v1, v2);
        id++;
        return sr;
    }

    public SegmentReflection newSegment(Vector v1, Vector v2, Double percentLength, double overallLength){
        SegmentReflection sr = new SegmentReflection(id, id, v1, v2, percentLength, overallLength);
        id++;
        return sr;
    }



}
