package map.graph;

import com.fasterxml.jackson.databind.ObjectMapper;
import map.graph.graphElements.*;
import org.apache.lucene.search.Query;
import se.kodapan.osm.domain.OsmObject;
import se.kodapan.osm.domain.root.indexed.IndexedRoot;
import se.kodapan.osm.parser.xml.OsmXmlParserException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Hubert on 01.11.2016.
 */
public class App {

    public static void main(String[] args) throws IOException, OsmXmlParserException {
        map.graph.graphElements.OsmFetcher gf = new map.graph.graphElements.OsmFetcher();
        DataSculptor ds = new DataSculptor();
        IndexedRoot<Query> index = gf.makeGraph("andorra-latest.osm");

        Map<OsmObject, Float> hits = ds.narrowDown(42.5110129,42.5209083,1.544432,1.527749, index);

        Graph g = ds.rebuildGraph(index,hits);

//        Graph g = getGraph();

        Collection<Segment> segments = g.getSegments().values();

        segments.forEach(System.out::println);
    }

    private static Graph getGraph() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File("./src/main/resources/graph.json"), Graph.class);
    }

}
