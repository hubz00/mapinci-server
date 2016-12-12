package map.graph.utils;

import map.graph.graphElements.Vector;
import map.graph.graphElements.segments.Segment;
import map.graph.graphElements.segments.SegmentFactory;
import map.graph.graphElements.segments.SegmentSoul;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class ReferenceRotator {

    private SegmentFactory sf;
    private Logger log;

    public ReferenceRotator(){
        this.sf = new SegmentFactory();
        this.log = Logger.getLogger("Reference rotator");
    }

    public List<SegmentSoul> rotate(List<SegmentSoul> segments, Double angle){
        List<SegmentSoul> result = new LinkedList<>();
        int i = 0;

        for(SegmentSoul s: segments){
            Vector v1 = s.getVector1();
            Vector v2 = s.getVector2();

            Vector rotatedV1 = new Vector(roundUp(v1.getX()*Math.cos(angle) - v1.getY()*Math.sin(angle)), roundUp(v1.getX()*Math.sin(angle) + v1.getY()*Math.cos(angle)));
            Vector rotatedV2 = new Vector(roundUp(v2.getX()*Math.cos(angle) - v2.getY()*Math.sin(angle)), roundUp(v2.getX()*Math.sin(angle) + v2.getY()*Math.cos(angle)));

            result.add(i,sf.newSegment(s.getId(),rotatedV1,rotatedV2, s.getPercentLength()));
            i += 1;
        }

        return result;
    }

    public List<SegmentSoul> rotateShapeToFit(Segment mapSegment, SegmentSoul shapeSegment, List<SegmentSoul> shape) {
        Double angle = mapSegment.getVector1().getAngleBetween(shapeSegment.getVector2());
        if(angle > 0 && angle < Math.pow(5,-8) || angle < 0 && angle > Math.pow(-5,-8)) {
            log.info("Angle too small");
            return shape;
        }
        log.info("\tRotating. Angle: " + angle);
        return rotate(shape, angle);
    }

    private Double roundUp(Double x){
        if(x < Math.pow(3,-16) && x > 0.0)
            return 0.0;
        else if (x > Math.pow(3,-16) && x < 0.0)
            return -0.0;
        else if(x > 1.0 && x < 1.0000000000003)
            return 1.0;
        else if(x < 1.0 && x > 0.99999999999999)
            return 1.0;
        else if(x < -1.0 && x > -1.0000000000000003)
            return -1.0;
        else if(x > -1.0 && x < -0.9999999999998)
            return -1.0;
        else
            return x;
    }
}
