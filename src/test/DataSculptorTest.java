
import map.graph.DataSculptor;
import map.graph.graphElements.Graph;
import map.graph.graphElements.OsmFetcher;
import map.graph.graphElements.segments.Segment;
import org.apache.lucene.search.Query;
import org.junit.Test;
import se.kodapan.osm.domain.OsmObject;
import se.kodapan.osm.domain.Way;
import se.kodapan.osm.domain.root.PojoRoot;
import se.kodapan.osm.domain.root.indexed.IndexedRoot;
import se.kodapan.osm.parser.xml.OsmXmlParserException;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class DataSculptorTest {

    @Test
    public void graphSculpting() throws IOException, OsmXmlParserException {

        OsmFetcher gf = new OsmFetcher();
        DataSculptor ds = new DataSculptor();
        IndexedRoot<Query> index = gf.makeGraph("test.osm");


        Map<OsmObject, Float> hits = ds.narrowDown(0.0,6.6, 6.6, 0.0, index);

        System.out.println("\nHits found: " + hits.size());

        PojoRoot pojo = (PojoRoot) index.getDecorated();

        pojo.getNodes().values().forEach(System.out::println);
        System.out.println("\n-----------------------------------------------------------------------\n");
        for (Way way : pojo.getWays().values()){
            way.getNodes().forEach(n -> System.out.println("ID: " + n.getId() + " ,long: " + n.getLongitude() + " ,lat: " + n.getLatitude()));
            System.out.println("");
        }

        Graph graph = ds.rebuildGraph(hits);

        Collection<Segment> segments = graph.getSegments().values();

        segments.forEach(System.out::println);
    }

}
