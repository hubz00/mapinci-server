package computation.graphElements.segments;

import computation.graphElements.Vector;

import java.util.HashMap;

public class SegmentReflection implements SegmentSoul{

    private long id;
    private long correspondingSegmentId;
    private HashMap<Integer, Vector> vectors;
    private Double slope;
    private Double percentLength;
    private Double length;
    private Double lengthToFind;

    SegmentReflection(long id, Long correspondingSegmentId, Vector v1, Vector v2){
        this.id = id;
        this.correspondingSegmentId = correspondingSegmentId;
        this.vectors = new HashMap<>();
        vectors.put(0,v1);
        vectors.put(1,v2);
        this.slope = v1.getY()/v1.getX();
    }

    SegmentReflection(long id, Long correspondingSegmentId, Vector v1, Vector v2, Double percentLength){
        this.id = id;
        this.correspondingSegmentId = correspondingSegmentId;
        this.vectors = new HashMap<>();
        vectors.put(0,v1);
        vectors.put(1,v2);
        this.slope = v1.getY()/v1.getX();
        this.percentLength = percentLength;
    }

    SegmentReflection(long id, Long correspondingSegmentId, Vector v1, Vector v2, Double percentLength, double overallLength){
        assert v1 != null;
        assert v2 != null;
        this.id = id;
        this.correspondingSegmentId = correspondingSegmentId;
        this.vectors = new HashMap<>();
        vectors.put(0,v1);
        vectors.put(1,v2);
        this.slope = v1.getY()/v1.getX();
        if(percentLength == null){
            this.percentLength = 1.0;
        } else {
            this.percentLength = percentLength;
        }
        this.length = this.percentLength * overallLength;
        this.lengthToFind = length;
    }

    SegmentReflection(long id, Vector v1, Vector v2){
        this.id = id;
        this.correspondingSegmentId = id;
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

    @Override
    public Double getLength() {
        return this.length;
    }

    public Double getPercentLength() {
        return percentLength;
    }

    public void setPercentLength(Double percentLength) {
        this.percentLength = percentLength;
    }

    public Long getId() {
        return id;
    }

    public Double getSlope() {
        return slope;
    }

    public String toString() {
        return String.format("[Slope: %s]", slope.toString());
    }

    public Double getLengthToFind(){
        return this.lengthToFind;
    }

    public void changeLengthToFind(Double addedValue){
        this.lengthToFind += addedValue;
    }
}
