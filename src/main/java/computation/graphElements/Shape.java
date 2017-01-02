package computation.graphElements;
import computation.graphElements.segments.JSONSegment;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Shape {
    private List<Segment> segments;
    private Double length;
    private Double radius;
    private Node startPoint;

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("length: ").append(this.length).append("\n")
                .append("radius: ").append(this.radius).append("\n")
                .append("startPoint: ").append(this.startPoint.toString()).append("\n");
        segments.forEach(builder::append);
        return builder.toString();
    }

    public static Shape fromJson(JSONShape jsonShape) {
        Shape shape = new Shape();
        shape.setLength(jsonShape.getLength());
        shape.setRadius(jsonShape.getRadius());
        shape.setStartPoint(jsonShape.getStartPoint());
        List<Segment> segments = new LinkedList<>();

        SegmentFactory sf = new SegmentFactory();
        for (JSONSegment jsonSegment : jsonShape.getSegments()) {
            segments.add(sf.JSONSegment(jsonSegment));
        }
        shape.setSegments(segments);
        return shape;
    }

}