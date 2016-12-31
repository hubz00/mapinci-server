package computation.graphElements;

import computation.graphElements.segments.JSONSegment;

public class JSONShape {
    private JSONSegment[] segments;
    private Double length;
    private Double radius;
    private Node startPoint;

    public JSONSegment[] getSegments() {
        return segments;
    }

    public void setSegments(JSONSegment[] segments) {
        this.segments = segments;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Node getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Node startPoint) {
        this.startPoint = startPoint;
    }
}
