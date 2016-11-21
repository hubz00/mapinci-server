package mapinci.Coordinate;

import map.graph.graphElements.segments.SegmentImpl;

import java.util.ArrayList;
import java.util.Collection;

//TODO decide if Coordinates are valid or Node is enough
public class Coordinates {

    private Collection<SegmentImpl> segments;
    private ArrayList<Coordinate> coordinates;

    public Coordinates(Collection<SegmentImpl> segments) {
        this.segments = segments;
        this.createCoordinates();
    }

    private void createCoordinates() {
        for (SegmentImpl s: segments
             ) {
            this.coordinates.add(new Coordinate(s.getNode1().getLatitude(), s.getNode1().getLongitude()));
            this.coordinates.add(new Coordinate(s.getNode2().getLatitude(), s.getNode2().getLongitude()));
        }
    }

    public ArrayList<Coordinate> getCoordinates() {
        return this.coordinates;
    }
}
