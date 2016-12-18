package computation.utils;

import computation.graphElements.LatLon;
import computation.graphElements.Node;

public class PositionApproximator {

    /**offsets given in meters
     *
     * @param n
     * @param offsetX
     * @param offsetY
     * @return
     */
    public LatLon offset(Node n, Double offsetX, Double offsetY){
        //Coordinate offsets in radians
        int earthRadius = 6378137;
        Double dLat = offsetY/earthRadius;
        Double dLon = offsetX/(earthRadius*Math.cos(Math.PI*n.getLatitude()/180));

        return new LatLon(n.getLatitude() + dLat*180/Math.PI, n.getLongitude() + dLon*180/Math.PI);
    }
}
