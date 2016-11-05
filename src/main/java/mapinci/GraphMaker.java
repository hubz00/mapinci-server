package mapinci;

import map.graph.DataSculptor;
import map.graph.graphElements.Graph;
import map.graph.graphElements.Node;
import map.graph.graphElements.Segment;
import org.apache.lucene.search.Query;
import se.kodapan.osm.domain.OsmObject;
import se.kodapan.osm.domain.root.indexed.IndexedRoot;
import se.kodapan.osm.parser.xml.OsmXmlParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by m on 05.11.16.
 */
public class GraphMaker {

    public GraphMaker(){};

    public ArrayList<Node> runApp() throws IOException, OsmXmlParserException {
        map.graph.graphElements.OsmFetcher gf = new map.graph.graphElements.OsmFetcher();
        DataSculptor ds = new DataSculptor();
        IndexedRoot<Query> index = gf.makeGraph("andorra-latest.osm");

        Map<OsmObject, Float> hits = ds.narrowDown(42.5110129,42.5209083,1.544432,1.527749, index);

        Graph g = ds.rebuildGraph(index,hits);

        Collection<Segment> segments = g.getSegments().values();

        ArrayList<Node> nodes = new ArrayList<>();

        for(Segment s: g.getSegments().values()) {
            nodes.add(s.getNode1());
            nodes.add(s.getNode2());
        }

        return nodes;
    }
}
