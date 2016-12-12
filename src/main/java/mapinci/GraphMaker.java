package mapinci;

import map.graph.DataSculptor;
import map.graph.algorithm.ShapeFinderManager;
import map.graph.algorithm.conditions.ConditionFactory;
import map.graph.algorithm.conditions.ConditionManager;
import map.graph.graphElements.Graph;
import map.graph.graphElements.Node;
import map.graph.graphElements.Shape;
import map.graph.graphElements.segments.Segment;
import map.graph.graphElements.segments.SegmentSoul;
import mapinci.osmHandling.MapFetcher;
import mapinci.osmHandling.MapFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GraphMaker {


    public List<Node> runApp (Shape shape) {

        MapFetcher mapFetcher = new MapFetcher();
        DataSculptor dataSculptor = new DataSculptor();
        MapFragment mapFragment = mapFetcher.fetch(shape.getStartPoint(),shape.getRadius());
        Graph mapGraph = dataSculptor.rebuildGraph(mapFragment);

        ConditionManager cm = new ConditionManager();
        ConditionFactory factory = new ConditionFactory();
        cm.addPrimaryCondition(factory.newPrimaryCondition(10.0, 10.0));
        cm.addCondition(factory.newCondition(0.25));
        cm.addCondition(factory.newCondition(0.3,shape.getLength()));

        ShapeFinderManager manager = new ShapeFinderManager(mapGraph);
        List<Segment> foundShape = manager.findShape(shape.getSegments(),shape.getStartPoint(),cm,shape.getRadius());

        return segmentsToNodeList(foundShape);
    }

    private List<Node> segmentsToNodeList(List<Segment> foundShape){
        List<Node> foundShapeNodes = new LinkedList<>();

        int index = 0;
        for (Segment foundSegment: foundShape){
            if(foundShapeNodes.isEmpty()){
                foundShapeNodes.add(index,foundSegment.getNode1());
                index++;
            }
            foundShapeNodes.add(index,foundSegment.getNeighbour(foundShapeNodes.get(index - 1)));
            index++;
        }

        return foundShapeNodes;
    }

}
