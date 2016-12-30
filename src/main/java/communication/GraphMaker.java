package communication;

import computation.DataSculptor;
import computation.algorithm.ShapeFinderManager;
import computation.algorithm.conditions.ConditionFactory;
import computation.algorithm.conditions.ConditionManager;
import computation.graphElements.Graph;
import computation.graphElements.Node;
import computation.graphElements.Shape;
import computation.graphElements.segments.Segment;
import communication.osmHandling.MapFetcher;
import communication.osmHandling.MapFragment;

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
        cm.addPrimaryCondition(factory.newPrimaryCondition(15.0, 5*Math.PI/12));
        cm.addCondition(factory.newDirectionCondition(Math.PI/6));
        cm.addCondition(factory.newLengthCondition(0.3));

        ShapeFinderManager manager = new ShapeFinderManager(mapGraph,3, shape.getLength());
        List<List<Segment>> foundShapes = manager.findShapeConcurrent(shape.getSegments(),shape.getStartPoint(),cm,shape.getRadius());
        if(!foundShapes.isEmpty()) {
            List<Segment> shortest = foundShapes.get(0);
            for (List<Segment> tmp : foundShapes) {
                if (tmp.size() < shortest.size())
                    shortest = tmp;
            }
            return segmentsToNodeList(shortest);
        }
        return new LinkedList<>();
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
