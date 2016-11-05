package map.graph.graphElements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Graph {

    private HashMap<Long, Segment> segments;

    public Graph(){
        this.segments = new HashMap<Long, Segment>();
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


    public HashMap<Long, Segment> getSegments() {
        return segments;
    }
}
