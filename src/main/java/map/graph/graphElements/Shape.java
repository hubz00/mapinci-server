package map.graph.graphElements;

import map.graph.graphElements.segments.Segment;

public class Shape {
    private Segment[] segments;
    private Double length;

    public Segment[] getSegments() {
        return segments;
    }

    public void setSegments(Segment[] segments) {
        this.segments = segments;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }
}
