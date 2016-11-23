package map.graph.graphElements.segments;

import map.graph.graphElements.Node;
import map.graph.graphElements.Vector;

import java.util.HashMap;

public class SegmentReflection implements SegmentSoul{

    private long id;
    private long correspondingSegmentId;
    private HashMap<Integer, Vector> vectors;
    private Double slope;

    protected SegmentReflection(long id,Long correspondingSegmentId, Vector v1, Vector v2){
        this.id = id;
        this.correspondingSegmentId = correspondingSegmentId;
        this.vectors = new HashMap<>();
        vectors.put(0,v1);
        vectors.put(1,v2);
        this.slope = v1.getY()/v1.getX();
    }

    public long getCorrespondingSegmentId(){
        return correspondingSegmentId;
    }

    public Vector getVector1(){
        return vectors.get(0);
    }
    public Vector getVector2(){
        return vectors.get(1);
    }

    public Long getId() {
        return id;
    }

    public Double getSlope() {
        return slope;
    }

    public String toString(){
        return String.format("[Corresponding id: %d] [Slope: %f]", id, slope);
    }
}
