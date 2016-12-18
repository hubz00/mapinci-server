package computation.graphElements;

import computation.graphElements.segments.Segment;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Graph {
    private Map<Long, Segment> segments;
    private Map<Long, Node> nodes;

    public Graph(){
        this.segments = new ConcurrentHashMap<>();
        this.nodes = new ConcurrentHashMap<>();
    }

    public void addSegment(Segment s){
        segments.put(s.getId(), s);
    }

    void addNode(Node n){
        nodes.put(n.getId(),n);
    }

    public Map<Long, Node> getNodes(){
        return nodes;
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
        if (nodes.containsKey(id))
            return nodes.get(id);
        else
            return null;
    }

    public Node getNodeByCoordinates(Double lon, Double lat, Double epsilon){
        Segment segment =  segments.values().stream()
                .filter(s -> (((Math.abs(s.getNode1().getLongitude() - lon) <= epsilon) && (Math.abs(s.getNode1().getLatitude() - lat) <= epsilon))
                        || ((Math.abs(s.getNode2().getLongitude() - lon) <= epsilon) && (Math.abs(s.getNode2().getLatitude() - lat) <= epsilon))))
                .findFirst().get();
        if(segment != null){
            if((Math.abs(segment.getNode1().getLongitude() - lon) <= epsilon) && (Math.abs(segment.getNode1().getLatitude() - lat) <= epsilon))
                return segment.getNode1();
            else if((Math.abs(segment.getNode2().getLongitude() - lon) <= epsilon) && (Math.abs(segment.getNode2().getLatitude() - lat) <= epsilon)) {
                return segment.getNode2();
            }
        }
        return null;
    }

    public List<Node> getNodesWithinRadius(Double lon, Double lat, Double maxEpsilon, Double minEpsilon){
        return new LinkedList<>(segments.values().parallelStream()
                .filter(s -> (((Math.abs(s.getNode1().getLongitude() - lon) <= maxEpsilon) && (Math.abs(s.getNode1().getLatitude() - lat) <= maxEpsilon) && (Math.abs(s.getNode1().getLongitude() - lon) >= minEpsilon) && (Math.abs(s.getNode1().getLatitude() - lat) >= minEpsilon))
                        || ((Math.abs(s.getNode2().getLongitude() - lon) <= maxEpsilon) && (Math.abs(s.getNode2().getLatitude() - lat) <= maxEpsilon) && (Math.abs(s.getNode2().getLongitude() - lon) >= minEpsilon) && (Math.abs(s.getNode2().getLatitude() - lat) >= minEpsilon))))
                .map(seg -> {
                        if((Math.abs(seg.getNode1().getLongitude() - lon) <= maxEpsilon) && (Math.abs(seg.getNode1().getLatitude() - lat) <= maxEpsilon) && (Math.abs(seg.getNode1().getLongitude() - lon) >= minEpsilon) && (Math.abs(seg.getNode1().getLatitude() - lat) >= minEpsilon))
                            return seg.getNode1();
                        else
                            return seg.getNode2();})
                .collect(Collectors.toSet()));
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

    public void removeSegment(Segment s){
        segments.remove(s.getId());
    }

    public List<Segment> getSegmentsForNode(Node n){
        return segments.values().stream()
                .filter(s -> s.contains(n))
                .collect(Collectors.toList());
    }

    public void setSegments(List<Segment> sgmnts){
        sgmnts.forEach(s -> segments.put(s.getId(),s));
    }


    public Map<Long, Segment> getSegments() {
        return segments;
    }
}
