package computation.graphElements;

public class LatLon {

    private final Double lat;
    private final Double lon;

    public LatLon(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String toString(){
        return " [Lat: " + lat + " Lon: " + lon + "] ";
    }
}
