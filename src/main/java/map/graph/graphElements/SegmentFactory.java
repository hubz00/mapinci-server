package map.graph.graphElements;

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

    public Segment newHalfSegment(Node n1){
        Segment segment = new Segment(id,n1);
        id++;
        return segment;
    }


}
