
import map.graph.DataSculptor;
import map.graph.graphElements.Graph;
import map.graph.graphElements.NodeFactory;
import map.graph.graphElements.segments.Segment;
import mapinci.osmHandling.MapFetcher;
import mapinci.osmHandling.MapFragment;
import org.junit.Test;

import java.util.Collection;

public class DataSculptorTest {

    @Test
    public void graphSculpting()  {
        MapFetcher fetcher = new MapFetcher();
        NodeFactory nodeFactory = new NodeFactory();
        MapFragment mapFragment = fetcher.fetch(nodeFactory.newNode(19.1,50.0),5000.0);
        DataSculptor ds = new DataSculptor();

        Graph graph = ds.rebuildGraph(mapFragment);

        Collection<Segment> segments = graph.getSegments().values();

        segments.forEach(System.out::println);
    }

}
