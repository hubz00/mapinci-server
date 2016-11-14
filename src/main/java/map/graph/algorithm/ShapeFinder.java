package map.graph.algorithm;

import map.graph.graphElements.Graph;
import map.graph.graphElements.Node;
import map.graph.graphElements.Segment;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShapeFinder {

    private final Graph graph;
    private final List<Segment> shape;
    private List<Segment> onMapSegments;
    private Double epsilon;
    private Logger log = Logger.getLogger("Shape Finder");

    public ShapeFinder(Graph graph, List<Segment> shape){
        this.shape = shape;
        this.graph = graph;
        this.onMapSegments = new LinkedList<>();
    }

    public Graph findShape(Node startNode, Double epsilon, Double nodeSearchEpsilon){
        this.epsilon = epsilon;
        Node node = graph.getNodeByCoordinates(startNode.getLongitude(), startNode.getLatitude(), nodeSearchEpsilon);
        log.log(Level.ALL,"Starting node: " + node);
        Graph result = new Graph();
        if(!findNextSegment(node, 0)) {
            //todo do sth about it
            System.out.println("Empty result graph");
            return result;
        }
        result.setSegments(onMapSegments);
        return result;
    }


    private boolean findNextSegment(Node startNode, int position){
        if(position == shape.size()) return true;

        List<Segment> possibleSegments = graph.getSegmentsForNode(startNode);
        Segment segmentToMap = shape.get(position);
        log.info(String.format("New call\n[Position: %s]\n[Start node: %s]\n[Segment to map: %s]",position, startNode, segmentToMap));

        for (Segment s: possibleSegments){
            if(position > 0) {
                if ((s.compareTo(onMapSegments.get(position-1)) != 0) && Math.abs(s.getSlope() - segmentToMap.getSlope()) <= epsilon) {
                    onMapSegments.add(s);
                    log.info(String.format("[Adding new node to result: %s]",s.getNeighbour(startNode)));
                    if (findNextSegment(s.getNeighbour(startNode), position + 1))
                        return true;
                    else
                        onMapSegments.remove(s);
                }
            } else {
                if (Math.abs(s.getSlope() - segmentToMap.getSlope()) <= epsilon) {
                    onMapSegments.add(s);
                    log.info(String.format("[Adding new node to result: %s]",s.getNeighbour(startNode)));
                    if (findNextSegment(s.getNeighbour(startNode), position + 1))
                        return true;
                    else
                        onMapSegments.remove(s);
                }
            }
        }

        return false;
    }

    public Graph getGraph() {
        return graph;
    }


}
