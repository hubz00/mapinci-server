package map.graph.graphElements;

import map.graph.graphElements.segments.Segment;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private HashMap<Long, Segment> segments;

    public Graph(){
        this.segments = new HashMap<>();
    }

    public void addSegment(Segment s){
        segments.put(s.getId(), s);
    }

    public Segment getSegment(Long key){
        return segments.get(key);
    }

    public List<Node> getNeighbours(Node n){
        return segments.values().stream()
                .filter(s -> s.contains(n))
                .map(s -> s.getNeighbour(n))
                .collect(Collectors.toList());
    }

    public Node getNodeById(Long id){
        Optional<Segment> segment =  segments.values().stream()
                .filter(s -> s.getNode1().getId() == id || s.getNode2().getId() == id)
                .findFirst();
        if(segment.isPresent()){
            Segment s = segment.get();
            if(segment.get().getNode1().getId() == id)
                return s.getNode1();
            else
                return s.getNode2();
        }
        return null;
    }

    public Node getNodeByCoordinates(Double lon, Double lat, Double epsilon){
        System.out.println(String.format("[GET_NODE_BY_COORDINATES]: Lon: %f, Lat: %f, epsilon: %f", lon, lat, epsilon));
        segments.values().forEach(System.out::println);
        Segment segment =  segments.values().stream()
                .filter(s -> (((Math.abs(s.getNode1().getLongitude() - lon) <= epsilon) && (Math.abs(s.getNode1().getLatitude() - lat) <= epsilon))
                        || ((Math.abs(s.getNode2().getLongitude() - lon) <= epsilon) && (Math.abs(s.getNode2().getLatitude() - lat) <= epsilon))))
                .findFirst().get();
        if(segment != null){
            System.out.println("Segment is present: " + segment);
            if((Math.abs(segment.getNode1().getLongitude() - lon) <= epsilon) && (Math.abs(segment.getNode1().getLatitude() - lat) <= epsilon))
                return segment.getNode1();
            else if((Math.abs(segment.getNode2().getLongitude() - lon) <= epsilon) && (Math.abs(segment.getNode2().getLatitude() - lat) <= epsilon)) {
                System.out.println("Second node: " + segment.getNode2() + " lat: " + segment.getNode2().getLatitude() + " lon: " + segment.getNode2().getLongitude());
                return segment.getNode2();
            }
        }
        return null;
    }

    public Node getNodeByCoordinates(Double lon, Double lat){
        return getNodeByCoordinates(lon,lat,0.0);
    }

    public boolean hasSegment(Segment s){
        for (Segment segment: segments.values()){
            if(segment.compareTo(s) == 0)
                return true;
        }
        return false;
    }

    public List<Segment> getSegmentsForNode(Node n){
        return segments.values().stream()
                .filter(s -> s.contains(n))
                .collect(Collectors.toList());
    }

    public void setSegments(List<Segment> sgmnts){
        sgmnts.forEach(s -> segments.put(s.getId(),s));
    }


    public HashMap<Long, Segment> getSegments() {
        return segments;
    }
}
