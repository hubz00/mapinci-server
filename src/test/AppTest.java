import map.graph.DataSculptor;
import map.graph.algorithm.ShapeFinder;
import map.graph.algorithm.conditions.Condition;
import map.graph.algorithm.conditions.ConditionFactory;
import map.graph.algorithm.conditions.ConditionManager;
import map.graph.graphElements.Graph;
import map.graph.graphElements.Node;
import map.graph.graphElements.NodeFactory;
import map.graph.graphElements.segments.Segment;
import map.graph.graphElements.segments.SegmentFactory;
import map.graph.graphElements.segments.SegmentSoul;
import org.apache.lucene.search.Query;
import org.junit.Test;
import se.kodapan.osm.domain.OsmObject;
import se.kodapan.osm.domain.root.indexed.IndexedRoot;
import se.kodapan.osm.parser.xml.OsmXmlParserException;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AppTest {

    @Test
    public void RealDataTest() throws IOException, OsmXmlParserException {
        map.graph.graphElements.OsmFetcher gf = new map.graph.graphElements.OsmFetcher();
        DataSculptor ds = new DataSculptor();
        List<Segment> shape = new LinkedList<>();

        IndexedRoot<Query> index = gf.makeGraph("andorra-latest.osm");
        Map<OsmObject, Float> hits = ds.narrowDown(42.5075208,42.509080,1.5319,1.530430, index);
        Graph g = ds.rebuildGraph(index,hits);

        g.getSegments().values().forEach(System.out::println);

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

        ShapeFinder finder = new ShapeFinder(g,shape, cm);
        finder.findShape(nf.newNode(1.5305071,42.5082785), 0.00001).forEach(segment -> System.out.println(String.format("Lon: %s\tLat: %s  \t\tLon: %s\tLat: %s [Segment: %s]", segment.getNode1().getLongitude(),segment.getNode1().getLatitude(), segment.getNode2().getLongitude(), segment.getNode2().getLatitude(), segment)));
    }
}
