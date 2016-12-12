package map.graph.graphElements;
import map.graph.graphElements.segments.Segment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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



    public JSONObject toJson() {
        JSONArray segmentsJson = new JSONArray(segments);
        JSONObject json = new JSONObject();

        try {
            json.put("segments", segmentsJson);
            json.put("length", length);
            json.put("radius", radius);
            json.put("startPoint", startPoint);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}