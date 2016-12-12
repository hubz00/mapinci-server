
import computation.DataSculptor;
import computation.graphElements.Graph;
import computation.graphElements.NodeFactory;
import communication.osmHandling.MapFetcher;
import communication.osmHandling.MapFragment;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class DataSculptorTest {

    @Test
    public void graphSculpting()  {
        MapFetcher fetcher = new MapFetcher();
        NodeFactory nodeFactory = new NodeFactory();
        MapFragment mapFragment = fetcher.fetch(nodeFactory.newNode(19.1,50.0),5000.0);
        DataSculptor ds = new DataSculptor();

        Graph graph = ds.rebuildGraph(mapFragment);

        assertTrue(graph.getNodes().size() == 52506);
    }

}
