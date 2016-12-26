import communication.osmHandling.MapFetcher;
import communication.osmHandling.MapFragment;
import computation.DataSculptor;
import computation.algorithm.ShapeFinderManager;
import computation.algorithm.conditions.ConditionFactory;
import computation.algorithm.conditions.ConditionManager;
import computation.graphElements.Graph;
import computation.graphElements.Node;
import computation.graphElements.NodeFactory;
import computation.graphElements.segments.Segment;
import computation.graphElements.segments.SegmentFactory;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ConcurrentAppTest {

    @Test
    public void test(){
        List<Segment> shape = new LinkedList<>();

        MapFetcher fetcher = new MapFetcher();
        NodeFactory nodeFactory = new NodeFactory();
        MapFragment mapFragment = fetcher.fetch(nodeFactory.newNode(19.9203659,50.0679934),2000.0);
        DataSculptor ds = new DataSculptor();

        Graph g = ds.rebuildGraph(mapFragment);


        ConditionManager cm = new ConditionManager();
        ConditionFactory factory = new ConditionFactory();
        cm.addPrimaryCondition(factory.newPrimaryCondition(10.0, 10.0));
        cm.addCondition(factory.newDirectionCondition(0.8));
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

        shape.forEach(System.out::println);

        ShapeFinderManager manager = new ShapeFinderManager(g,4, 510.0);
        System.out.println(g.getNodes().values().size());
        manager.findShapeConcurrent(shape,nf.newNode(19.9202640, 50.0678914), cm, 0.05).forEach(list  -> list.forEach(segment -> System.out.println(String.format("Lon: %s\tLat: %s  \t\tLon: %s\tLat: %s [Segment: %s]", segment.getNode1().getLongitude(),segment.getNode1().getLatitude(), segment.getNode2().getLongitude(), segment.getNode2().getLatitude(), segment))));
    }
}
