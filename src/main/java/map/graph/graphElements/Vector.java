package map.graph.graphElements;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.logging.Logger;

import static java.lang.Math.atan2;

public class Vector{

    private Double x;
    private Double y;
    private Logger log;

    public Vector(Node n1, Node n2){
        x = n2.getLongitude() - n1.getLongitude();
        y = n2.getLatitude() - n1.getLatitude();
        this.log = Logger.getLogger("Reference rotator");
    }

    public Vector(Double x, Double y){
        this.x = x;
        this.y = y;
    }

    public Vector() {};

    @JsonIgnore
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
        Double otherX = v.getX();
        Double otherY = v.getY();
        return Math.acos((x*otherX + y*otherY)/(Math.sqrt(x*x + y*y) * Math.sqrt(otherX*otherX + otherY*otherY)));
    }

    public String toString(){
        return String.format("X: %f    Y: %f",x,y);
    }
}
