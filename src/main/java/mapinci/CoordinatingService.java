package mapinci;

import map.graph.graphElements.Node;
import map.graph.graphElements.Shape;
import org.springframework.stereotype.Component;
import se.kodapan.osm.parser.xml.OsmXmlParserException;

import java.io.IOException;

@Component
public class CoordinatingService {
    public Node[] startAlgo(Shape shape) throws IOException, OsmXmlParserException {
//        map.graph.graphElements.OsmFetcher gf = new map.graph.graphElements.OsmFetcher();
//        DataSculptor ds = new DataSculptor();
//
//        IndexedRoot<Query> index = gf.makeGraph("andorra-latest.osm");
//        Map<OsmObject, Float> hits = ds.narrowDown(42.5075208,42.509080,1.5319,1.530430, index);
//        Graph g = ds.rebuildGraph(index,hits);
//
//        ConditionManager cm = new ConditionManager();
//        ConditionFactory factory = new ConditionFactory();
//        cm.addPrimaryCondition(factory.newPrimaryCondition(10.0, 10.0));
//        cm.addCondition(factory.newCondition(0.25));
//        cm.addCondition(factory.newCondition(0.3,338.0));
//
//        ShapeFinder finder = new ShapeFinder(g, shape.getSegments(), cm);
//        NodeFactory nf = new NodeFactory();
//
//        List<Segment> segments = finder.findShape(nf.newNode(1.5305071,42.5082785), 0.00001);
//
//        return segments.stream().map(segment -> { return segment.getNode1(); }).collect(Collectors.toList());
        return null;

    }
}
