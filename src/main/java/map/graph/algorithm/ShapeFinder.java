package map.graph.algorithm;

import map.graph.algorithm.conditions.ConditionManager;
import map.graph.graphElements.Graph;
import map.graph.graphElements.Node;
import map.graph.graphElements.segments.Segment;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShapeFinder {

    private final Graph graph;
    private List<Segment> shape;
    private List<Segment> onMapSegments;
    private Double epsilon;
    private Logger log = Logger.getLogger("Shape Finder");
    private ConditionManager conditionManager;

    public ShapeFinder(Graph graph, List<Segment> shape, ConditionManager cm){
        this.shape = shape;
        this.graph = graph;
        this.onMapSegments = new LinkedList<>();
        this.conditionManager = cm;
    }

    public Graph findShape(Node startNode, Double epsilon, Double nodeSearchEpsilon){
        this.epsilon = epsilon;
        Node node = graph.getNodeByCoordinates(startNode.getLongitude(), startNode.getLatitude(), nodeSearchEpsilon);
        log.log(Level.ALL,"Starting node: " + node);
        Graph result = new Graph();
        //todo rotate shape to fit in first node
        if(isClosedShape()){
            log.info("Is a closed shape");
            for(int i = 0; i < shape.size(); i++){
                log.info(String.format("Taking next node to check: [Loop: %d]",i));
                if(findNextSegment(node,0)){
                    result.setSegments(onMapSegments);
                    return result;
                }
                onMapSegments = new LinkedList<>();
                Segment tmp = shape.remove(0);
                shape.add(tmp);
            }
        }
        //todo add checking from the end
        else if(findNextSegment(node,0)) {
            result.setSegments(onMapSegments);
            return result;
        }
        System.out.println("Empty result graph");
        return result;
    }

    private boolean findFirstSegment(Node startNode){
        //todo implement
        /* first cycle of searching for shape,
           adjust the shape coordinates to fit first segment
           and run recursion
          */
        return true;
    }

    private boolean findNextSegment(Node startNode, int position){
        if(position == shape.size()) return true;

        List<Segment> possibleSegments = graph.getSegmentsForNode(startNode);
        Segment segmentToMap = shape.get(position);
        log.info(String.format("New call\n[Position: %s]\n[Start node: %s]\n[Segment to map: %s]",position, startNode, segmentToMap));

        for (Segment s: possibleSegments){
            if(position == 0) rotateShapeToFit(s);
            log.info(String.format("[Checking Segment: %s]",s));
            if(((position > 0 && (s.compareTo(onMapSegments.get(position-1)) != 0)) || position == 0) && conditionManager.checkConditions(segmentToMap,s)){
                onMapSegments.add(s);
                log.info(String.format("[Adding new segment to result: %s]",s));
                if (findNextSegment(s.getNeighbour(startNode), position + 1))
                    return true;
                else
                    onMapSegments.remove(s);
            }
        }
        return false;
    }

    private void rotateShapeToFit(Segment s) {
        //todo implement shape rotation to fit first segment
    }

    private boolean isClosedShape() {
        if(shape.size() < 3) return false;
        Segment firstSegment = shape.get(0);
        Segment lastSegment = shape.get(shape.size()-1);
        Segment secondSegment = shape.get(1);
        Segment preLastSegment = shape.get(shape.size()-2);

        Node firstSegmentNode1 = firstSegment.getNode1();
        Node firstSegmentNode2 = firstSegment.getNode2();

        if(lastSegment.contains(firstSegmentNode1)
                && !preLastSegment.contains(firstSegmentNode1)
                && !secondSegment.contains(firstSegmentNode1)) return true;

        else if(lastSegment.contains(firstSegmentNode2)
                && !preLastSegment.contains(firstSegmentNode2)
                && !secondSegment.contains(firstSegmentNode2)) return true;

        return false;
    }


    public Graph getGraph() {
        return graph;
    }


}
