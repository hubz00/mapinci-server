package communication.Coordinate;

/**
 * Created by m on 05.11.16.
 */
public class Coordinate {

    private double longitude;
    private double latitude;

    public Coordinate(double lat, double longitude) {
        this.latitude = lat;
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
