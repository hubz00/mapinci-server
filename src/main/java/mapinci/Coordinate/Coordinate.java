package mapinci.Coordinate;

public class Coordinate {

    private final int shapeId;
    private final String startingLong;
    private final String startingLat;

    public Coordinate(int shapeId, String startingLong, String startingLat) {
        this.shapeId = shapeId;
        this.startingLat = startingLat;
        this.startingLong = startingLong;
    }

    public int getShapeId() {
        return shapeId;
    }

    public String getStartingLong() {
        return startingLong;
    }

    public String getStartingLat() {
        return startingLat;
    }
}
