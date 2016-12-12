import map.graph.DataSculptor;
import map.graph.algorithm.ShapeFinderManager;
import map.graph.algorithm.conditions.ConditionFactory;
import map.graph.algorithm.conditions.ConditionManager;
import map.graph.graphElements.Graph;
import map.graph.graphElements.Node;
import map.graph.graphElements.NodeFactory;
import map.graph.graphElements.segments.Segment;
import map.graph.graphElements.segments.SegmentFactory;
import mapinci.osmHandling.MapFetcher;
import mapinci.osmHandling.MapFragment;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AppTest {

    @Test
    public void RealDataTest() {
        List<Segment> shape = new LinkedList<>();

        MapFetcher fetcher = new MapFetcher();
        NodeFactory nodeFactory = new NodeFactory();
        MapFragment mapFragment = fetcher.fetch(nodeFactory.newNode(1.5308,42.508),500.0);
        DataSculptor ds = new DataSculptor();

        Graph g = ds.rebuildGraph(mapFragment);


        ConditionManager cm = new ConditionManager();
        ConditionFactory factory = new ConditionFactory();
        cm.addPrimaryCondition(factory.newPrimaryCondition(10.0, 10.0));
        cm.addCondition(factory.newCondition(0.25));
        cm.addCondition(factory.newCondition(0.3,338.0));

        NodeFactory nf = new NodeFactory();
        List<Node> nodes = new LinkedList<>();
        nodes.add(0,nf.newNode(0.0,0.0));
        nodes.add(1,nf.newNode(1.530441,42.508301));
        nodes.add(2,nf.newNode(1.531757,42.508306));
        nodes.add(3,nf.newNode(1.531628,42.509072));
        nodes.add(4,nf.newNode(1.530771,42.50877));

        List<Integer> shapeNodes = new LinkedList<>();
        shapeNodes.add(0,1);
        shapeNodes.add(1,2);
        shapeNodes.add(2,2);
        shapeNodes.add(3,3);
        shapeNodes.add(4,3);
        shapeNodes.add(5,4);
        shapeNodes.add(6,4);
        shapeNodes.add(7,1);

        List<Double> percentLength = new LinkedList<>();
        percentLength.add(0,0.32544);
        percentLength.add(1,0.263313);
        percentLength.add(2,0.230769);
        percentLength.add(3,0.18047337);


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

        ShapeFinderManager manager = new ShapeFinderManager(g);
        manager.findShape(shape,nf.newNode(1.532178,42.507852), cm, 0.05).forEach(segment -> System.out.println(String.format("Lon: %s\tLat: %s  \t\tLon: %s\tLat: %s [Segment: %s]", segment.getNode1().getLongitude(),segment.getNode1().getLatitude(), segment.getNode2().getLongitude(), segment.getNode2().getLatitude(), segment)));
    }
}
