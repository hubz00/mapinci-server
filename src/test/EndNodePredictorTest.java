import communication.osmHandling.MapFetcher;
import communication.osmHandling.MapFragment;
import communication.osmHandling.DataSculptor;
import computation.algorithm.conditions.ConditionFactory;
import computation.algorithm.conditions.ConditionManager;
import computation.graphElements.Graph;
import computation.graphElements.Node;
import computation.graphElements.NodeFactory;
import computation.graphElements.Vector;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentFactory;
import computation.graphElements.segments.SegmentSoul;
import computation.algorithm.SegmentFinder;
import computation.utils.ReferenceRotor;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EndNodePredictorTest {

    @Test
    public void correctNodesPredicted(){

        List<Segment> shape =  new LinkedList<>();
        MapFetcher fetcher = new MapFetcher();
        NodeFactory nodeFactory = new NodeFactory();
        MapFragment mapFragment = fetcher.fetch(nodeFactory.newNode(19.9203659,50.0679934),2000.0);
        DataSculptor ds = new DataSculptor();

        Graph g = ds.rebuildGraph(mapFragment);

        ConditionManager cm = new ConditionManager();
        ConditionFactory factory = new ConditionFactory();
        cm.addPrimaryCondition(factory.newPrimaryCondition(15.0, 5*Math.PI/12));
        cm.addCondition(factory.newDirectionCondition(Math.PI/6));
        cm.addCondition(factory.newLengthCondition(0.3));

        NodeFactory nf = new NodeFactory();
        List<Node> nodes = new LinkedList<>();
        nodes.add(0,nf.newNode(0.0,0.0));
        nodes.add(1,nf.newNode(19.918203,50.067650));
        nodes.add(2,nf.newNode(19.920435,50.067006));
        nodes.add(3,nf.newNode(19.920823,50.068449));

        List<Integer> shapeNodes = new LinkedList<>();
        shapeNodes.add(0,1);
        shapeNodes.add(1,2);
        shapeNodes.add(2,2);
        shapeNodes.add(3,3);
        shapeNodes.add(4,3);
        shapeNodes.add(5,1);

        List<Double> percentLength = new LinkedList<>();
        percentLength.add(0,0.327272727);
        percentLength.add(1,0.290909090);
        percentLength.add(2,0.381818181);


        Iterator<Integer> i = shapeNodes.iterator();
        Iterator<Double> iPercent = percentLength.iterator();

        SegmentFactory sf = new SegmentFactory();

        int ind = 0;
        if(!percentLength.isEmpty()) {
            while (i.hasNext()) {
                Double percent = iPercent.next();
                shape.add(ind, sf.newSegment(nodes.get(i.next()), nodes.get(i.next()), percent ));
                ind++;
            }
        }

        shape.remove(0);

        Node startNode = g.getNodeById(267538595L);
        Node endNode = g.getNodeById(2564329474L);


        Segment startSegment = null;
        for( Segment s: g.getSegmentsForNode(startNode)){
            if (s.getNeighbour(startNode).getId() == 2564329474L)
                startSegment = s;
        }

        SegmentFinder segmentFinder = new SegmentFinder(g, cm);
        Vector mapVector =  startSegment.getVectorFromNode(startNode);
        Vector shapeVector;
        if(mapVector.getAngleBetween(shape.get(0).getVector1()) > mapVector.getAngleBetween(shape.get(0).getVector2()))
             shapeVector = shape.get(0).getVector2();
        else
            shapeVector = shape.get(0).getVector1();

        List<SegmentSoul> postShape = new LinkedList<>();
        shape.forEach(segment -> postShape.add(sf.newSegment(segment.getVector1(), segment.getVector2(), segment.getPercentLength(), 510)));
        ReferenceRotor referenceRotor = new ReferenceRotor();
        List<SegmentSoul> postShape2 = referenceRotor.rotateShapeToFit(postShape, new Vector(startNode,endNode), shapeVector);


        Map<Node, List<Segment>> foundNodes = segmentFinder.getNodes(startNode, startSegment,postShape2.get(0), shapeVector);

        foundNodes.entrySet().forEach( entry -> System.out.println(String.format("%s\t%s", entry.getKey(), entry.getValue().size())));

    }
}
