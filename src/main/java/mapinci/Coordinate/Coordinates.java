package mapinci.Coordinate;

import map.graph.graphElements.Segment;
import se.kodapan.osm.util.Coordinate;

import java.util.ArrayList;
import java.util.Collection;

public class Coordinates {

    private Collection<Segment> segments;
    private ArrayList<Coordinate> coordinates;

    public Coordinates(Collection<Segment> segments) {
        this.segments = segments;
    }

    private void createCoordinates() {
        for (Segment s: segments
             ) {
            this.coordinates.add(new Coordinate(s.getNode1().getLatitude(), s.getNode1().getLongitude()));
            this.coordinates.add(new Coordinate(s.getNode2().getLatitude(), s.getNode2().getLongitude()));
        }
    }

    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }
}
