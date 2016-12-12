package computation.algorithm;

import computation.algorithm.conditions.ConditionManager;
import computation.graphElements.Graph;
import computation.graphElements.Node;
import computation.graphElements.segments.Segment;

import java.util.LinkedList;
import java.util.List;

public class ShapeFinderManager {

    private Graph graph;

    public ShapeFinderManager(Graph graph){
        this.graph = graph;
    }


    /**
     * Initiates searching for given shape from coordinates given in startNode
     * maxSearchEpsilon given in degrees
     * recommended value between: 0.005 - 0.05
     * shape found on map needs to meet conditions form conditionManager
     *
     * @param shape
     * @param startNode
     * @param conditionManager
     * @param startPointRange
     * @return list of segments from the map in asked shape
     */

    public List<Segment> findShape(List<Segment> shape, Node startNode, ConditionManager conditionManager, Double startPointRange){
        ShapeFinder finder = new ShapeFinder(graph);
        Double minSearchEpsilon = 0.0;


        while (minSearchEpsilon < startPointRange) {
            Double tempMaxSearch = minSearchEpsilon + 0.002;
            List <Node> nodes = graph.getNodesWithinRadius(startNode.getLongitude(), startNode.getLatitude(), tempMaxSearch, minSearchEpsilon);
            for(Node n: nodes){
                List<Segment> result = finder.findShapeForNode(n, shape,conditionManager);
                if(!result.isEmpty())
                    return result;
                conditionManager.reset();
            }
            minSearchEpsilon = tempMaxSearch;
        }


        return new LinkedList<Segment>();
    }


}
