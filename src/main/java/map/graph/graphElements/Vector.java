package map.graph.graphElements;

import static java.lang.Math.atan2;

public class Vector{

    private final Double x;
    private final Double y;

    public Vector(Node n1, Node n2){
        x = n2.getLongitude() - n1.getLongitude();
        y = n2.getLatitude() - n1.getLatitude();
    }

    public Double[] getPoints() {
        Double[] points = new Double[2];
        points[0] = x;
        points[1] = y;
        return points;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getAngleBetween(Vector v){
        return atan2( x*v.getY() - y*v.getX(), x*v.getX() + y*v.getY() );
    }
}
