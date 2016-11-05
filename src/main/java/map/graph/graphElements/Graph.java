package map.graph.graphElements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

//    public Node getNode(Long id){
//        return segments.values().stream()
//                .filter(s -> s.getNode1().getId() == id || s.getNode2().getId() == id)
//    }


    public HashMap<Long, Segment> getSegments() {
        return segments;
    }
}
