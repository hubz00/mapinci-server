package computation.graphElements.segments;


import computation.graphElements.Vector;

import java.util.List;

public interface SegmentSoul {

    Double getSlope();
    Long getId();
    Vector getVector1();
    Vector getVector2();
    Double getLength();
    Double getPercentLength();
    Double getLengthToFind();
    void changeLengthToFind(Double value);
    List<Vector> getVectors();
}
