package map.graph.graphElements;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            else if(segment.get().getNode2().getId() == id)
                return s.getNode2();
        }
        return null;
    }

    public boolean hasSegment(Segment s){
        for (Segment segment: segments.values()){
            if(segment.compareTo(s) == 0)
                return true;
        }
        return false;
    }


    public HashMap<Long, Segment> getSegments() {
        return segments;
    }
}
