package map.graph.graphElements;

import map.graph.graphElements.segments.Segment;

import java.util.List;

public class Shape {
    private List<Segment> segments;
    private Double length;

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }
}
