package computation.graphElements.segments;


import computation.graphElements.Vector;

public interface SegmentSoul {

    Double getSlope();
    Long getId();
    Vector getVector1();
    Vector getVector2();
    Double getLength();
    Double getPercentLength();
    Double getLengthToFind();
    void changeLengthToFind(Double value);
}
