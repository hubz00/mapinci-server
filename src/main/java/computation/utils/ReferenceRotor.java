package computation.utils;

import computation.graphElements.Node;
import computation.graphElements.Vector;
import computation.graphElements.segments.SegmentSoul;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class ReferenceRotor {

    private SegmentFactory sf;
    private Logger log;

    public ReferenceRotor(){
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

            result.add(i,sf.newSegment(s.getId(),rotatedV1,rotatedV2, s.getPercentLength(), s.getLength()));
            i += 1;
        }

        return result;
    }

    public List<SegmentSoul> rotateShapeToFit(List<SegmentSoul> shape, Vector mapVector, Vector shapeVector) {
        Double angle = mapVector.getAngleBetween(shapeVector);
        if(angle >= 0 && angle < Math.pow(5,-8) || angle <= 0 && angle > Math.pow(-5,-8)) {
            return shape;
        }

        angle = checkSumOrSub(mapVector, shapeVector) * angle;
        return rotate(shape, angle);
    }

    private Double checkSumOrSub(Vector mapVector, Vector shapeVector) {

        Double mX = mapVector.getX();
        Double mY = mapVector.getY();
        Double sX = shapeVector.getX();
        Double sY = shapeVector.getY();
        Double mSlope = mY/mX;
        Double sSlope = sY/sX;

        switch (whichQuarter(mX,mY)){
            case 1:{
                switch (whichQuarter(sX, sY)){
                    case 1:{
                        if(sSlope > mSlope)
                            return -1.0;
                        else
                            return 1.0;
                    }
                    case 2:{
                        return -1.0;
                    }
                    case 3:{
                        if(sSlope > mSlope)
                            return 1.0;
                        else
                            return -1.0;
                    }
                    case 4:{
                        return 1.0;
                    }
                    default:
                        return 1.0;
                }
            }
            case 2:{
                switch (whichQuarter(sX, sY)){
                    case 1:{
                        return 1.0;
                    }
                    case 2:{
                        if (sSlope > mSlope)
                            return -1.0;
                        else
                            return 1.0;
                    }
                    case 3:{
                        return -1.0;
                    }
                    case 4:{
                        if (sSlope > mSlope)
                            return 1.0;
                        else
                            return -1.0;
                    }
                    default:
                        return 1.0;
                }
            }
            case 3:{
                switch (whichQuarter(sX, sY)){
                    case 1:{
                        if(sSlope > mSlope)
                            return 1.0;
                        else
                            return -1.0;
                    }
                    case 2:{
                        return 1.0;
                    }
                    case 3:{
                        if(sSlope > mSlope)
                            return -1.0;
                        else
                            return 1.0;
                    }
                    case 4:{
                        return -1.0;
                    }
                    default:
                        return 1.0;
                }
            }
            case 4:{
                switch (whichQuarter(sX, sY)){
                    case 1:{
                        return -1.0;
                    }
                    case 2:{
                        if(sSlope > mSlope)
                            return 1.0;
                        else
                            return -1.0;
                    }
                    case 3:{
                        return 1.0;
                    }
                    case 4:{
                        if (sSlope > mSlope)
                            return -1.0;
                        else
                            return 1.0;
                    }
                    default:
                        return 1.0;
                }
            }
            default:
                return 1.0;
        }

    }

    private int whichQuarter(Double x, Double y){
        if(x >= 0){
            if (y >= 0)
                return 1;
            else
                return 4;
        }
        else{
            if(y >= 0)
                return 2;
            else
                return 3;
        }
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
